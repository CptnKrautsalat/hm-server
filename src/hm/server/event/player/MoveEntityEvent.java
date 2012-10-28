/**
 * 
 */
package hm.server.event.player;

import hm.server.Server;
import hm.server.Server.ServerThread;
import hm.server.event.AbstractEvent;
import hm.server.event.EventManager;

/**
 * @author Ole Lorenzen
 *
 */
public class MoveEntityEvent extends AbstractEvent {
	
	public static final String CMD = "mve";

	public static void sendToClient (Server server, ServerThread thread, int entityID, int entityType, int direction) {
		//	TODO: logic
		float x = 0,y = 0;
		Object[] values = { entityID, entityType, x, y, direction };
		server.sendToClient(CMD, EventManager.buildResponse(values), thread);
	}
	
	@Override
	public void performEvent(Server server, ServerThread thread, String[] request) {
		int playerID = Integer.parseInt(request[0]);
		int direction = Integer.parseInt(request[1]);
		sendToClient(server, thread, playerID, 0, direction);
	}

}
