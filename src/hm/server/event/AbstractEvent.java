package hm.server.event;

import hm.Client;
import hm.server.Server;

/**
 * @author Ole Lorenzen
 *
 */
public abstract class AbstractEvent {

	public void performEvent (Server server, Client client, String[] request) {
		
	}
}
