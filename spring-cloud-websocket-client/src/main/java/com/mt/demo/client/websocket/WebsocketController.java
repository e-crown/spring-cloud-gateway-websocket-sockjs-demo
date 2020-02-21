package com.mt.demo.client.websocket;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * WebsocketController
 *
 * @author mt.luo
 * @description:
 */
@RestController
@Slf4j
public class WebsocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private Environment environment;

    @GetMapping("/test")
    public String test(@RequestParam("name") String name){
        return environment.getProperty("cloud.eureka.host");
    }
    /**
     * 前端主动调用
     * @param msg
     * @return
     */
    @MessageMapping("/hello")
    //利用代理 重写发送地址，不重写的话就像http直接返回
    @SendTo("/topic/subscribe")
    public String say(String msg) {
        log.info("websocket msg: {}", msg);
        return msg;
    }

    /** 后端主动推送
     * 可以 定时任务触发 可以达到后端定时推送的效果
     * @param msg
     * @return
     */
    @GetMapping("/websocket/reply")
    public String msgReply(@RequestParam String msg) {
        log.info("websocket reply: {}", msg);
        //可以实现自由的向任意目的地发送消息，并且订阅此目的地的所有用户都能收到消息。(比如index.html中  stompCient.subscribe('/subscribe', function (response) {...})
        template.convertAndSend("/topic/subscribe", msg);
        return msg;
    }

    /**
     * 前端主动调用
     * @param msg
     * @return
     */
    @MessageMapping("/hello2")
    //利用代理 重写发送地址，不重写的话就像http直接返回
    @SendTo("/topic2/subscribe")
    public String say2(String msg) {
        log.info("websocket2 msg: {}", msg);
        return msg;
    }

    /** 后端主动推送
     * 可以 定时任务触发 可以达到后端定时推送的效果
     * @param msg
     * @return
     */
    @GetMapping("/websocket/reply2")
    public String msgReply2(@RequestParam String msg) {
        log.info("websocket2 reply: {}", msg);
        //可以实现自由的向任意目的地发送消息，并且订阅此目的地的所有用户都能收到消息。(比如index.html中  stompCient.subscribe('/subscribe', function (response) {...})
        template.convertAndSend("/topic2/subscribe", msg);
        return msg;
    }
}
