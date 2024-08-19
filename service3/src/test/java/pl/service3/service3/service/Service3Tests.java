package pl.service3.service3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.service3.service3.service.sources.report.ServicePerformance;
import reactor.core.publisher.Mono;
import java.util.HashMap;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Service3Tests {

    static Service3 service;
    static final int size = 1001;
    static final int testServicePort = 5000;

    @Container
    static GenericContainer<?> service2wraith = new GenericContainer<>("service2wraith:1.2")
            .withExposedPorts(5000);

    @BeforeAll
    static void setUp() {
        service = new Service3(String.format("http://%s:%d/",
                service2wraith.getContainerIpAddress(),
                service2wraith.getMappedPort(testServicePort)));
    }

    @Test
    @Order(1)
    @DisplayName("Zebranie raportów - odmowa")
    void getReport2SoonTest() {
        HashMap<String, ServicePerformance> response = service.getBothServiceReports();

        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("Nowe jsony")
    void newJsonTest() {
        boolean response = service.newJsons(size);

        Assertions.assertTrue(response);
    }

    @Test
    @DisplayName("Podstawowe zapytanie")
    void basicRequestTest() {
        String response = service.basic();

        Assertions.assertEquals("basic heads", response);
    }

    @Test
    @DisplayName("Wybrane pola")
    void selectRequestTest() {
        String heads = "_id, fullName, type, longitude, inEurope, countryCode, iata_airport_code".replace(" ", "");

        String response = service.select(heads.split(","));

        Assertions.assertEquals(heads, response);
    }

    @Test
    @DisplayName("Działania matematyczne")
    void mathRequestTest() {
        String expressions = "_id*_id, sqrt(latitude), location_id+longitude".replace(" ", "");

        Mono<String> response = service.math(expressions);

        Assertions.assertEquals(expressions, response.block());
    }

    @Test
    @DisplayName("Zebranie raportów - pomyślne")
    void getReportTest() {
        HashMap<String, ServicePerformance> response = service.getBothServiceReports();

        Assertions.assertFalse(response.isEmpty());
    }
}
