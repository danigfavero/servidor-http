import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer extends Thread {

    private Socket client;
    private DataOutputStream resp;
    private BufferedReader req;

    public MyServer(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
        	
        	InputStream is = client.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            req = new BufferedReader(isr);
            
            response();

        } catch (IOException e) {	
            e.printStackTrace();     
        }
    }

    private void response() throws IOException {
        resp = new DataOutputStream(client.getOutputStream());
        String requestString = req.readLine();
        if (requestString.contains("HTTP/1.1") && requestString.contains("/oi")) {
        	String requestArray[] = requestString.split(" ");
            oi(requestArray[1]);
        } else {
            notFound();
        }
        resp.close();
    }

    private void oi(String path) throws IOException {
        String responseString = "HTTP/1.1 200 OK";
        resp.writeBytes(responseString);
        resp.writeBytes("\r\n\r\n");
        
        String parameters[] = path.split("nome=");
        String nome;
        if (parameters.length > 1) {
        	nome = ", " + parameters[1];
        } else {
        	nome = "";
        }
        resp.writeBytes("Oi" + nome + "!\n");
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
