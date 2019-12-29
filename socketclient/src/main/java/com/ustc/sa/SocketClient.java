package com.ustc.sa;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;

import java.net.URI;
import java.util.UUID;

/**
 * @author charles
 */

public class SocketClient extends WebSocketClient {

    private Logger logger;
    public static final String NAME="machine-"+UUID.randomUUID().toString().split("-")[1];

    public SocketClient(URI serverUri, Draft protocolDraft,Logger logger) {
        super(serverUri,protocolDraft);
        this.logger=logger;

    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("握手成功");
    }

    @Override
    public void onMessage(String msg) {
        logger.info("收到消息:" + msg);
        if (("admin:exit-"+NAME).equals(msg)) {
            this.close();
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("链接已关闭");
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        logger.info("发生错误已关闭");
    }

}
