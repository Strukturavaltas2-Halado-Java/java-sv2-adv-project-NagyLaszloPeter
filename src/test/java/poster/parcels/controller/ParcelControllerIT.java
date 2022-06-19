package poster.parcels.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import poster.parcels.dtos.CreateParcelCommand;
import poster.parcels.dtos.ParcelDto;
import poster.parcels.model.ParcelType;
import poster.parcels.service.ParcelService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = "DELETE FROM parcels")
class ParcelControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ParcelService parcelService;

    ParcelDto parcelDto;


    @Test
    void testPostAndGetAllParcels() {
        webTestClient.post()
                .uri("api/parcels")
                .bodyValue(new CreateParcelCommand(
                        "1A2b-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.HUGE))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParcelDto.class);

        webTestClient.post()
                .uri("api/parcels")
                .bodyValue(new CreateParcelCommand(
                        "5555-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.HUGE))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParcelDto.class);

        webTestClient.get()
                .uri("api/parcels")
                .exchange()
                .expectBodyList(ParcelDto.class)
                .value(parcelDtos -> assertThat(parcelDtos)
                        .hasSize(2)
                        .extracting(ParcelDto::getSenderId)
                        .containsOnly("1A2b-3C4d-5E6f-8G9H", "5555-3C4d-5E6f-8G9H"));
    }

    @Test
    void testPostAndGetParcelById() {
        parcelDto = webTestClient.post()
                .uri("api/parcels")
                .bodyValue(new CreateParcelCommand(
                        "9999-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.HUGE))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParcelDto.class)
                .returnResult().getResponseBody();

        webTestClient.get()
                .uri("api/parcels/{id}", parcelDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParcelDto.class)

                .value(parcelDto -> assertThat(parcelDto)
                        .extracting(ParcelDto::getSenderId)
                        .isEqualTo("9999-3C4d-5E6f-8G9H"))
                .value(parcelDto -> assertThat(parcelDto)
                        .extracting(ParcelDto::getSendingDateOfTime)
                        .isEqualTo(LocalDateTime.of(2022, 6, 16, 16, 16, 16)));
    }

    @Test
    void testPostAndGetParcel() {
        webTestClient.post()
                .uri("api/parcels")
                .bodyValue(new CreateParcelCommand(
                        "1A2b-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.HUGE))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParcelDto.class)

                .value(parcelDto -> assertThat(parcelDto.getSenderId())
                        .isEqualTo("1A2b-3C4d-5E6f-8G9H"))
                .value(parcelDto -> assertThat(parcelDto.getSendingDateOfTime())
                        .isEqualToIgnoringNanos(LocalDateTime.of(2022, 6, 16, 16, 16, 16)));

        EntityExchangeResult<ParcelDto> entityExchangeResult = webTestClient.post()
                .uri("api/parcels")
                .bodyValue(new CreateParcelCommand(
                        "1A2b-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.HUGE))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParcelDto.class)
                .returnResult();

        assertThat(Objects.requireNonNull(entityExchangeResult.getResponseBody()).getSendingDateOfTime())
                .isEqualToIgnoringNanos(LocalDateTime.of(2022, 6, 16, 16, 16, 16));
        assertThat(entityExchangeResult.getResponseBody().getSenderId())
                .isEqualTo("1A2b-3C4d-5E6f-8G9H");
    }

    @Test
    void testDeleteParcelByIdAndNotFoundException() {
        parcelDto = parcelService.createParcel(
                new CreateParcelCommand("2A2b-3C4d-5E6f-8G9H", LocalDateTime.of(2022, 6, 16, 16, 16, 16), ParcelType.MINI));
        webTestClient.delete()
                .uri("api/parcels/{id}", parcelDto.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        Problem problem = webTestClient.get()
                .uri("/api/parcels/{id}", parcelDto.getId())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();

        assertThat(problem)
                .extracting(Problem::getType, Problem::getTitle, Problem::getStatus, Problem::getDetail)
                .containsExactly(
                        URI.create("parcels/parcel-NOT-FOUND"),
                        "Parcel not found",
                        Status.NOT_FOUND,
                        String.format("Parcel not found with ID: %d", parcelDto.getId()));
    }
}
