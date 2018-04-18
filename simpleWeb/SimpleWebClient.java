/**
 * 
 */
package simpleWeb;
import java.io.*;
import java.net.*;

/**
* An application that opens a connection to a local Web server and reads
* a single Web page from the connection.
*/
public class SimpleWebClient {
    public static void main(String args[]) 
    {
        try
        {
            // Open a client socket connection
            Socket clientSocket = new Socket("localhost", 80);
            System.out.println("Client: " + clientSocket);

            // Get a Web page
            getPage(clientSocket); 
        }
        catch (UnknownHostException uhe)
        {
            System.out.println("UnknownHostException: " + uhe);
        }
        catch (IOException ioe)
        {
            System.err.println("IOException: " + ioe);
        }
    }

    	
	/**
     * 
     * Request a Web page using the passed client socket.
     * Display the reply and close the client socket.
     */
	public static void getPage(Socket clientSocket) 
    {
        
		try
        {
			 // Acquire the input and output streams
	        PrintWriter outbound = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader inbound = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            // Write the HTTP request to the server
            outbound.println("GET / HTTP/1.0");
            outbound.println();
            // Read the response
            String responseLine;
            while ((responseLine = inbound.readLine()) != null)
            { 
                // Display each line to the console
                System.out.println(responseLine); 
            } 

            // Clean up
            outbound.close(); 
            inbound.close(); 
            clientSocket.close(); 
        }
        catch (IOException ioe)
        {
            System.out.println("IOException: " + ioe);
        }
    }
}
