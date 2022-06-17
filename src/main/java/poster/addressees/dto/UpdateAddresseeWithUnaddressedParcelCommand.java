package poster.addressees.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class UpdateAddresseeWithUnaddressedParcelCommand {

    @NotNull(message = "Parcel ID must not be null.")
    private Long parcelId;
}
