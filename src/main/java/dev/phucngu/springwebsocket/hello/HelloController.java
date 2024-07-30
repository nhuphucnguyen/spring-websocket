package dev.phucngu.springwebsocket.hello;

import dev.phucngu.springwebsocket.config.WebSocketConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    private final WebSocketConfig.SimpleWebSocketHandler simpleWebSocketHandler;

    public HelloController(WebSocketConfig.SimpleWebSocketHandler simpleWebSocketHandler) {
        this.simpleWebSocketHandler = simpleWebSocketHandler;
    }

    @GetMapping("")
    public String hello() {
        return "index";
    }

    @PostMapping("/broadcast/")
    @ResponseBody
    public void broadcast(@RequestBody Broadcast broadcast) {
        simpleWebSocketHandler.broadcast(null, broadcast.topic, broadcast.message);
    }

    public record Broadcast(String topic, String message) {
    }
}
