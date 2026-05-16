package com.health.fatloss.controller;

import com.health.fatloss.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simple")
public class SimpleController {

    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello World");
    }

    @PostMapping("/echo")
    public Result<String> echo(@RequestBody String message) {
        return Result.success("Echo: " + message);
    }
}
