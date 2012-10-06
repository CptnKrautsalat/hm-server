package hm.server.event;

import hm.Client;
import hm.server.Server;

import java.util.HashMap;

/**
 * @author Ole Lorenzen
 *
 */
public class EventManager {
	
	private static final String MESSAGE_DELIMITER = "#";
	private static final String VALUE_DELIMITER = "%";
	
	private Server server;
	private HashMap<String, AbstractEvent> events;
	
	public static String buildResponse (Object[] values) {
		StringBuilder response = new StringBuilder();
		for (Object value : values) {
			response.append(value).append(VALUE_DELIMITER);
		}
		response.append(MESSAGE_DELIMITER);
		return response.toString();
	}
	
	public void handleRequest (String request, Client client) {
		String[] messageComponents = request.split(MESSAGE_DELIMITER);
		AbstractEvent event = events.get(messageComponents[0]);
		if (event == null) {
			//TODO: error message
			return;
		}
		event.performEvent(server, client, messageComponents[1].split(VALUE_DELIMITER));
	}
}
