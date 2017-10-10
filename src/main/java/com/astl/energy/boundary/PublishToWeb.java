package com.astl.energy.boundary;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.json.Json;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/echo")
public class PublishToWeb {
	
	private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
	
    @OnMessage
    public String echo(String message) {
        System.out.println("Got message " + message);
        return message;
    }
    
    @OnOpen
    public void onOpen(Session session){
    	peers.add(session);
    	
        System.out.println(session.getId() + " has opened a connection"); 
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	
	@OnClose
    public void onClose(Session session){
		peers.remove(session);
		
        System.out.println("Session " +session.getId()+" has ended");
    }
	
	public void onEvent(@Observes @Default EnergyEvent event) {
		System.out.println("PublishToWeb Observed energy event - " + event.getEnergy() + " energy");
		for (Session peer: peers) {
			 try {
		            peer.getBasicRemote().sendText(toJsonFormat(event.getEnergy()));
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
			
		}
	}
	
	public String toJsonFormat(String e) {
		
		String[] parts = e.split("\t");
		String epoch = parts[0];
		String kwh = parts[1];
		
		
		Date d = new Date(Long.valueOf(epoch));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(d);
		
		String json = Json.createObjectBuilder()
	            .add("date", date)
	            .add("kwh", kwh)
	            .build()
	            .toString();
		
		System.out.println(json);

		
		return json;
	}
}
