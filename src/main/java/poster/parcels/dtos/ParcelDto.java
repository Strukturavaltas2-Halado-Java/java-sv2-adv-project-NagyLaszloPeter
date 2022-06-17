package poster.parcels.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import poster.parcels.model.ParcelType;
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
