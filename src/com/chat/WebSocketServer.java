package com.chat;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
 
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
 
 
@ServerEndpoint(value = "/websocket/{username}")  
@Component
public class WebSocketServer {
	
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。  
    private static int onlineCount = 0;  
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。  
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据  
    private Session session;  
    private String username;  
	@OnOpen
	public void onOPen(@PathParam("username") String username, Session session) {
		  this.username = username;  
		  this.session = session;  
	        webSocketSet.add(this);     //加入set中  
	        addOnlineCount();           //在线数加1  
	        try {  
	             sendMessage("{\"msg\":\"success\"}");  
	        } catch (IOException e) {  
	           
	        }  
	}
 
	
	@OnMessage
	public void onMessage(String message, Session session) throws EncodeException, IOException {	
		session.getUserProperties();
			  System.out.println("用户登录！"+message);
			  System.out.println("当前在线人数！"+ onlineCount);
			  sendInfo("我已收到你的消息");
	}
	
	@OnClose
	public void onClose() {
		  webSocketSet.remove(this);  //从set中删除  
	      subOnlineCount();           //在线数减1  
	      System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());  	
	}
	
	@OnError
	public void onErroe(Session session, Throwable error) {
		 System.out.println("发生错误");  
	     error.printStackTrace();  
	}
	public  void sendMessage(String message) throws IOException {  
	          this.session.getBasicRemote().sendText(message);  
	}  
	  /** 
     * 群发自定义消息 
     * */  
    public  void sendInfo(String message) throws IOException {  
        for (WebSocketServer item : webSocketSet) {  
              item.sendMessage(message); 
            }  
    }
    /** 
     * 定向自定义消息 
     * */  
    public void sendSomeoneInfo(String message,String someone) throws IOException {  
        for (WebSocketServer item : webSocketSet) {  
            try { 
            	if(item.username.equals(someone)) {
            		item.sendMessage(message);  
            		System.out.println("开始推送信息到："+someone);
            	}
            } catch (IOException e) {  
                continue;  
            }  
        }  
    }
	
   public static synchronized int getOnlineCount() {  
        return onlineCount;  
    }  
  
    public static synchronized void addOnlineCount() {  
        WebSocketServer.onlineCount++;  
    }  
  
    public static synchronized void subOnlineCount() {  
        WebSocketServer.onlineCount--;  
    }  
 
}
