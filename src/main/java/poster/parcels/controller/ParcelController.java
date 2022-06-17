package poster.parcels.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import poster.parcels.dtos.CreateParcelCommand;
import poster.parcels.dtos.ParcelDto;
import poster.parcels.service.ParcelService;

import javax.validation.Valid;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/parcels")
@Tag(name = "Parcel-Controller operations")
public class ParcelController {

    private ParcelService parcelService;

    @GetMapping
    @Operation(summary = "List All Parcels")
    @ApiResponse(responseCode = "200", description = "Parcels query success.")
    public List<ParcelDto> getAllParcels() {
        return parcelService.readAllParcels();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Parcel-Creator")
    @ApiResponse(responseCode = "201", description = "Parcel created.")
    public ParcelDto postNewParcel(
            @Valid
            @RequestBody CreateParcelCommand createCommand){
        return parcelService.createParcel(createCommand);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Parcel by ID.")
    @ApiResponse(responseCode = "204", description = "Parcel delete success.")
    public void deleteParcelById(
            @PathVariable("id") long id) {
        parcelService.deleteParcelById(id);
    }

}
