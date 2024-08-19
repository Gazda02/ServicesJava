package pl.service2.service2.service.sources.JsonClass;

/**
 * Rekord reprezentujący Jsona zwracanego przez serwis nr.1
 *
 * @param _type
 * @param _id
 * @param key
 * @param name
 * @param fullName
 * @param iata_airport_code
 * @param type
 * @param country
 * @param geo_position
 * @param location_id
 * @param inEurope
 * @param countryCode
 * @param coreCountry
 * @param distance
 */
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
