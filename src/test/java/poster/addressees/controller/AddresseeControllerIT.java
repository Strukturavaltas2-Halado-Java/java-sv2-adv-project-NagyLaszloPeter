package poster.addressees.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import poster.addressees.dto.AddresseeDto;
import poster.addressees.dto.CreateAddresseeCommand;
import poster.addressees.dto.UpdateAddresseeWithUnaddressedParcelCommand;
import poster.parcels.dto.CreateParcelCommand;
import poster.parcels.dto.ParcelDto;
import poster.parcels.exception.model.ParcelType;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"DELETE FROM parcels", "DELETE FROM addressees"})
class AddresseeControllerIT {

    @Autowired
    WebTestClient webTestClient;

    AddresseeDto addresseeDto;

    @BeforeEach
    void setUp() {
        addresseeDto = webTestClient.post()
                .uri("api/addressees")
                .bodyValue(new CreateAddresseeCommand("Training 360 Kft.", 1234, "Budapest", "111/B", Collections.singletonList(new CreateParcelCommand(
                        "1A2b-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.HUGE))))
                .exchange()
                .expectBody(AddresseeDto.class)
                .returnResult().getResponseBody();
    }


    @Test
    void testGetAllAddressesAndParcelsOptionalSettlementAndParcelType() {
        webTestClient.post()
                .uri("api/addressees")
                .bodyValue(new CreateAddresseeCommand("Mikrooszoft Nyrt.", 1235, "Budapest", "1/A", Collections.singletonList(new CreateParcelCommand(
                        "2222-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.HUGE))))
                .exchange();

        webTestClient.post()
                .uri("api/addressees")
                .bodyValue(new CreateAddresseeCommand("J??s Nyrt. ", 1236, "Budapest", "2/C", Collections.singletonList(new CreateParcelCommand(
                        "3333-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.OVERSIZE))))
                .exchange();

        List<AddresseeDto> resultAddresseeDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/addressees")
                        .queryParam("settlement", "Budapest")
                        .queryParam("parcelType", ParcelType.HUGE)
                        .build())
                .exchange()
                .expectBodyList(AddresseeDto.class)
                .returnResult().getResponseBody();

        assertThat(resultAddresseeDtos)
                .hasSize(2)
                .extracting(a -> a.getParcels().size(), AddresseeDto::getSettlement, AddresseeDto::getAddresseeName)
                .containsOnly(tuple(1, "Budapest", "Mikrooszoft Nyrt."),
                        tuple(1, "Budapest", "Training 360 Kft."));
    }

    @Test
    void testGetAllAddressees() {
        webTestClient.get()
                .uri("api/addressees")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AddresseeDto.class)

                .value(addresseeDtos -> assertThat(addresseeDtos)
                        .hasSize(1)
                        .extracting(AddresseeDto::getAddresseeName)
                        .isEqualTo(List.of("Training 360 Kft.")))
                .value(addresseeDtos -> assertThat(addresseeDtos)
                        .extracting(AddresseeDto::getParcels)
                        .hasSize(1)
                        .extracting(parcelDtos -> parcelDtos.get(0).getSendingDateOfTime())
                        .isEqualTo(List.of(LocalDateTime.of(2022, 6, 16, 16, 16, 16))));
    }

    @Test
    void testGetAddresseeById() {
        webTestClient.get()
                .uri("api/addressees/{id}", addresseeDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AddresseeDto.class)

                .value(addresseeDto -> assertThat(addresseeDto.getAddresseeName())
                        .isEqualTo("Training 360 Kft."))

                .value(addresseeDto -> assertThat(addresseeDto.getParcels())
                        .hasSize(1)
                        .extracting(ParcelDto::getSenderId)
                        .containsOnly("1A2b-3C4d-5E6f-8G9H"));
    }

    @Test
    void testPostAddressee() {
        webTestClient.post()
                .uri("api/addressees")
                .bodyValue((new CreateAddresseeCommand("Nanosoft Bt.", 55555, "Budapest", "100/A", Collections.singletonList(new CreateParcelCommand(
                        "555A-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.HUGE)))))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(AddresseeDto.class)

                .value(addresseeDto -> assertThat(addresseeDto.getAddresseeName())
                        .contains("Nanosoft Bt."))
                .value(addresseeDto -> assertThat(addresseeDto.getPostCode())
                        .isEqualTo(55555));
    }

    @Test
    void testPutParcelsAddressee() {
        AddresseeDto addresseeWithNewParcel = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/addressees/{id}/parcels")
                        .build(addresseeDto.getId()))
                .bodyValue(new CreateParcelCommand(
                        "2A2b-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.MINI))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AddresseeDto.class)
                .returnResult().getResponseBody();

        assertThat(addresseeWithNewParcel.getParcels())
                .hasSize(2)
                .extracting(ParcelDto::getSenderId)
                .contains("2A2b-3C4d-5E6f-8G9H", "1A2b-3C4d-5E6f-8G9H");
    }

    @Test
    void testParcelsNotFound() {
        webTestClient.put()
                .uri("api/addressees/{id}/parcels", Long.MAX_VALUE)
                .bodyValue(new UpdateAddresseeWithUnaddressedParcelCommand(Long.MAX_VALUE))
                .exchange()

                .expectStatus().isNotFound()
                .expectBody(ParcelDto.class)
                .returnResult().getResponseBody();
    }

    @Test
    void testLogisticsLoadOverLimit() {
        Problem problem = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/addressees/{id}/parcels")
                        .build(addresseeDto.getId()))
                .bodyValue(new CreateParcelCommand(
                        "OVER-SIZE-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.OVERSIZE))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();

        assertThat(problem)
                .extracting(Problem::getType, Problem::getTitle, Problem::getStatus, Problem::getDetail)
                .containsExactly(
                        URI.create("parcel/addressee-Logistics-Load-Over"),
                        "To much load capacity",
                        Status.NOT_ACCEPTABLE,
                        "This load: 18 is over maximum limit of: 10");
    }

    @Test
    void testDeleteAddresseeAndAddresseeNotFound() {
        webTestClient.delete()
                .uri("/api/addressees/{id}", addresseeDto.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        Problem problem = webTestClient.get()
                .uri("/api/addressees/{id}", addresseeDto.getId())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();

        assertThat(problem)
                .extracting(Problem::getType, Problem::getTitle, Problem::getStatus, Problem::getDetail)
                .containsExactly(
                        URI.create("addressees/addressee-NOT-FOUND"),
                        "Addressee not found",
                        Status.NOT_FOUND,
                        String.format("Addressee not found with ID: %d", addresseeDto.getId()));
    }
}
