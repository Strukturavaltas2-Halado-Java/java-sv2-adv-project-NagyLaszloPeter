package poster.addressees.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import poster.addressees.dto.AddresseeDto;
import poster.addressees.dto.CreateAddresseeCommand;
import poster.addressees.dto.UpdateAddresseeWithUnaddressedParcelCommand;
import poster.addressees.exceptions.AddresseeNotFoundException;
import poster.addressees.exceptions.LogisticsLoadOverLimitException;
import poster.addressees.model.Addressee;
import poster.addressees.repository.AddresseeRepository;
import poster.parcels.dtos.CreateParcelCommand;
import poster.parcels.exceptions.ParcelNotFoundException;
import poster.parcels.model.Parcel;
import poster.parcels.model.ParcelType;
import poster.parcels.repository.ParcelRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddresseeService {

    private static final int LOGISTICS_CAPACITY = 10;

    private ParcelRepository parcelRepository;
    private AddresseeRepository addresseeRepository;
    private ModelMapper modelMapper;


    public List<AddresseeDto> readAllAddressees() {
        return parcelRepository.findAll().stream()
                .map(parcel -> modelMapper.map(parcel, AddresseeDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public AddresseeDto createAddressee(CreateAddresseeCommand createCommand) {
        List<Parcel> parcels = createCommand.getParcels().stream()
                .map(createParcelCommand -> modelMapper.map(createCommand, Parcel.class))
                .collect(Collectors.toList());
        Addressee addressee = modelMapper.map(createCommand, Addressee.class);
        parcels
                .forEach(parcel -> parcel.setAddressee(addressee));
        addresseeRepository
                .save(addressee);
        return modelMapper.map(addressee, AddresseeDto.class);
    }

    @Transactional
    public AddresseeDto createParcelToAddressById(Long id, CreateParcelCommand createParcelCommand) {
        Addressee addressee = addresseeRepository
                .findById(id)
                .orElseThrow(() -> new AddresseeNotFoundException(id));
        Parcel parcel = modelMapper
                .map(createParcelCommand, Parcel.class);
        parcelRepository
                .save(parcel);
        return modelMapper
                .map(addressee, AddresseeDto.class);
    }

    @Transactional
    public AddresseeDto updateAddresseeWithUnaddressedParcel(Long id, UpdateAddresseeWithUnaddressedParcelCommand updateCommand) {
        Addressee addressee = addresseeRepository
                .findById(id)
                .orElseThrow(() -> new AddresseeNotFoundException(id));
        Parcel parcel = parcelRepository
                .findById(updateCommand.getParcelId())
                .orElseThrow(() -> new ParcelNotFoundException(updateCommand.getParcelId()));

        addressChecker(parcel, addressee);
        addressee.addParcels(parcel);
        return modelMapper
                .map(addressee, AddresseeDto.class);
    }

    private void addressChecker(Parcel parcel, Addressee addressee) {
        if (parcel.getAddressee() != null) {
            throw new IllegalArgumentException("Parcel has one addressee! Addressee: " + parcel.getAddressee());
        }
        int logisticsLoad = parcelCapacityChecker(addressee, parcel.getParcelType());
        if (logisticsLoad > LOGISTICS_CAPACITY) {
            throw new LogisticsLoadOverLimitException(logisticsLoad);
        }
    }

    private int parcelCapacityChecker(Addressee addressee, ParcelType parcelType) {
        return Math.toIntExact(addressee.getParcels().stream()
                .filter(parcel -> parcel.getParcelType() == parcelType)
                .count());
    }
}
