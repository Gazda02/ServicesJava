package pl.service3.service3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    @GetMapping("/report")
    public String report() {
        return "reportSite";
    }
}
