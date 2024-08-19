package pl.service2.service2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.service2.service2.service.Service2;
import pl.service2.service2.service.sources.report.ServicePerformance;

import java.util.Arrays;
import java.util.HashMap;

@RestController
public class MainController {

    //klasy pomocnicze
    private static final Logger log = LoggerFactory.getLogger(Service2.class);
    private static final Service2 service = new Service2();

    @GetMapping("/")
    public ResponseEntity<HttpStatus> home() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/new/{size}")
    public ResponseEntity<HttpStatus> newJsons(@PathVariable int size) {

        log.info("New request: new | {}", size);

        return new ResponseEntity<>(service.newJsons(size));
    }

    @GetMapping("/basic")
    public ResponseEntity<String> basic() {

        log.info("New Request: basic");

        return makeResponse(service.select(), "basic");
    }

    @GetMapping("/select/{heads}")
    public ResponseEntity<String> selected(@PathVariable String heads) {

        log.info("New Request: selected | {}", heads);

        return makeResponse(service.select(heads), "selected");
    }

    @PostMapping("/math")
    public ResponseEntity<String> math(@RequestBody HashMap<String, String[]> mathRequest) {

        log.info("New request: math | {}", Arrays.stream(mathRequest.get("expressions")).toList());

        return makeResponse(service.calculate(mathRequest.get("expressions")), "math");
    }

    @GetMapping("/report")
    public ResponseEntity<ServicePerformance> generateReport() {

        log.info("New request: generate report");

        return makeResponse(service.getResourcesUsageReport().getServicePerformance(), "generate report");
    }

    @GetMapping("/bridge")
    public ResponseEntity<ServicePerformance> bridge() {

        log.info("New request: bridge");

        return makeResponse(service.bridge(), "bridge");
    }

    private ResponseEntity<String> makeResponse(String body, String methodeName) {

        if (body.isEmpty()) {
            log.error("Request: {}", methodeName);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            log.info("Request: {} | Successful", body);
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
    }

    private ResponseEntity<ServicePerformance> makeResponse(ServicePerformance body, String methodeName) {

        if (body == null) {
            log.error("Request: {}", methodeName);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            log.info("Request: {} | Successful", body);
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
    }
}
