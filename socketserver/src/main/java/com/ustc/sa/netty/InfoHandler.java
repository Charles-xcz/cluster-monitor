package com.ustc.sa.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ustc.sa.mapper.MachineInfoMapper;
import com.ustc.sa.pojo.MachineInfo;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author charles
 */
@Component
@ChannelHandler.Sharable
public class InfoHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private MachineInfoMapper machineInfoMapper;

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Map<String, Channel> channelMap = new ConcurrentHashMap<>();
    @Value("${Admin.name}")
    private String adminName;
    @Value("${login.head}")
    private String loginHead;

    /**
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel incoming = ctx.channel();
        String content = msg.text();

        if (!channels.contains(incoming)) {
            //请登录
            if (content.startsWith(loginHead)) {
                login(incoming, content);
            } else {
                incoming.writeAndFlush(new TextWebSocketFrame("您未登录,请登录!"));
                incoming.close();
                return;
            }
        }

        if (incoming != channelMap.get(adminName)) {
            incoming.writeAndFlush(new TextWebSocketFrame(content));
            try {
                MachineInfo machineInfo = JSON.parseObject(content, MachineInfo.class);
                machineInfoMapper.add(machineInfo);
                if (null != channelMap.get(adminName)) {
                    channelMap.get(adminName).writeAndFlush(new TextWebSocketFrame(content));
                }
            } catch (JSONException e) {
                System.out.println();
            }
        } else {
            //管理员消息群发
            for (Channel channel : channels) {
                if (channel != incoming) {
                    channel.writeAndFlush(new TextWebSocketFrame(adminName + ":" + content));
                }
            }
        }
    }


    /**
     * 加入,提示所有人,加入连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        for (Channel channel : channels) {
//            channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + "加入连接"));
//        }
    }

    /**
     * 登录操作
     *
     * @param incoming 当前线程
     * @param content  消息内容
     * @return
     */
    public void login(Channel incoming, String content) {
        String name = content.split(":")[1];
        //简化登录操作
        if (channels.contains(incoming)) {
            incoming.writeAndFlush(new TextWebSocketFrame("请勿重复登录!"));
        } else {
            incoming.writeAndFlush(new TextWebSocketFrame("您已登录成功"));
            //登录成功的加入线程组
            channels.add(incoming);
            channelMap.put(name, incoming);
            if (channelMap.get(adminName) != null && !name.equals(adminName)) {
                channelMap.get(adminName).writeAndFlush(new TextWebSocketFrame(name + "登录成功"));
            }
        }
    }


    /**
     * 断开 提示
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channels.remove(ctx.channel());
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + "断开连接"));
        }
    }
}
