/**
 * 
 */
package simpleWeb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


/**
* An application that listens for connections and serves a simple 
* HTML document.
*/
class SimpleWebServer {
	
    public static void main(String args[]) 
    {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try
        {
            // Create the server socket, fila de espera com 5 ligações pendentes
            serverSocket = new ServerSocket(80, 5);
            
            while (true)
            { 
                // Wait for a connection
            	System.out.println("Espera pelo estabelecimento do circuito virtual...");
                clientSocket = serverSocket.accept();
                System.out.println("Cliente: " + clientSocket);
                //Service the connection
                ServiceClient(clientSocket);  
            } 
             
        }
        catch (IOException ioe)
        {
            System.out.println("Error in SimpleWebServer: " + ioe);
        }
        finally {
        	if (serverSocket!=null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
    }

    private static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.ROOT); // exemplo (Locale.US)
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
    
	public static void ServiceClient(Socket client)
        throws IOException 
    {
		BufferedReader inbound = null;
        PrintWriter outbound = null;
        
        client.setSoTimeout(2);   // tempo máximo de espera na leitura em milisegundos
        
        try
        {
            // Acquire the streams for IO
        	outbound = new PrintWriter(client.getOutputStream(), true);
            inbound = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            
            String inputLine;
          
            while ((inputLine = inbound.readLine()) != null)
            {
                // If end of HTTP request, send the response
            	System.err.println(inputLine);
            	// nao é analizado o caminho indicado no URL
            	// porque a resposta é virtualizada
                if ( inputLine.equals("") )
                { // Format the output (response header and tiny HTML document)
                	String htmlPage="<HTML>"+"\n"+
    						"<HEAD><TITLE>Simple Virtual HTML Document</TITLE></HEAD>"+"\n"+
    						"<BODY>"+"\n"+
    						"<H1>"+"Virtual HTML"+"</H1>"+"<HR>"+"\n"+
    						"Hey look, I just created a virtual (yep, virtual) HTML document!"+"\n"+
    						"</BODY></HTML>"+"\n";
                    outbound.print(
                    		"HTTP/1.1 200 OK\n"+
                    		"Date: "+getServerTime()+"\n"+
                    		"Server: MyServer/0.99 (Win32)\n"+
                    		"Last-Modified: Wed, 22 Jul 2017 19:15:56 GMT\n"+
                    		"Content-Length: "+htmlPage.length()+"\n"+
                    		"Content-Type: text/html\n"+
                    		"Connection: Closed\n\n"+
                    		htmlPage
    						); 
                    break; 
                } 
            } 
        }
        finally
        {
            // Clean up
            System.out.println("Cleaning up connection: " + client);
            outbound.close(); 
            inbound.close(); 
            client.close();  
        }
    }
}