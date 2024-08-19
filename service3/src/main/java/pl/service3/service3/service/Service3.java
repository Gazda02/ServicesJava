package pl.service3.service3.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.service3.service3.service.sources.ApiConf;
import pl.service3.service3.service.sources.WrongStatusCode;
import pl.service3.service3.service.sources.parents.ServiceExtended;
import pl.service3.service3.service.sources.report.ServicePerformance;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
public class Service3 extends ServiceExtended {

    //klasy pomocnicze
    private static final Logger log = LoggerFactory.getLogger(Service3.class);

    //zmienne pola
    @Getter
    HashMap<String, ServicePerformance> bothServiceReports = new HashMap<>();
    private int totalSizeOfRequests = 0;
    private boolean flag1k = true;
    private boolean flag10k = true;
    private boolean flag100k = true;

    //parametry
    private String newJsonsUrl;
    private String basicUrl;
    private String selectUrl;
    private String mathUrl;
    private String reportUrl;
    private String bridgeUrl;

    /**
     * Konstruktor bazowy
     */
    public Service3() {
        homeUrl = "http://172.20.1.2:5000/";
        constructorSetter();
    }

    /**
     * Konstruktor testowy
     *
     * @param homeUrl adres główny serwisu na którym będą wykonywane zapytania
     */
    public Service3(String homeUrl) {
        this.homeUrl = homeUrl;
        constructorSetter();
    }

    /**
     * Wysyła zapytanie generujące nową liste jsonów o podanym rozmiarze
     *
     * @param size rozmiar zarządanej listy
     * @return 'true' przy powodzeniu
     */
    public boolean newJsons(int size) {

        Request newJsonsRequest = new Request.Builder().url(newJsonsUrl + size).build();

        numOfRequest++;
        double responseTime;
        long startTime = System.nanoTime();
        boolean isSuccess = false;
        StringBuilder newJsonLog = new StringBuilder().append("Method: newJson | ");


        try (Response response = serviceConnection.newCall(newJsonsRequest).execute()) {

            responseTime = (System.nanoTime() - startTime) / 1_000_000.0;

            if (response.code() == statusOK) {

                isSuccess = true;
                numOfJsons = size;
                totalSizeOfRequests += size;
                log.info(newJsonLog.append("Success | Size: ").append(size).toString());

            } else {
                throw new WrongStatusCode(response.code());
            }

        } catch (Exception e) {

            numOfJsons = -1;
            responseTime = -1;
            log.error(newJsonLog.append(e.getMessage()).toString());
        }

        updateRequestsStats(responseTime);
        collectReports();
        return isSuccess;
    }

    /**
     * Wysyła zapytanie o podstawowe wartości
     *
     * @return otrzymana odpowiedź
     */
    public String basic() {

        StringBuilder logString = new StringBuilder().append("Method: basic | ");

        if (numOfJsons <= 0) {
            logString.append("Forbidden | No jsons");
            log.warn(logString.toString());
            return logString.toString();
        }

        Request selectRequest = new Request.Builder().url(basicUrl).build();

        numOfRequest++;
        String csvLikeResponse;
        double responseTime;
        long startTime = System.nanoTime();

        try (Response response = serviceConnection.newCall(selectRequest).execute()) {

            responseTime = (System.nanoTime() - startTime) / 1_000_000.0;

            if (response.code() == statusOK && response.body() != null) {

                log.info(logString.append("Success").toString());
                csvLikeResponse = response.body().string();

            } else {
                throw new WrongStatusCode(response.code());
            }

        } catch (Exception e) {
            responseTime = -1;
            logString.append(e.getMessage());
            csvLikeResponse = logString.toString();
            log.error(logString.toString());
        }

        updateRequestsStats(responseTime);
        return csvLikeResponse;
    }

    /**
     * Wysyła zapytanie o wybrane wartości
     *
     * @return otrzymana odpowiedź
     */
    public String select(String[] selectedHeads) {

        StringBuilder logString = new StringBuilder().append("Method: select | ");

        if (numOfJsons <= 0) {
            logString.append("Forbidden | No jsons");
            log.error(logString.toString());
            return logString.toString();
        }

        String selectedHeadsEndpoint = String.join(",", selectedHeads);

        Request selectRequest = new Request.Builder().url(selectUrl + selectedHeadsEndpoint).build();

        numOfRequest++;
        String csvLikeResponse;
        double responseTime;
        long startTime = System.nanoTime();

        try (Response response = serviceConnection.newCall(selectRequest).execute()) {

            responseTime = (System.nanoTime() - startTime) / 1_000_000.0;

            if (response.code() == statusOK && response.body() != null) {

                csvLikeResponse = response.body().string();
                log.info(logString.append("Success").toString());

            } else {
                throw new WrongStatusCode(response.code());
            }

        } catch (Exception e) {

            responseTime = -1;
            logString.append(e.getMessage());
            csvLikeResponse = logString.toString();
            log.error(logString.toString());
        }

        updateRequestsStats(responseTime);
        return csvLikeResponse;
    }

