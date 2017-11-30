package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.parser.ParseException;

public class YelpDBServer {
	/** Default port number where the server listens for connections. */
	private static int port;
	private ServerSocket serverSocket;
	private YelpDb database;
	private BigInteger newId;
	
	
	
	/**
	 * Start a YelpDBServer running on the default port.
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws InvalidPortException, ParseException{
		if(args.length > 0) {
			throw new InvalidPortException();
		}
		//port = Integer.parseInt(args[0]);
		port = 4949;
		if(port > 65535 || port < 0) {
			throw new InvalidPortException();
		}
		try {
			YelpDBServer server = new YelpDBServer(port);
			server.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Rep invariant: serverSocket != null
	//
	// Thread safety argument:
	// TODO FIBONACCI_PORT
	// TODO serverSocket
	// TODO socket objects
	// TODO readers and writers in handle()
	// TODO data in handle()

	/**
	 * Make a YelpDBServer that listens for connections on port.
	 * 
	 * @param port
	 *            port number, requires 0 <= port <= 65535
	 * @throws ParseException 
	 * @throws IOException
	 */
	public YelpDBServer(int port) throws IOException, ParseException {
		database = new YelpDb("data/users.json", "data/reviews.json", "data/restaurants.json");
		newId = BigInteger.valueOf(0);
		serverSocket = new ServerSocket(port);
	}

	/**
	 * Run the server, listening for connections and handling them.
	 * 
	 * @throws IOException
	 *             if the main server socket is broken
	 */
	public void serve() throws IOException {
		while (true) {
			// block until a client connects
			final Socket socket = serverSocket.accept();
			// create a new thread to handle that client
			Thread handler = new Thread(new Runnable() {
				public void run() {
					try {
						try {
							handle(socket);
						} finally {
							socket.close();
						}
					} catch (IOException ioe) {
						// this exception wouldn't terminate serve(),
						// since we're now on a different thread, but
						// we still need to handle it
						ioe.printStackTrace();
					}
				}
			});
			// start the thread
			handler.start();
		}
	}

	/**
	 * Handle one client connection. Returns when client disconnects.
	 * 
	 * @param socket
	 *            socket where client is connected
	 * @throws IOException
	 *             if connection encounters an error
	 */
	private void handle(Socket socket) throws IOException {
		System.err.println("client connected");

		// get the socket's input stream, and wrap converters around it
		// that convert it from a byte stream to a character stream,
		// and that buffer it so that we can read a line at a time
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));

		// similarly, wrap character=>bytestream converter around the
		// socket output stream, and wrap a PrintWriter around that so
		// that we have more convenient ways to write Java primitive
		// types to it.
		PrintWriter out = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()), true);

		try {
			// each request is a single line containing a number
			for (String line = in.readLine(); line != null; line = in
					.readLine()) {
				System.err.println("request: " + line);
				fillRequest(line);
			}
		} finally {
			out.close();
			in.close();
		}
	}

	private void fillRequest(String request) {
		String [] split = request.split(" ");
		String operation = split[0];
		System.out.println(request);
		String input = split[1];
		Map<String, YelpRestaurant> restaurants = database.getRestaurants();
		if(operation.equals("GETRESTAURANT")) {
			System.out.println(restaurants.get(input).toJson());
		}
		if(operation.equals("ADDRESTAURANT")) {
			System.out.println("Chose ADDRESTAURANT");
		}
		if(operation.equals("ADDREVIEW")) {
			System.out.println("Chose ADDREVIEW");
		}
		if(operation.equals("ADDUSER")) {
			System.out.println("Chose ADDUSER");
		}
	}

	
}

