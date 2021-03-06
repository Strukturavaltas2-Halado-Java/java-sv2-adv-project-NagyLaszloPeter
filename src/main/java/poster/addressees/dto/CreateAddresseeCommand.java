package poster.addressees.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import poster.parcels.dto.CreateParcelCommand;

import javax.validation.constraints.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateAddresseeCommand {

    @NotBlank(message = "Addressee name must not blank.")
    @Size(min = 3, max = 100)
    @Schema(name = "Please type the name of the Addressee.", description = "Name of the Addressee.",
            example = "Training 360 Kft.")
    private String addresseeName;

    @NotNull(message = "Addressee postcode must not be null.")
    @Min(value = 1000, message = "Addressee postcode minimal number 1000.")
    @Max(value = 1_000_000, message = "Addressee postcode maximal number 1.000.000.")
    @Schema(name = "Please type the postcode of the Addressee.", description = "Postcode of the Addressee.",
            example = "1234")
    private int postCode;

    @NotBlank(message = "Addressee settlement must not be blank.")
    @Size(min = 2, max = 50)
    @Schema(name = "Please type the settlement of the Addressee.", description = "Settlement of the Addressee.",
            example = "Jászfelsőszentgyörgy")
    private String settlement;

    @NotBlank(message = "Addressee number must not be blank.")
    @Size(min = 1, max = 25)
    @Schema(name = "Please type the (location) number of the Addressee.", description = "Number of the Addressee.",
            example = "Hrsz: 12345/6789/01234565")
    private String addresseeNumber;

    @Schema(name = "Parcel(s) of the Addressee.", description = "Please type the data of Parcel.")
        private List<CreateParcelCommand> parcels;
}
