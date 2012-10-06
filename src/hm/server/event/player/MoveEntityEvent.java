/**
 * 
 */
package hm.server.event.player;

import hm.Client;
import hm.server.Server;
import hm.server.event.AbstractEvent;
import hm.server.event.EventManager;

/**
 * @author Ole Lorenzen
 *
 */
public class MoveEntityEvent extends AbstractEvent {
	
	private static final String CMD = "mve";

	public static void sendToClient (Server server, Client client, int entityID, int entityType, int direction) {
		//	TODO: logic
		float x = 0,y = 0;
		Object[] values = { entityID, entityType, x, y, direction };
		server.sendToClient(CMD, EventManager.buildResponse(values), client);
	}
	
	@Override
	public void performEvent(Server server, Client client, String[] request) {
		int playerID = Integer.parseInt(request[0]);
		int direction = Integer.parseInt(request[1]);
		sendToClient(server, client, playerID, 0, direction);
	}

}
