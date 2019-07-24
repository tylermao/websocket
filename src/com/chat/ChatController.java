package com.chat;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping(value="chat")
public class ChatController {

	@Autowired
	private WebSocketServer websocket;
	
	@RequestMapping(value="/toclient")
    public void sendMsg(@RequestParam("userName") String userName) {
        try {
			websocket.sendSomeoneInfo("»¶Ó­Á¬½ÓWebSocket£¡£¡£¡£¡", userName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
