package hm.server;

import hm.server.event.EventManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * @author Ole Lorenzen
 *
 */
public class Server {

	private static final int DEFAULT_PORT = 8080;
	private EventManager eventManager;
	private ArrayList < Thread > threads;
	private Thread acceptThread;
	private ArrayList < String > log;
	private int lastID = 0;
	private ServerSocket socket;
	
	public void sendToClient (String command, String message, ServerThread thread) {
		try {
	        StringBuilder response = new StringBuilder();
	        
	        response.append(command);
	        response.append(EventManager.MESSAGE_DELIMITER);
	        response.append(message);
	        
	        thread.w.write(response.toString());
	        thread.w.newLine();
	        thread.w.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		if (args.length < 1) {
			System.out.print("No port specified, using default port " + DEFAULT_PORT);
		} else try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.print("Invalid port specified, using default port " + DEFAULT_PORT);
		}
		Server s = new Server();
		try {
			s.startServer(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addEntry(String e) {
		System.out.print(e);
		synchronized (log) {
			log.add(e);
		}
	}
	
	private void startAcceptLoop()
	  {
	    acceptThread = new Thread(new Runnable()
	    {
	
	      @Override
	      public void run()
	      {
	        try {
	          while (!socket.isClosed()) {
	            final Socket client = socket.accept();
	            final ServerThread st = new ServerThread(client);
	            final Thread t = new Thread(st);
	            threads.add(t);
	            t.start();
	          }
	        } catch (SocketException e) {
	          System.out.println("Server: shut down.");
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	
	      }
	    });
	    acceptThread.start();
	  }
	
	public void startServer(int port) throws IOException {
		System.out.println("Server: started.");
		socket = new ServerSocket(port);
		eventManager = new EventManager(this);
		startAcceptLoop();
	}
	
	public void stopServer() throws IOException, InterruptedException {
		socket.close();
		acceptThread.join();
		for (Thread t : threads) {
		  t.join();
		}
	}
	
	public class ServerThread implements Runnable {

		private Socket s;
		private int id;
		private BufferedWriter w;
		
		public ServerThread(Socket s)
	    {
	      this.s = s;
	    }
		
		@Override
		public void run() {
			id = lastID++;
			addEntry("client connected," + id);
			try {
		        w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		        w.write(Integer.toString(id));
		        w.newLine();
		        w.flush();
		        final BufferedReader r =
		            new BufferedReader(new InputStreamReader(s.getInputStream()));
		        boolean disconnected = false;
		        while (!s.isClosed() && !disconnected) {
		          final String s = r.readLine();
		          if (s == null) {
		            // client disconnected
		            disconnected = true;
		          }
		          if (!disconnected) {
		            eventManager.handleRequest(s, this);
		          } else {
		            addEntry("client disconnected," + id);
		          }
		        }
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
		}

		public int getId() {
			return id;
		}
		
	}
	
}
