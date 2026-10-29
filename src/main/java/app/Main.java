package app;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    
    public static void main(String[] args) throws IOException {
        int port = 8080;
        
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        server.createContext("/ping", new PingHandler());
        
        server.start();
        System.out.println("Java Gym API is running on http://localhost:" + port);
    }


    static class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{\"message\": \"Java Server is Alive!\"}";
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close(); 
        }
    }
}