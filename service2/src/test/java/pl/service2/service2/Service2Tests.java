package pl.service2.service2;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.service2.service2.service.Service2;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Service2Tests {

    static Service2 service2;
    static final int size = 5;
    static final int testServicePort = 5000;

    @Container
    static GenericContainer<?> service1wraith = new GenericContainer<>("service1wraith:1.0")
            .withExposedPorts(5000);

    @BeforeAll
    static void setUp() {
        service2 = new Service2(String.format("http://%s:%d/",
                service1wraith.getContainerIpAddress(),
                service1wraith.getMappedPort(testServicePort)));
    }

    @Test
    @Order(1)
    @DisplayName("Generating new jsons test")
    void newJsonTest() {
        HttpStatus responseCode = service2.newJsons(size);

        Assertions.assertEquals(HttpStatus.OK, responseCode);
    }

    @Test
    @DisplayName("Basic function test")
    void basicTest() {
        String result = service2.select();

        String expected = "latitude,_type,name,_id,type,longitude<br/>" +
                        "-144.989497330727,Position,Dchbfrxwv,13785210,location,81.3144659272392<br/>" +
                        "-83.8508943481876,Position,Ysdzyxv,51668887,location,-69.3719772963255<br/>" +
                        "74.7239301517674,Position,Vokst,77216198,location,-82.8588705131302<br/>" +
                        "-50.757897501247,Position,Werrkhwz,27740675,location,69.0058295377179<br/>" +
                        "39.6575427978659,Position,Uzwnyrnbg,84355085,location,-42.84677424238<br/>";

        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Select function test")
    void selectTest() {
        String result = service2.select("coreCountry,distance,fullName,location_id,_id,key,iata_airport_code");

        String expected = "distance,iata_airport_code,fullName,_id,coreCountry,location_id,key<br/>" +
                        "null,null,Dchbfrxwv, Pydwxx,13785210,true,455811,81848678<br/>" +
                        "null,null,Ysdzyxv, Mrsspivd,51668887,true,778737,28574480<br/>" +
                        "3316,null,Vokst, Mpbgkynnsy,77216198,false,945330,29154072<br/>" +
                        "null,WER,Werrkhwz, Nalagnfqln,27740675,false,293366,50967442<br/>" +
                        "1372,null,Uzwnyrnbg, Fqkduqz,84355085,false,646449,41387019<br/>";

        Assertions.assertEquals(expected, result);
    }

    @Test
    void mathTest() {
        String[] listOfExpressions = new String[]{"latitude*longitude", "sqrt(_id)", "key", "key+1", "location_id/_id"};
        String result = service2.calculate(listOfExpressions);

        String expected = "key+1,location_id/_id,sqrt(_id),latitude*longitude,key<br/>" +
                        "8.1848679E7,0.03306521989871754,3712.843923463522,-11789.74354050694,8.1848678E7<br/>" +
                        "2.8574481E7,0.015071681338907107,7188.107330862555,5816.902338999058,2.857448E7<br/>" +
                        "2.9154073E7,0.012242638519964426,8787.274776630124,-6191.540452677481,2.9154072E7<br/>" +
                        "5.0967443E7,0.01057530143012021,5266.941712227315,-3502.590822664008,5.0967442E7<br/>" +
                        "4.138702E7,0.007663426573513618,9184.50243616931,-1699.1977832676832,4.1387019E7<br/>";

        Assertions.assertEquals(expected, result);
    }
}
