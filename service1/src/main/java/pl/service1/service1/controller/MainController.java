package pl.service1.service1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.service1.service1.service.sources.JsonClass.RandJson;
import pl.service1.service1.service.Service1;
import pl.service1.service1.service.sources.report.ServicePerformance;

@RestController
public class MainController {

    private static final Service1 service = new Service1();
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public ResponseEntity<HttpStatus> home(){
        //pozwala na pingowanie serwisu
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/generate/json/{size}")
    public ResponseEntity<RandJson[]> generateJsons(@PathVariable int size){

        String logString = "Request: generate json | ";

        log.info("New request: generate json | Size: {}", size);

        RandJson[] listOfJsons = service.generateJsons(size);
        ResponseEntity<RandJson[]> response = new ResponseEntity<>(listOfJsons, HttpStatus.OK);

        if (listOfJsons.length == size) {

            log.info(logString + "Successful");

        } else if (listOfJsons.length > 0) {

            log.warn(logString + "Actual length: {}", size);

        } else {

            log.error(logString + "Empty list of jsons");
            response = new ResponseEntity<>(new RandJson[]{}, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return response;

    }

    @GetMapping("/report")
    public ResponseEntity<ServicePerformance> generateReport(){

        log.info("New request: report");

        return new ResponseEntity<>(service.getResourcesUsageReport().getServicePerformance(), HttpStatus.OK);
    }
}
