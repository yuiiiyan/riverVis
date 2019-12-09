package com.stream;

import com.infopublic.util.Convert;
import com.infopublic.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.UnsupportedEncodingException;

public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	// 消息的中止判断符
    public String EndChar = "cc";
    // 消息的开始判断符
    public static String BeginChar = "aa";
    public WebSocketSession conn;
    public String message;
    String streamid;
    ByteBuf encoded;
    protected Logger logger = Logger.getLogger("streamclient");//错误日志
    final static String UTF8 = "UTF8";
    public NettyClientHandler(WebSocketSession connection,String message){
    	this.conn = connection;
    	this.message = message;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//    	System.out.println("完成与服务器连接: "+ctx.channel().remoteAddress());
        String[] rs = message.split(":");
    	String commd="";
    	String imeilist="";
    	if(rs.length>=2){
    		commd=rs[0];
    		streamid=rs[1];
    		if(rs.length>2)
    			imeilist=rs[2];
    	}
        switch(commd){
		case "start":
			String sendtext = streamid+":"+imeilist;
			 SendToByte(ctx,"1","1",sendtext.getBytes(UTF8));
			 logger.info("正常日志  记录：前台直播开始命令  信息：直播编号" +streamid);
			break;
		case "end":
			SendToByte(ctx,"0","1",streamid.getBytes(UTF8));
			logger.info("正常日志  记录：前台直播结束命令  信息：直播编号" +streamid);
			break;
		}
       
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf buf = msg.readBytes(msg.readableBytes());
        String recstr = ByteBufUtil.hexDump(buf);
        if(recstr!=null && recstr!=""){
        	String result = parseResult(recstr);
        	String[] rs = result.split(":");
        	String byteType="";
        	String recievedata="";
        	if(rs.length>=2){
        		byteType=rs[0];
        		recievedata=rs[1];
        	}else{
        		byteType=rs[0];
        	}
        	logger.debug("跟踪日志  记录："+" 终端握手类型：" + byteType +"  信息：" + recievedata );
        	String sendstr = "";
        	switch(byteType){
        	case "0"://结束
        		sendstr = byteType+":success";//表示streamserver发送指令成功
        		conn.sendMessage(new TextMessage(sendstr));//找到streamid相同的发送指令
        		ctx.close();//结束后关闭与server连接
        		logger.info("正常日志  记录：服务器直播结束命令返回  信息：直播" +streamid);
        		break;
        	case "1"://开始
        		sendstr = byteType+":success";
        		conn.sendMessage(new TextMessage(sendstr));//找到streamid相同的发送指令
        		logger.info("正常日志  记录：服务器直播开始命令返回  信息：直播" +streamid);
        		//向服务器请求终端连接状态
        		SendToByte(ctx,"2","1",streamid.getBytes(UTF8));
        		break;
        	case "2"://终端连接状态
        		sendstr=byteType+":"+recievedata;
        		conn.sendMessage(new TextMessage(sendstr));//找到streamid相同的发送指令
        		logger.info("正常日志  记录：服务器终端连接状态返回  信息：直播" +streamid);
//        		if(!ctx.isRemoved()){
        			//向服务器请求终端连接状态,隔2秒发送一次请求
        			Thread.sleep(2000);
        			SendToByte(ctx,"2","1",streamid.getBytes(UTF8));
//        		}
        		break;
        	}
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
        ctx.close();
    }
    private void SendToByte(ChannelHandlerContext ctx, String byteType, String byteOrder, byte[] SendData){
		if (SendData != null && SendData.length != 0){
			byte[] checkData = new byte[SendData.length+3];//用来计算校验和
			try {
				int buffersize = 7+SendData.length;
				encoded = ctx.alloc().buffer(buffersize);//建立发送数据字节数组
				//加上包头尾
				encoded.writeByte(Convert.hexStringToBytes(BeginChar)[0]);//将开始标示复制到发送数据中去
				encoded.writeByte((byte)Integer.parseInt(byteType));//将类型复制到发送数据中去
				byte[] l = Convert.hexStringToBytes(Integer.toHexString(SendData.length+2));
				if(l.length==1){
					encoded.writeByte(l[0]);//将包长复制到发送数据中去
					encoded.writeByte((byte)0);
					checkData[0]=l[0];
					checkData[1]=(byte)0;
				}else if(l.length==2){
					encoded.writeByte(l[1]);//将包长复制到发送数据中去
					encoded.writeByte(l[0]);
					checkData[1]=l[0];
					checkData[0]=l[1];
				}
				encoded.writeByte((byte)Integer.parseInt(byteOrder));//将命令设置到发送数据中去
				checkData[2] = (byte)Integer.parseInt(byteOrder);
				encoded.writeBytes(SendData);//将数据字节复制到发送数据中去
				System.arraycopy(SendData, 0, checkData, 3, SendData.length);
				String check = Convert.checksum(checkData);
				encoded.writeByte(Convert.hexStringToBytes(check)[0]);//将校验复制到发送数据中去
				encoded.writeByte(Convert.hexStringToBytes(EndChar)[0]);//将结束标示复制到发送数据中去
				
				ctx.writeAndFlush(encoded);
			} catch (Exception ex) {
				logger.error("出错日志  记录："+ex.getMessage()+"  信息：ClientSocket错误,SendToByte  byteType:" + byteType + ",byteOrder:" + byteOrder);
			}
		}
	}
    public String parseResult(String str){
		byte[] content =Convert.hexStringToBytes(str);
		String byteType="";
		String byteOrder="";
		String acceptData="";
		if(content.length>0){
			if(Convert.byteToHexString(content[0]).equals(BeginChar)){
				byteType = Convert.byteToInt(content[1])+"";
				int byteLength = Convert.byteToInt(content[2])+Convert.byteToInt(content[3]);
				byteOrder = Convert.byteToInt(content[4])+"";
				//接收数据
				byte[] mybuf = new byte[content.length-7];
				for(int i=0;i<content.length-7;i++){
					mybuf[i] = content[i+5];
				}
				try {
							acceptData = new String(mybuf, "UTF-8");
						System.out.println(" 收到："+acceptData);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return byteType+":"+acceptData;
	}
}