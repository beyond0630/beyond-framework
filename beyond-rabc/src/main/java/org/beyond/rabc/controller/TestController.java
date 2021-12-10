package org.beyond.rabc.controller;

import org.beyond.rabc.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Beyond
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public Result<?> test() {
        return Result.ok();
    }
}
