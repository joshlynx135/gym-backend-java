package app;
import app.domain.Member;
import tools.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class MemberHandler implements HttpHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override 
    public void handle(HttpExchange exchange) throws IOException{
        String method = exchange.getRequestMethod();

        try {
            if(method.equals("POST")){
                //read json body
                Map<String, String> body = mapper.readValue(exchange.getRequestBody(), Map.class);

                Member newMember = Main.gymService.createMember(body.get("name"), body.get("type"));
                sendJson(exchange, 201,newMember); 
            }
            else {
                sendJson(exchange, 405, Map.of("error", "Method Not Allowed"));
            }
        } catch (Exception e) {
            sendJson(exchange, 400, Map.of("error", e.getMessage()));
        }
    }

    private void sendJson(HttpExchange exchange, int statusCode, Object responseObj) throws IOException {
        String jsonResponse = mapper.writeValueAsString(responseObj);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }
}
