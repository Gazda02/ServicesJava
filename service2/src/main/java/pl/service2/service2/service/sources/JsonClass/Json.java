package pl.service2.service2.service.sources.JsonClass;

public record Json(
        String _type,
        int _id,
        Object key,
        String name,
        String fullName,
        Object iata_airport_code,
        String type,
        String country,
        GeoPosition geo_position,
        int location_id,
        boolean inEurope,
        String countryCode,
        boolean coreCountry,
        Object distance)
{
    public double latitude() {return geo_position.latitude();}
    public double longitude() {return geo_position.longitude();}
}
