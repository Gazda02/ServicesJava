package pl.service3.service3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.service3.service3.service.Service3;
import pl.service3.service3.service.sources.report.ServicePerformance;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@RestController
public class BackController {

    private static final Service3 service3 = new Service3();

    @PostMapping("/new")
    public ResponseEntity<String> getNewJsons(@RequestBody HashMap<String, Integer> newRequest){

        if (service3.newJsons(newRequest.get("size"))) {
            return new ResponseEntity<>("New jsons OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("New jsons failed", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/basic")
    public String basic() {

        return service3.basic();
    }

    @PostMapping("/select")
    public String select(@RequestBody HashMap<String, String[]> selectRequest) {

        return service3.select(selectRequest.get("selected"));
    }

    @PostMapping("/math")
    public Mono<String> math(@RequestBody HashMap<String, String> mathRequest) {

        return service3.math(mathRequest.get("text"));
    }

    @GetMapping("/data")
    public ResponseEntity<HashMap<String, ServicePerformance>> getReportsData() {

        return new ResponseEntity<>(service3.getBothServiceReports(), HttpStatus.OK);
    }


}
