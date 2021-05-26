package org.beyond.longpolling.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;

import org.beyond.longpolling.common.Message;
import org.beyond.longpolling.common.PollableMessageBroker;
import org.beyond.longpolling.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    private final PollableMessageBroker<Result<List<Message>>> messageBroker;

    public TestController(final PollableMessageBroker<Result<List<Message>>> messageBroker) {
        this.messageBroker = messageBroker;
    }

    @PostMapping("/send")
    public Result<?> pushMessage(HttpServletRequest request) {
        final String id = request.getSession().getId();
        LOGGER.debug("session id [{}]", id);
        IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .mapToObj(x -> {
                    final Message message = new Message();
                    message.setId(String.valueOf(x));
                    message.setPublishTime(LocalDateTime.now());
                    message.setPayload(String.format("message: %s", x));
                    return message;
                })
                .forEach(x -> this.messageBroker.publish(id, x));
        return Result.ok();
    }

    @GetMapping("/result")
    public DeferredResult<Result<List<Message>>> subscribe(HttpServletRequest request) {
        final String id = request.getSession().getId();
        LOGGER.debug("session id [{}]", id);
        return messageBroker.subscribe(id);
    }
}
