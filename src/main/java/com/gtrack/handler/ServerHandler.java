package com.gtrack.handler;

import com.gtrack.manger.NettyChannelMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * Created by ghkjg on 2017/7/11.
 */
public class ServerHandler  extends ChannelInboundHandlerAdapter {

    public static final AttributeKey<String> CLIENT_ID = AttributeKey.valueOf("client_id");


    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);


    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {



    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        Attribute<String> c =  ctx.channel().attr(CLIENT_ID);
        String clientId     =  c.get();
        NettyChannelMap.remove(clientId);
        ctx.close();
    }



}
