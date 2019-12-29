package com.ustc.sa;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_6455;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * @author charles
 */
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);
    private final static String SOCKET_URI="ws://localhost:8888/websocket";

    @SneakyThrows
    public static void main(String[] args) throws InterruptedException {
        SocketClient client = null;

        try {
            client = new SocketClient(new URI(SOCKET_URI), new Draft_6455(), logger);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        client.connect();
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            logger.info("正在连接...");
        }
        MachineInfo machine = new MachineInfo();
        machine.setName(SocketClient.NAME);
        machine.setIpAddr(InetAddress.getLocalHost().getHostAddress());
        //连接成功,发送信息
        client.send("loginReq:"+machine.getName());

        while (true) {
            machine.setHeart(Math.random()*10);
            machine.setTime(System.currentTimeMillis());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client.send(JSON.toJSONString(machine));
        }
    }
}
