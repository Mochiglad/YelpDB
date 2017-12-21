package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;

/** NO NEED TO TEST THIS CLASS, MEANT PURELY FOR TESTING THE SERVER
 * 
 * YelpDBClient is a client that sends requests to the YelpDBServer
 * and interprets its replies.
 * A new YelpDBClient is "open" until the close() method is called,
 * at which point it is "closed" and may not be used further.
 */
public class YelpDBClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    // Rep invariant: socket, in, out != null
    
    /**
     * Make a YelpDBClient and connect it to a server running on
     * hostname at the specified port.
     * @throws IOException if can't connect
     */
    public YelpDBClient(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    
    /**
     * Send a request to the server. Requires this is "open".
     * @param x to find Fibonacci(x)
     * @throws IOException if network or server failure
     */
    public void sendRequest(String s) throws IOException {
        out.print(s + "\n");
        out.flush(); // important! make sure x actually gets sent
    }

    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     * @throws IOException if close fails
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
    
    
    
    
    private static final int N = 100;
    
    /**
     * Use a YelpDBServer to find the first N Fibonacci numbers.
     */
    public static void main(String[] args) {
    	String input = "";
        try {
            YelpDBClient client = new YelpDBClient("localhost", 4950);
                for(int i = 0; i < args.length - 1; i++) {
    				input += args[i] + " ";
    			}
                input += args[args.length - 1];
                client.sendRequest(input);
                client.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
