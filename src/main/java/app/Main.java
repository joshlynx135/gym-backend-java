package app;

import app.domain.GymService;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static final GymService gymService = new GymService();

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // route incoming url to specific handler
        server.createContext("/api/members", new  MemberHandler());
        server.createContext("/api/classes", new ClassHandler());
        
        server.start();
        System.out.println("Java Gym API is running on port " + port);
    }
}