package poster.parcels.dto;

import lombok.*;
import poster.parcels.exception.model.ParcelType;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class ParcelDto {

    private Long id;
    private String senderId;
    private LocalDateTime sendingDateOfTime;
    private ParcelType parcelType;

}
