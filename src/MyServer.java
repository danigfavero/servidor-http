import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer extends Thread {
	
	private Socket client;
	private BufferedReader req;
    private DataOutputStream resp;
    
    public MyServer(Socket client) {
    	this.client = client;
    }
    
    public void run() {
    	try {
	    	String requestString = request();
	    	response(requestString);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private String request() throws IOException {
    	InputStream is = client.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		req = new BufferedReader(isr);
    	return req.readLine(); 
    }
    
    private void response(String requestString) throws IOException {
    	resp = new DataOutputStream(client.getOutputStream());
    	if (requestString.contains("HTTP/1.1") && requestString.contains("/oi")) {
    		oi();
    	} else {
    		notFound();
    	}
		resp.close();
    }
    
    private void oi() throws IOException {
    	String responseString = "HTTP/1.1 200 OK";
		resp.writeBytes(responseString);
		resp.writeBytes("\r\n\r\n");
    	resp.writeBytes("Oi!\n");
    }
    
    private void notFound() throws IOException {
    	String responseString = "HTTP/1.1 404 Not Found";
		resp.writeBytes(responseString);
		resp.writeBytes("\r\n\r\n");
    	resp.writeBytes("Não encontrado\n");
    }

	public static void main(String[] args) throws IOException {	
		try (ServerSocket server = new ServerSocket(8080)) {			
			while (true) {
				Socket client = server.accept();			
				MyServer myServer = new MyServer(client);
				myServer.start();		
			}
		}
	}
	
}
