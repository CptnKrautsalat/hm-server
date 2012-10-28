package hm.server.event;

import hm.server.Server;
import hm.server.event.player.MoveEntityEvent;

import java.util.HashMap;

/**
 * @author Ole Lorenzen
 *
 */
public class EventManager {
	
	public static final String MESSAGE_DELIMITER = "#";
	public static final String VALUE_DELIMITER = "%";
	
	private Server server;
	private HashMap<String, AbstractEvent> events;
	
	public EventManager (Server server) {
		this.server = server;
		events = new HashMap<String, AbstractEvent>();
		events.put(MoveEntityEvent.CMD, new MoveEntityEvent());
	}
	
	public static String buildResponse (Object[] values) {
		StringBuilder response = new StringBuilder();
		for (Object value : values) {
			response.append(value).append(VALUE_DELIMITER);
		}
		response.append(MESSAGE_DELIMITER);
		return response.toString();
	}
	
	public void handleRequest (String request, Server.ServerThread thread) {
		String[] messageComponents = request.split(MESSAGE_DELIMITER);
		String cmd = messageComponents[0];
		AbstractEvent event = events.get(cmd);
		if (event == null) {
			server.addEntry("Unkown event " +  cmd + "received " + " from client " + thread.getId());
			return;
		}
		server.addEntry("Performing event " + cmd);
		event.performEvent(server, thread, messageComponents[1].split(VALUE_DELIMITER));
	}
}
