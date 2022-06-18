package poster.addressees.dto;

import lombok.*;
import poster.parcels.dtos.ParcelDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class AddresseeDto {

    private Long id;
    private String addresseeName;
    private int postCode;
    private String settlement;
    private String addresseeNumber;
    private List<ParcelDto> parcels;
}
