package pl.service1.service1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.service1.service1.service.sources.JsonClass.RandJson;
import pl.service1.service1.service.sources.parents.ServiceBasic;

/**
 * Serwis generujący zadaną liczbę losowych jsonów o określonym układzie
 */
@Service
public class Service1 extends ServiceBasic {

    private static final Logger log = LoggerFactory.getLogger(Service1.class);

    public Service1() {
        //uruchomienie wątku zbierającego informacje o zużyciu zasobów
        resourcesUsageReport.start();
        log.info("Service started");
    }

    /**
     * Generator jsonów
     * @param size ilość nowych jsonów
     * @return tablica losowych jsonów
     */
    public RandJson[] generateJsons(int size) {

        RandJson[] tableOfJsons = new RandJson[size];

        for(int i = 0; i < size; i++) {
            tableOfJsons[i] = new RandJson();
        }

        log.info("Method: generateJson | Size: {}", tableOfJsons.length);

        return tableOfJsons;
    }
}
