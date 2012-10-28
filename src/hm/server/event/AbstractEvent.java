package hm.server.event;

import hm.server.Server;
import hm.server.Server.ServerThread;

/**
 * @author Ole Lorenzen
 *
 */
public abstract class AbstractEvent {

	public abstract void performEvent (Server server, ServerThread thread, String[] request);
}
