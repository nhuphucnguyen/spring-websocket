package dev.phucngu.springwebsocket.hello;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public BackAndForth.Greeting greeting(BackAndForth.Hello message) throws Exception {
        Thread.sleep(1000);
        System.out.println("Received: " + message.message());
        return new BackAndForth.Greeting("Hello, " + message.message());
    }


    public record Broadcast(String topic, String message) {
    }
}
