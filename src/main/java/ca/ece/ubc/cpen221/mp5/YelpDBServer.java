package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class YelpDBServer {
	/** Default port number where the server listens for connections. */
	private static int port;
	private ServerSocket serverSocket;
	private YelpDb database;
	private BigInteger newIdRestaurant;
	private BigInteger newIdReview;
	private BigInteger newIdUser;

	/**
	 * Start a YelpDBServer running on the default port.
	 * 
	 * @throws ParseException
	 */
	public static void main(String[] args) throws InvalidPortException, ParseException {
		if (args.length > 0) {
			throw new InvalidPortException();
		}
		// port = Integer.parseInt(args[0]);
		port = 4950;
		if (port > 65535 || port < 0) {
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
		newIdRestaurant = BigInteger.valueOf(0);
		newIdReview = BigInteger.valueOf(0);
		newIdUser = BigInteger.valueOf(0);
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
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
	 * @throws ParseException
	 */
	private void handle(Socket socket) throws IOException, ParseException {
		System.err.println("client connected");

		// get the socket's input stream, and wrap converters around it
		// that convert it from a byte stream to a character stream,
		// and that buffer it so that we can read a line at a time
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// similarly, wrap character=>bytestream converter around the
		// socket output stream, and wrap a PrintWriter around that so
		// that we have more convenient ways to write Java primitive
		// types to it.
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

		try {
			// each request is a single line containing a number
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				System.err.println("request: " + line);
				fillRequest(line);
			}
		} finally {
			out.close();
			in.close();
		}
	}

	private synchronized void fillRequest(String request) throws ParseException {
		String[] split; 
		String operation;
		String input = "";
		Map<String, YelpRestaurant> restaurants = database.getRestaurants();
		Map<String, YelpReview> reviews = database.getReviews();
		Map<String, YelpUser> users = database.getUsers();
		YelpRestaurant addRestaurant;
		YelpReview addReview;
		YelpUser addUser;
		
		request = request.trim();
		split = request.split(" ");
		if(split.length < 2) {
			System.err.println("ERR: ILLEGAL_REQUEST");
			return;
		}
		operation = split[0];
		if (operation.equals("GETRESTAURANT")) {
			input = split[1];
			if(!restaurants.containsKey(input)) {
				System.err.println("ERR: NO_SUCH_RESTAURANT");
				return;
			}
			System.out.println("ID: " + restaurants.get(input).getBusinessId() + "\nRESTAURANT: " + restaurants.get(input).toJson());
		}
		else if (operation.equals("GETREVIEW")) {
			input = split[1];
			if(!reviews.containsKey(input)) {
				System.err.println("ERR: NO_SUCH_REVIEW");
				return;
			}
			System.out.println("ID: " + reviews.get(input).getId() + "\nREVIEW: " + reviews.get(input).toJson());
		}
		else if (operation.equals("GETUSER")) {
			input = split[1];
			if(!users.containsKey(input)) {
				System.err.println("ERR: NO_SUCH_USER");
				return;
			}
			System.out.println("ID: " + users.get(input).getId() + "\nUSER: " + users.get(input).toJson());
		}
		else if (operation.equals("ADDRESTAURANT")) {
			input = split[1];
			JSONObject json = null;
			for (int i = 2; i < split.length - 1; i++) {
				split[i] = split[i].trim();
				input += split[i] + " ";
			}
			input += split[split.length - 1];

			JSONParser parser = new JSONParser();
			try {
				json = (JSONObject) parser.parse(input);
				addRestaurant = YelpDb.restaurantParser(json, true, newIdRestaurant + "");
				restaurants.put(newIdRestaurant + "", addRestaurant);
				System.out.println("ADDED RESTAURANT JSON:\n" + restaurants.get(newIdRestaurant + "").toJson());
				newIdRestaurant = newIdRestaurant.add(BigInteger.valueOf(1));
			} catch (Exception e) {
				System.err.println("ERR: INVALID_RESTAURANT_STRING");
			}

		}
		else if (operation.equals("ADDREVIEW")) {
			input = split[1];
			JSONObject json;
			for (int i = 2; i < split.length - 1; i++) {
				split[i] = split[i].trim();
				input += split[i] + " ";
			}
			input += split[split.length - 1];

			JSONParser parser = new JSONParser();
			try {
				json = (JSONObject) parser.parse(input);
				addReview = YelpDb.reviewParser(json, true, newIdReview + "");
				if(!restaurants.containsKey(addReview.getBusinessId())) {
					System.out.println("ERR: NO_SUCH_RESTAURANT");
					return;
				}
				if(!users.containsKey(addReview.getUserId())) {
					System.out.println("ERR: NO_SUCH_USER");
					return;
				}
				reviews.put(newIdReview + "", addReview);
				database.updateDB(addReview.getId(), addReview.getUserId());
				System.out.println("ADDED REVIEW JSON:\n" + reviews.get(newIdReview + "").toJson());
				newIdReview = newIdReview.add(BigInteger.valueOf(1));
			} catch (Exception e) {
				System.err.println(e);
			}

		}
		else if (operation.equals("ADDUSER")) {
			input = split[1];
			JSONObject json;
			for (int i = 2; i < split.length - 1; i++) {
				split[i] = split[i].trim();
				input += split[i] + " ";
			}
			input += split[split.length - 1];
			JSONParser parser = new JSONParser();
			try {
				json = (JSONObject) parser.parse(input);
				addUser = YelpDb.userParser(json, true, newIdUser + "");
				users.put(newIdUser + "", addUser);
				System.out.println("ADDED USER JSON:\n" + users.get(newIdUser + "").toJson());
				newIdUser = newIdUser.add(BigInteger.valueOf(1));
			} catch (Exception e) {
				System.err.println(e);
			}
		} else {
			try{
				for (int i = 0; i < split.length - 1; i++) {
					split[i] = split[i].trim();
					input += split[i] + " ";
				}
				if(split.length > 1) {
					input += split[split.length - 1];
				}
				System.out.println(input);
				QueryParser query = new QueryParser(input, database);
				query.findRestaurant();
			} catch (Exception e) {
				System.err.println("ERR: ILLEGAL_REQUEST");
			}
		}
	}

}
