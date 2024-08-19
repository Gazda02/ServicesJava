package pl.service1.service1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.service1.service1.service.Service1;
import pl.service1.service1.service.sources.JsonClass.RandJson;

class Service1Test {

    static Service1 service1;
    int size = 10;

    @BeforeAll
    static void setUp() {
        service1 = new Service1();
    }

    @Test
    @DisplayName("Generate size test")
    void sizeOfTableTest() {

        RandJson[] jsonTable = service1.generateJsons(size);

        Assertions.assertEquals(size, jsonTable.length);
    }
}
