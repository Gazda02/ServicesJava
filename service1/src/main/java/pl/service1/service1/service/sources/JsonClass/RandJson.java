package pl.service1.service1.service.sources.JsonClass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Ustawia pola na odpowiednie, losowe wartości, nie ma możliwości ustawiania wartości.
 * Nie na możliwości zmiany parametrów obietku.
 */
@Getter
public class RandJson {

    //pomocnicze
    //zewnętrzne klasy
    @JsonIgnore
    private final Random random = new Random();
    //parametry
    @JsonIgnore
    private final int maxStringLength = 10;
    @JsonIgnore
    private final int minStringLength = 4;

    //klucze json
    private final String _type;
    private final int _id;
    private final Object key;
    private final String name;
    private final String fullName;
    private final Object iata_airport_code;
    private final String type;
    private final String country;
    private final GeoPosition geo_position;
    private final int location_id;
    private final boolean inEurope;
    private final String countryCode;
    private final boolean coreCountry;
    private final Object distance;

    /**
     * Losuje wartości
     */
    public RandJson() {
        _type = "Position";
        _id = randInt(10_000_000, 100_000_000);
        key = random.nextBoolean() ? null : randInt(10_000_000, 100_000_000);
        name = randomString();
        country = randomString();
        fullName = name + ", " + country;
        iata_airport_code = random.nextBoolean() ? null : name.substring(0, 3).toUpperCase();
        type = "location";
        geo_position = new GeoPosition(rand_double(180), rand_double(90));
        location_id = randInt(100_000, 1_000_000);
        inEurope = random.nextBoolean();
        countryCode = country.substring(0, 2).toUpperCase();
        coreCountry = random.nextBoolean();
        distance = random.nextBoolean() ? null : randInt(1, 10_000);
    }

    /**
     *Tworzy losowe nazwy miast/państw, pierwsza litera jest zawsze duża
     *
     * @return losowy ciąg znaków
     */
    private String randomString() {
        StringBuilder new_string = new StringBuilder();
        int length = random.nextInt(maxStringLength - minStringLength) + minStringLength;

        new_string.append((char) ('A' + random.nextInt(26)));

        for(int i=0; i<length; i++) {
            new_string.append((char) ('a' + random.nextInt(26)));
        }

        return new_string.toString();
    }

    /**
     * Generuje pseudolosową wartość całkowitą z zakresu <min, max)
     *
     * @return pseudolosowa liczba
     */
    private int randInt(int min, int max){
        return random.nextInt(max - min) + min;
    }

    /**
     * Generuje pseudolosową wartość zmiennoprzecinkową z wybranego zakresu <-x, x>
     *
     * @param range zakres generowanej liczby
     * @return      pseudolosowa liczba
     */
    private double rand_double(int range){
        return random.nextDouble() + random.nextInt() % range;
    }
}

