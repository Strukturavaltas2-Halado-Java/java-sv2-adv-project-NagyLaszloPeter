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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddresseeService {

    private static final int LOGISTICS_CAPACITY = 10;

    private ParcelRepository parcelRepository;
    private AddresseeRepository addresseeRepository;
    private ModelMapper modelMapper;



    public AddresseeDto readAddresseeFromId(long id) {
        return modelMapper.map(validateAndGetAddresseeById(id), AddresseeDto.class);
    }

    @Transactional
    public AddresseeDto createAddressee(CreateAddresseeCommand createAddresseeCommand) {
        Addressee addressee = modelMapper
                .map(createAddresseeCommand, Addressee.class);
        addressee.getParcels()
                .forEach(addressee::assignParcel);
        addresseeRepository
                .save(addressee);
        return modelMapper
                .map(addressee, AddresseeDto.class);
    }

    @Transactional
    public AddresseeDto createParcelToAddressById(Long id, CreateParcelCommand createParcelCommand) {
        Addressee addressee = validateAndGetAddresseeById(id);
        Parcel parcel = modelMapper.map(createParcelCommand, Parcel.class);
        validateParcel(parcel, addressee);
        addressee.addParcels(parcel);
        parcelRepository.save(parcel);
        return modelMapper.map(addressee, AddresseeDto.class);
    }

    @Transactional
    public AddresseeDto updateAddresseeWithUnaddressedParcel(Long id, UpdateAddresseeWithUnaddressedParcelCommand updateCommand) {
        Addressee addressee = addresseeRepository
                .findById(id)
                .orElseThrow(() -> new AddresseeNotFoundException(id));
        Parcel parcel = parcelRepository
                .findById(updateCommand.getParcelId())
                .orElseThrow(() -> new ParcelNotFoundException(updateCommand.getParcelId()));
        validateParcel(parcel, addressee);
        addressee.addParcels(parcel);
        return modelMapper
                .map(addressee, AddresseeDto.class);
    }

    @Transactional
    public void deleteAddresseeById(long id) {
        parcelRepository.deleteAll(validateAndGetAddresseeById(id).getParcels());
        addresseeRepository.deleteById(id);
    }



    private Addressee validateAndGetAddresseeById(long id) {
        return addresseeRepository.findById(id)
                .orElseThrow(() -> new AddresseeNotFoundException(id));
    }

    private void validateParcel(Parcel parcel, Addressee addressee) {
        if (parcel.getAddressee() != null) {
            throw new IllegalArgumentException("Parcel already has one addressee by ID: " + parcel.getAddressee().getId());
        }
        int logisticsLoad = calculateLogisticsLoad(addressee, parcel.getParcelType());
        if (logisticsLoad > LOGISTICS_CAPACITY) {
            throw new LogisticsLoadOverLimitException(logisticsLoad, AddresseeService.LOGISTICS_CAPACITY);
        }
    }

    private int calculateLogisticsLoad(Addressee addressee, ParcelType parcelType) {
        return addressee.getParcels().stream()
                .mapToInt(value -> value.getParcelType().getCapacity())
                .sum() + parcelType.getCapacity();
    }

    public List<AddresseeDto> getAddresseesAndParcelsOptionalSettlementAndParcelType(
            Optional<String> settlement, Optional<ParcelType> parcelType) {

        return addresseeRepository.readAddresseesAndParcelsOptionalSettlementAndParcelType(settlement, parcelType)
                .stream()
                .map(addressee -> modelMapper.map(addressee, AddresseeDto.class))
                .collect(Collectors.toList());
    }
}
