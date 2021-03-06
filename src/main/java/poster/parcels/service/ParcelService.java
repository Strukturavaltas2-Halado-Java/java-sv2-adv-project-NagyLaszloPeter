package poster.parcels.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import poster.parcels.dto.CreateParcelCommand;
import poster.parcels.dto.ParcelDto;
import poster.parcels.exception.ParcelNotFoundException;
import poster.parcels.exception.model.Parcel;
import poster.parcels.repository.ParcelRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
public class ParcelService {

    private ParcelRepository parcelRepository;
    private ModelMapper modelMapper;


    public List<ParcelDto> readAllParcels() {
        return parcelRepository.findAll().stream()
                .map(parcel -> modelMapper.map(parcel, ParcelDto.class))
                .collect(Collectors.toList());
    }

    public ParcelDto readParcelFromId(long id) {
        return modelMapper.map(validateAndGetParcelId(id), ParcelDto.class);
    }


    public ParcelDto createParcel(CreateParcelCommand command) {
        Parcel parcel = modelMapper.map(command, Parcel.class);
        parcelRepository.save(parcel);
        return modelMapper.map(parcel, ParcelDto.class);
    }


    public void deleteParcelById(long id) {
        try {
            parcelRepository.deleteById(id);
        } catch (EmptyResultDataAccessException erdae){
            throw new ParcelNotFoundException(id);
        }
    }


    private Parcel validateAndGetParcelId(long id) {
        return parcelRepository.findById(id)
                .orElseThrow(() -> new ParcelNotFoundException(id));
    }
}