    /**
     * Wysyła zapytanie typu POST z działaniami matematycznymi na wartościach pól jsonów
     *
     * @return otrzymana odpowiedź
     */
    public Mono<String> math(String expressions) {

        StringBuilder logString = new StringBuilder().append("Method: math | ");

        if (numOfJsons <= 0) {
            logString.append("Forbidden | No jsons");
            log.error(logString.toString());
            return Mono.just(logString.toString());
        }

        if (expressions.isEmpty()) {
            logString.append("Forbidden | No expressions");
            log.error(logString.toString());
            return Mono.just(logString.toString());
        }

        //przygotowanie wyrażeń
        expressions = expressions.replace(" ", "");
        String[] expressionsArray = expressions.split(",");

        //stworzenie słownika/jsona z listą wyrażeń
        HashMap<String, String[]> expressionsJson = new HashMap<>();
        expressionsJson.put("expressions", expressionsArray);

        //budowa zapytania
        WebClient webClient = ApiConf.webClientBuilder().build();

        log.info(logString.append("Success").toString());

        return webClient
                .post()
                .uri(mathUrl)
                .bodyValue(expressionsJson)
                .retrieve()
                .bodyToMono(String.class);
    }
    /**
     * Pobiera raporty pozostałych serwisów (tylko gdy osiągnięto warunek)
     */
    private void collectReports() {

        StringBuilder logString = new StringBuilder().append("Method: collectReports | ");

        if (!reportsGuard()) return;

        Request reportS1Request = new Request.Builder().url(bridgeUrl).build();
        Request reportS2Request = new Request.Builder().url(reportUrl).build();

        ServicePerformance service1Performance;
        ServicePerformance service2Performance;
        ServicePerformance reportTmp;

        //raport serwisu nr. 1
        try (Response response = serviceConnection.newCall(reportS1Request).execute()){

            if (response.code() == statusOK && response.body() != null) {

                service1Performance = new ObjectMapper().readValue(response.body().string(), new TypeReference<>(){});
                log.info(logString.append("Service1 report successfully received").toString());

            } else {
                service1Performance = null;
                log.warn(logString.append("Service1 report NOT received | Status code: ").append(response.code()).toString());
            }

        } catch (Exception e) {
            log.error(logString.append("Terminated | ").append(e.getMessage()).toString());
            return;
        }

        //report serwisu nr. 2
        logString = new StringBuilder().append("Method: collectReports | ");

        try (Response response = serviceConnection.newCall(reportS2Request).execute()) {


            if (response.code() == statusOK && response.body() != null) {
                reportTmp = new ObjectMapper().readValue(response.body().string(), new TypeReference<>(){});
                service2Performance = new ServicePerformance(reportTmp.cpu(), reportTmp.memory(), requestsStats);
                log.info(logString.append("Service2 report successfully received").toString());

            } else {
                service2Performance = null;
                log.warn(logString.append("Service2 report NOT received | Status code: ").append(response.code()).toString());
            }

        } catch (Exception e) {
            log.error(logString.append("Terminated | ").append(e.getMessage()).toString());
            return;
        }

        bothServiceReports.put("service1", service1Performance);
        bothServiceReports.put("service2", service2Performance);
    }

    private boolean reportsGuard() {

        boolean guardFlag = false;

        if (flag1k && totalSizeOfRequests/1_000 >= 1) {
            flag1k = false;
            guardFlag = true;
            log.info("Report 1k enabled");
        }
        if (flag10k && totalSizeOfRequests/10_000 >= 1) {
            flag10k = false;
            guardFlag = true;
            log.info("Report 10k enabled");
        }
        if (flag100k && totalSizeOfRequests/100_000 >= 1) {
            flag100k = false;
            guardFlag = true;
            log.info("Report 100k enabled");
        }

        return guardFlag;
    }

    @Override
    protected void setEndPoints() {
        newJsonsUrl = homeUrl + "new/";
        basicUrl = homeUrl + "basic";
        selectUrl = homeUrl + "select/";
        mathUrl = homeUrl + "math";
        reportUrl = homeUrl + "report";
        bridgeUrl = homeUrl + "bridge";
    }
}
