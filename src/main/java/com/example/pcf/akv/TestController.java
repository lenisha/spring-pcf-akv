package com.example.pcf.akv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class TestController {

    @Value("${my-very-secret}")
    private String secretValue;

    @Value("${my-not-secret}")
    private String nosecretValue;

    @GetMapping("/values")
    public String[] Values() {
        return  new String[] { secretValue, nosecretValue};
    }
}
