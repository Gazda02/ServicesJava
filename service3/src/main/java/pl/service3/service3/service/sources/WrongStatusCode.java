package pl.service3.service3.service.sources;

public class WrongStatusCode extends Exception {

    private static final String exceptionMessage = "Retrieve wrong status code: ";

    public WrongStatusCode(int statusCode) {
        super(exceptionMessage + statusCode);
    }
}
