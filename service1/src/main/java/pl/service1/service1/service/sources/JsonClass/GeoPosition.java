package pl.service1.service1.service.sources.JsonClass;

import lombok.Getter;
//może też rekord
/**
 * Pomocnicza klasa dla RandJson.
 * Przechowuje informacje o długości i szerokości geograficznej lokalizacji.
 */
/*@Getter
public class GeoPosition{
    private final double latitude;
    private final double longitude;

    public GeoPosition(double longitude,double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}*/

public record GeoPosition(double latitude, double longitude) {}
