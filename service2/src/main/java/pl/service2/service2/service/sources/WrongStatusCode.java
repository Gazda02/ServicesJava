package pl.service2.service2.service.sources;

/**
 * Wyjątek rzucany gdy kod opowiedzi jest inny niż oczekiwany.
 */
public class WrongStatusCode extends Exception {

    private static final String exceptionMessage = "Retrieve wrong status code: ";

    public WrongStatusCode(int statusCode) {
        super(exceptionMessage + statusCode);
    }
}
