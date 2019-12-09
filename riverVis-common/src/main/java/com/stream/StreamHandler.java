package com.stream;

import com.infopublic.util.Logger;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StreamHandler implements WebSocketHandler {
	private final ArrayList<WebSocketSession> streams = new ArrayList<WebSocketSession>();
	private final ArrayList<String> streamids = new ArrayList<String>();
	private final ArrayList<Boolean> streamstates = new ArrayList<Boolean>();
//	private final ArrayList<VlcStreamThread> vlcthreads = new ArrayList<VlcStreamThread>();//vlc直播线程
	ExecutorService executorService =  Executors.newFixedThreadPool(100);
	protected Logger logger = Logger.getLogger("websocket");
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1)
			throws Exception {
		for(int i=0;i<streams.size();i++){
			if(session==streams.get(i)){
				if(streamids.size()>i){
					String sid = streamids.get(i);
					streamids.remove(i);
					if(streamstates.size()>i){						
						if(streamstates.get(i)){
							//ws连接结束，并直播未关闭则关闭直播
							sendCMDtoSocket(session,"end:"+sid);
							streamstates.set(i, false);
						}
						streamstates.remove(i);
					}
				}
			}
		}
		streams.remove(session);
		if(session.isOpen()) session.close();
		logger.info( "正常日志："+session.getRemoteAddress() + " 断开连接!" );
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		streams.add(session);
		logger.info( "正常日志："+session.getRemoteAddress() + ": 打开连接！"  );
	}

	@Override
	public void handleMessage(WebSocketSession conn, WebSocketMessage<?> message)
			throws Exception {
		System.out.println("String message: "+message.getPayload());
		String[] rs = message.getPayload().toString().split(":");
    	String commd="";
    	String imeilist="";
    	String streamid="";
    	if(rs.length>=2){
    		commd=rs[0];
    		streamid=rs[1];
    		if(rs.length>2)
    		imeilist=rs[2];
    	}
			switch(commd){
			case "start":
				for(int i=0;i<streams.size();i++){
					if(conn==streams.get(i)){
						streamids.add(i, streamid);
						streamstates.add(i,true);
					}
				}
				conn.sendMessage(new TextMessage("start:success"));
				
				logger.info( "正常日志："+conn.getRemoteAddress() + ":开始直播 ");
				break;
			case "end":
				for(int i=0;i<streams.size();i++){
					if(conn==streams.get(i)){
						if(streamstates.size()>i){
							streamstates.add(i,false);
						}						
					}
				}
				conn.sendMessage(new TextMessage("end:success"));
				logger.info( "正常日志："+ conn.getRemoteAddress() + ":结束直播 ");
				break;
			}
			sendCMDtoSocket(conn,message.getPayload().toString());
				
		System.out.println( conn.getRemoteAddress() + ": " + message.getPayload() );
	}
private void sendCMDtoSocket(WebSocketSession conn,String msg){
	//给后台socket服务器发送命令
	executorService.execute(new Runnable(){
			 @Override
			 public void run() {
				 try {
					 new NettyClient(conn,msg).start();
				 } catch (Exception e) {
					 logger.error( "错误日志："+e.getMessage()+ " ");
					 try {
						conn.sendMessage(new TextMessage("error:socketconnect"));
					} catch (IOException e1) {
						logger.error( "错误日志："+e1.getMessage()+ " ");
					}
				 } 
			 }
	});
}
	@Override
	public void handleTransportError(WebSocketSession session, Throwable arg1)
			throws Exception {
		if(session.isOpen()){
            session.close();
        }
		logger.error( "出错日志: IP:" +session.getRemoteAddress()+"  "+ arg1.getMessage() );
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	
}