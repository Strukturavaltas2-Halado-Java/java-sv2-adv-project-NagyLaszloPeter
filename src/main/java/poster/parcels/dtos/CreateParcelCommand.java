package poster.parcels.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import poster.parcels.model.ParcelType;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class CreateParcelCommand {

    @NotBlank(message = "Sender ID must not blank.")
    @Size(min = 19, max = 19, message = "Sender ID is 19 character.")
    @Pattern(regexp = "[0-z-]+[0-z-]+[0-z]+[0-z]",
            message = "Wrong Sender ID format.")
    @Schema(description = "ID of the Sender.",
            example = "1A2b-3C4d-5E6f-8G9H")
    private String senderId;

    @Past(message = "Date is not past or present.")
    @NotNull(message = "Date must not null.")
    private LocalDateTime sendingDateOfTime;

    @NotNull(message = "Parcel type must not be null.")
    private ParcelType parcelType;
}
