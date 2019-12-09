package com.streamsocket.server;

import com.infopublic.util.Const;
import com.infopublic.util.Logger;
import com.infopublic.util.Tools;
import com.streamsocket.enty.Stream;
import com.streamsocket.enty.TerStream;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import com.socket.mapper.BaseAttribsMapper;

public class StreamServer {

//	public static void main(String[] args) {
//		StreamServer server= new StreamServer();
//		server.bind();
//	}
	private int PORT = 8800;//终端端口
	private static int Socketbacklog = 1000; //允许客户端数量
	final static String UTF_8 = "utf-8";
	public static ExecutorService executorService =  Executors.newCachedThreadPool();
     // 消息的中止判断符
     public String EndChar = "cc";
     // 消息的开始判断符
     public String BeginChar = "aa";
     
     protected Logger logger = Logger.getLogger("streamsocket");//正常日志
     private List<Stream> streamlist;
     private List<TerStream> terstreamlist;
	public StreamServer() {
			this.PORT= Integer.parseInt(Tools.GetValueByKey(Const.CONFIG, "SERVERPORT"));
			StreamServer.Socketbacklog = Integer.parseInt(Tools.GetValueByKey(Const.CONFIG, "SERVERbacklog"));
			streamlist = new ArrayList<Stream>();
			terstreamlist = new ArrayList<TerStream>();
//			//测试
//			streamlist.clear();
//			terstreamlist.clear();
//			streamlist.add(new Stream("201612"));
//			terstreamlist.add(new TerStream("201612", "862105022052751", false));
//			//测试-----end
	}

	public void bind() {

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();

			bootstrap.group(boss, worker);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.option(ChannelOption.SO_BACKLOG, Socketbacklog); //连接数
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);  //允许重复使用本地地址和端口
//			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //长连接
			bootstrap.childHandler(new NettyChannelHandler());
			
			ChannelFuture channelFuture = bootstrap.bind(PORT).sync();
			if (channelFuture.isSuccess()) {
				System.out.println("启动流媒体监听");
				logger.info("正常日志  信息： 开始启动StreamSocket监听，端口:" + PORT + "；" );
			}
			// 关闭连接
			channelFuture.channel().closeFuture().sync();
			
		} catch (Exception e) {
			logger.info("启动流媒体服务异常，异常信息：" + e.getMessage());
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	private class NettyChannelHandler extends ChannelInitializer<SocketChannel> {
		  
        @Override  
        protected void initChannel(SocketChannel socketChannel)
                throws Exception {  
        	// 自己的逻辑Handler
        	socketChannel.pipeline().addLast(new NettyServerHandler(streamlist,terstreamlist));  
        }  
    }  
}
