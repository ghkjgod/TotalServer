package com.gtrack.start;

import com.gtrack.config.ConstDefine;
import com.gtrack.handler.ServerHandler;
import com.gtrack.proto.Message;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created by ghkjg on 2017/7/11.
 */
public class StartServer {


    public static void startServer()  {

        Thread tcpThread = new Thread(new Runnable() {
            public void run() {
                //配置服务端NIO线程组
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {

                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast("frameDecoder",
                                                    new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                            .addLast(new ProtobufDecoder(Message.ctrlTransfer.getDefaultInstance()))
                                            .addLast("frameEncoder",new LengthFieldPrepender(4))
                                            .addLast(new ProtobufEncoder())
                                            .addLast( new ReadTimeoutHandler(600))
                                            .addLast(new ServerHandler());
                                }

                            });
                    //绑定端口，同步等待成功
                    ChannelFuture f = null;
                    try {
                        f = b.bind(ConstDefine.LISTEN_PORT).sync();
                        f.channel().closeFuture().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //等待服务端监听端口关闭

                } finally {
                    //退出时释放资源
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        });

        tcpThread.start();
    }
    public static void main(String[] args) throws Exception {


        startServer();
    }


}
