package org.beyond.longpolling.controller;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.beyond.longpolling.common.result.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/deferred-result/demo")
public class DemoDeferredResultController {

    private static final long DEFAULT_TIMEOUT_VALUE = 60000;

    private final Map<String, DeferredResult<Result<String>>> map = new ConcurrentHashMap<>(16);

    @GetMapping("result/{id}")
    public DeferredResult<Result<String>> get(@PathVariable String id) {
        DeferredResult<Result<String>> result = map.get(id);
        if (Objects.nonNull(result)) {
            return result;
        }

        result = new DeferredResult<>(DEFAULT_TIMEOUT_VALUE);
        this.map.put(id, result);
        return result;
    }

    @PostMapping("send/{id}")
    public void send(@PathVariable String id, @RequestBody String message) {
        final DeferredResult<Result<String>> result = map.get(id);
        if (Objects.isNull(result)) {
            return;
        }
        result.setResult(Result.data(message));
    }

}
