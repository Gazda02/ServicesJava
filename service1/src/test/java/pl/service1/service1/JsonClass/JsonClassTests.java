package pl.service1.service1.JsonClass;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.service1.service1.service.sources.JsonClass.RandJson;

import java.lang.reflect.InvocationTargetException;


class JsonClassTests {

    RandJson json1;
    RandJson json2;

    @BeforeEach
    void setUp() {
        json1 = new RandJson();
        json2 = new RandJson();
    }

    @Test
    @DisplayName("Różnica dwóch obiektów")
    void objectRandomnessTest() {
        Assertions.assertNotEquals(json1, json2);
    }

    //bez pól _type, type, distance oraz wszyskich typu boolen
    @ParameterizedTest
    @DisplayName("Różnica pojedyńczych pól")
    @ValueSource(strings = {"_id",
                            "Key",
                            "Name",
                            "FullName",
                            "Iata_airport_code",
                            "Country",
                            "Geo_position",
                            "Location_id",
                            "CountryCode"})
    void fieldRandomnessTest(String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        methodName = "get" + methodName;
        Assertions.assertNotEquals(RandJson.class.getMethod(methodName).invoke(json1),
                                    RandJson.class.getMethod(methodName).invoke(json2));
    }
}
