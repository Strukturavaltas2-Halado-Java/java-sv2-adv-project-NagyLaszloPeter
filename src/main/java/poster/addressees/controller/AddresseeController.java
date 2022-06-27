package poster.addressees.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import poster.addressees.dto.AddresseeDto;
import poster.addressees.dto.CreateAddresseeCommand;
import poster.addressees.dto.UpdateAddresseeWithUnaddressedParcelCommand;
import poster.addressees.service.AddresseeService;
import poster.parcels.dtos.CreateParcelCommand;
import poster.parcels.model.ParcelType;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addressees")
@AllArgsConstructor
public class AddresseeController {

    private AddresseeService addresseeService;


    @GetMapping()
    @Operation(summary = "List Addressees' parcels",
            description = "List -all- Addressee -optional settlement-, AND Addressees' parcels -optional Parcel type-.")
    @ApiResponse(responseCode = "200", description = "Addresse & Parcells - Query success.")
    public List<AddresseeDto> listAddresseesAndParcelsOptionalSettlementAndParcelType(
            @Valid @RequestParam Optional<String> settlement,
            @Valid @RequestParam Optional<ParcelType> parcelType) {
        return addresseeService.getAddresseesAndParcelsOptionalSettlementAndParcelType(settlement, parcelType);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find one Addressee from ID")
    @ApiResponse(responseCode = "200", description = "Addressee-Query success.")
    public AddresseeDto getAddresseeById(
            @PathVariable("id") long id) {
        return addresseeService.readAddresseeFromId(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Addressee & Parcel-Creator")
    @ApiResponse(responseCode = "201", description = "Addressee created.")
    public AddresseeDto postAddressee(
            @Valid
            @RequestBody CreateAddresseeCommand createAddresseeCommand) {
        return addresseeService.createAddressee(createAddresseeCommand);
    }

    @PostMapping("/{id}/parcels")
    @Operation(summary = "Parcel-Creator to Addressee")
    @ApiResponse(responseCode = "201", description = "Parcel created to Addressee.")
    public AddresseeDto postParcelToAddressee(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody CreateParcelCommand createCommand) {
        return addresseeService.createParcelToAddressById(id, createCommand);
    }


    @PutMapping("/{id}/parcels")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update unaddressed parcels Addressee")
    @ApiResponse(responseCode = "200",
            description = "Parcels Addressee updated.")
    public AddresseeDto putParcelsAddressee(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody UpdateAddresseeWithUnaddressedParcelCommand updateCommand) {
        return addresseeService.updateAddresseeWithUnaddressedParcel(id, updateCommand);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "DELETE: Addressee with all parcels")
    @ApiResponse(responseCode = "204",
            description = "Addressee deleted.")
    public void deleteLocation(
            @PathVariable("id") long id) {
        addresseeService.deleteAddresseeById(id);
    }
}
