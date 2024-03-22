package unreliable;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Random;

import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

public class MantraServer {
    private static final Logger logger = Logger.getLogger(MantraServer.class.getName());
    
    static String localhost = "127.0.0.1";
    static String dropletHost = "146.190.45.234";
    
    public static void main(String[] args) throws IOException {
        int port = 8888;
        String host = localhost;
        
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/", new IndexHandler());
        server.createContext("/mantra", new MantraHandler());
        server.createContext("/mantra.json", exchange -> {
            String response = "{\"mantra\": \"" + IndexHandler.mantra.replace("\"", "\\\"") + "\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });
        server.setExecutor(null);
        
        System.out.printf("Server running on port %d...\n", port);
        server.start();
    }

    static String asciiTreeArt = ""
    + "       %%%,%%%%%%%\n"
    + "        ,'%% \\\\-*%%%%%%%\n"
    + "  ;%%%%%*%   _%%%%\n"
    + "   ,%%%       \\(_.*%%%%.\n"
    + "   % *%%, ,%%%%*(    '\n"
    + " %^     ,*%%% )\\|,%%*%,_\n"
    + "      *%    \\/ #).-*%%*\n"
    + "          _.) ,/ *%,\n"
    + "  _________/)#(_____________\n"
    + " |__________________________|\n";
    
    static class IndexHandler implements HttpHandler {
        public static String mantra = "Be one. Be all.";
        
        public static String indexContent() {
            String response = ""
            + "<!DOCTYPE html>"
            + "<html lang=\"en\">"
            + "<head>"
            + "<meta charset=\"UTF-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
            + "<title>Mantra Server</title>"
            + "<style>"
            + "body {"
            + "  font-family: Arial, sans-serif;"
            + "  background-color: #f8f9fa;"
            + "  margin: 0;"
            + "  padding: 0;"
            + "}"
            + "header {"
            + "  background-color: #343a40;"
            + "  color: #ffffff;"
            + "  padding: 20px;"
            + "  text-align: center;"
            + "}"
            + ".mantra-container {"
            + "  text-align: center;"
            + "}"
            + ".ascii-container {"
            + "  display: flex;"
            + "  justify-content: center;"
            + "  padding-top: 20px;"
            + "}"
            + "pre {"
            + "  font-size: 24px;"
            + "  white-space: pre-wrap;"
            + "  color: #343a40;"
            + "}"
            + "</style>"
            + "</head>"
            + "<body>"
            + "<header>"
            + "<h1>Welcome to the Mantra Server</h1>"
            + "</header>"
            + "<div class=\"ascii-container\">"
            + "<pre>"
            + asciiTreeArt
            + "</pre>"
            + "</div>"
            + "<div style=\"padding-left: 100px; padding-right: 100px;\">"
            + "<div class=\"mantra-container\">"
            + "<h2 id='mantra'>"
            + mantra
            + "</h2>"
            + "</div>"
            + "</div>"
            + "<script>"
            + "setInterval(function() {"
            + "  fetch('/mantra.json')"
            + "    .then(function(response) { return response.json(); })"
            + "    .then(function(data) {"
            + "      var currentMantra = document.getElementById('mantra').innerText;"
            + "      if (data.mantra !== currentMantra) {"
            + "        document.getElementById('mantra').innerText = data.mantra;"
            + "      }"
            + "    });"
            + "}, 1000);" // Check every 1000 milliseconds (1 second)
            + "</script>"
            + "</body>"
            + "</html>";
            return response;
        }
    
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            logger.setLevel(Level.INFO);
            String remoteAddress = exchange.getRemoteAddress() != null ?
            exchange.getRemoteAddress().toString() : "Unknown";
            logger.info("Connection attempt received from: " + remoteAddress);
            
            String response = IndexHandler.indexContent();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class MantraHandler implements HttpHandler {
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String response = "uninitialized";

            if (query != null && query.contains("content=")) {
                try {
                    response = "OK";
                    String encodedString = query.split("content=")[1];
                    String decodedString = URLDecoder.decode(encodedString, "UTF-8");
                    IndexHandler.mantra = decodedString;
                    exchange.sendResponseHeaders(200, response.getBytes().length);
//                    exchange.getResponseHeaders().set("Location", "/");
//                    exchange.sendResponseHeaders(302, -1);
                } catch (UnsupportedEncodingException e) {
                    response = "Unsupported encoding: " + e.getMessage();
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                } catch (NumberFormatException e) {
                    response = "Unable to set new content: " + e.getMessage();
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                }
            } else {
                response = "Bad Request";
                exchange.sendResponseHeaders(400, response.getBytes().length);
            }
            
//            if (!response.equals("OK")) { // Send the error response only if not OK
//            }
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
