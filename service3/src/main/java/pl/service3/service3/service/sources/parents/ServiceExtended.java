package pl.service3.service3.service.sources.parents;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import pl.service3.service3.service.sources.WrongStatusCode;
import pl.service3.service3.service.sources.report.RequestStat;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ServiceExtended extends ServiceBasic{

    //zewnetrze klasy pomocnicze
    protected final OkHttpClient serviceConnection = new OkHttpClient();
    private static final Logger log = LoggerFactory.getLogger(ServiceExtended.class);

    //zmienne pola
    protected int numOfRequest = 0;
    protected int numOfJsons = 0;
    protected final HashMap<Integer, RequestStat> requestsStats = new HashMap<>();

    //parametry
    protected String homeUrl = null;
    protected final int statusOK = HttpStatus.OK.value();

    protected void connectionCheck() {

        if (homeUrl == null || homeUrl.isEmpty()) {
            log.error("No URL for connection check");
            return;
        }

        Request chceckRequest = new Request.Builder().url(homeUrl).build();
        int maxConnectionTrys = 3;
        int sleepTime = 2;

        do {
            maxConnectionTrys--;
            try (Response response = serviceConnection.newCall(chceckRequest).execute()) {

                if (response.code() == statusOK) {

                    log.info("Connection success with {}", homeUrl);
                    break;

                } else {
                    throw new WrongStatusCode(response.code());
                }

            } catch (Exception e) {
                log.error("Connection error | Attempts left: {} | {}", maxConnectionTrys, e.getMessage());
            }

            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (Exception e) {
                log.error("Interrupted while waiting for connection check");
            }

        } while (maxConnectionTrys > 0);
    }

    protected void updateRequestsStats(double responseTime) {
        requestsStats.put(numOfRequest, new RequestStat(numOfJsons, String.format("%.2f ms", responseTime)));
    }

    protected void constructorSetter() {
        log.info("Service started | Home URL: {}", homeUrl);
        setEndPoints();
        connectionCheck();
    }

    protected void setEndPoints() {}
}

