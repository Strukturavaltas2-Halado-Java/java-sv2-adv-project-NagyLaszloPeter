package poster.parcels.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import poster.parcels.exception.model.ParcelType;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateParcelCommand {

    @NotBlank(message = "Sender ID must not blank.")
    @Size(min = 19, max = 19, message = "Sender ID is 19 character.")
    @Pattern(regexp = "[0-z-]+[0-z-]+[0-z]+[0-z]",
            message = "Wrong Sender ID format.")
    @Schema(name = "Please enter the ID of the Sender" +
            " (Pattern: XXXX-XXXX-XXXX-XXXX)", description = "ID of the Sender.",
            example = "1A2b-3C4d-5E6f-8G9H")
    private String senderId;


    @PastOrPresent(message = "Date can at take place in the future.")
    @NotNull(message = "Date must not null.")
    @Schema(name = "Please enter the fate of time the sending", description = "Date can at take place in the future.",
            example = "2022-02-22T22:22:22.222Z")
    private LocalDateTime sendingDateOfTime;


    @NotNull(message = "Parcel type must not be null.")
    @Schema(name = "Please enter the Parcel type (size).", description = "Parcel type",
            example = "HUGE")
    private ParcelType parcelType;
}
