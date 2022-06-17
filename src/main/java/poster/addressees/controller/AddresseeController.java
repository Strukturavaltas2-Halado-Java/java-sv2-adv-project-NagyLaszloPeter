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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/addressees")
@AllArgsConstructor
public class AddresseeController {

    private AddresseeService addresseeService;


    @GetMapping
    @Operation(summary = "List All Addressees", description = "Show the Addresses list Java-Babe.")
    @ApiResponse(responseCode = "200", description = "Addresses-Query success.")
    public List<AddresseeDto> getAllAddressees() {
        return addresseeService.readAllAddressees();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Addressee-Creator")
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
            @Valid
            @PathVariable("id") Long id,
            @RequestBody CreateParcelCommand createCommand){
        return addresseeService.createParcelToAddressById(id, createCommand);
}

    @PutMapping("/{id}/parcels")
    @Operation(summary = "Update parcels Addressee")
    @ApiResponse(responseCode = "200",
            description = "Parcels Addressee updated.")
    public AddresseeDto putParcelsAddressee(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody UpdateAddresseeWithUnaddressedParcelCommand updateCommand) {
        return addresseeService.updateAddresseeWithUnaddressedParcel(id, updateCommand);
    }
}
