package io.temporal.learning;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.PrintStream;

import java.net.InetSocketAddress;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Random;


public class ForecastServerApp {
    static {
        // Enable print statements
        System.setOut(new PrintStream(new BufferedOutputStream(System.out), true));
        System.setErr(new PrintStream(new BufferedOutputStream(System.err), true));
    }

    private static final Logger logger =
        Logger.getLogger(ForecastServerApp.class.getName());
    
    static String localhost = "127.0.0.1";
    static String dropletHost = "146.190.45.234";
    
    public static void main(String[] args) throws IOException {
        // Where to serve
        int    port = 8080;
        String host = dropletHost;
                
        HttpServer server =
            HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/", new IndexHandler());
        server.createContext("/fetchForecast", new ForecastHandler());
        server.setExecutor(null); // Creates a default executor
        
        System.out.printf("Server running on port %d...\n", port);
        server.start();
    }
    
    static class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Index Handler");
            String remoteAddress = exchange.getRemoteAddress() != null ?
            exchange.getRemoteAddress().getAddress().getHostAddress() : "Unknown";
            String[] parts = remoteAddress.split(":");
            String ipaddress = parts[0];
            logger.info("Connection attempt received from: " + ipaddress);
            
            String response;
            // Check if IP address is local or not
            if (ipaddress.equals("127.0.0.1") || 
                ipaddress.startsWith("10.") ||
                ipaddress.startsWith("172.") ||
                ipaddress.startsWith("192.")) {
                // IP address is local, show alternative message
                response = generateLocalNetworkResponse();
            } else {
                // IP address is not local, fetch forecast
                response = generateForecastResponse(ipaddress);
            }

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
        private String generateLocalNetworkResponse() {
            return ""
            + "<html><head><title>Forecast Server</title>"
            + "<style>" + css() + "</style>"
            + "</head>"
            + "<body>"
            + "<h1>Welcome to the Forecast Server</h1>"
            + "<p>Hello visitor from the Local Area Network! Fetching your forecast...</p>"
            + "<div id=\"forecast\">The weather inside your room is balmy today, with no expectations of inclement conditions. Remember: drive carefully when inside buildings!</div>"
            + "<div id=\"quote\">" + getRandomQuote() + "</div>"
            + "</body></html>";
        }
        
        private String generateForecastResponse(String ipaddress) {
            return ""
            + "<html><head><title>Forecast Server</title>"
            + "<style>" + css() + "</style>"
            + "<script>"
            + "function fetchForecast() {"
            + "  var xhr = new XMLHttpRequest();"
            + "  var timeout = setTimeout(function() {"
            + "    xhr.abort();"
            + "    document.getElementById('forecast').innerHTML = 'Sorry, unable to load the weather at this time. This service only supports locations within the United States (although it can sometimes work elsewhere) and it may fail for some US locations as well.';"
            + "  }, 10000);" // 10 seconds timeout
            + "  xhr.open('GET', '/fetchForecast', true);"
            + "  xhr.onload = function() {"
            + "    clearTimeout(timeout);"
            + "    if (xhr.status === 200) {"
            + "      document.getElementById('forecast').innerHTML = xhr.responseText;"
            + "    } else {"
            + "      console.error('Error fetching forecast: ' + xhr.status);"
            + "    }"
            + "  };"
            + "  xhr.send();"
            + "}"
            + "window.onload = fetchForecast;"
            + "</script>"
            + "</head>"
            + "<body>"
            + "<h1>Welcome to the Forecast Server</h1>"
            + "<p>Hello visitor from " + ipaddress + "! Fetching your forecast...</p>"
            + "<div id=\"forecast\"></div>"
            + "<div id=\"quote\">" + getRandomQuote() + "</div>"
            + "</body></html>";
        }
        
        public static String css() {
            // Hardcoded CSS styling
            return ""
            + "body {"
            + "    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;"
            + "    background-color: #ffffff; /* White background */"
            + "    margin: 0;"
            + "    padding: 0;"
            + "}"
            + "h1 {"
            + "    color: #333;"
            + "    text-align: center;"
            + "    margin-top: 40px;"
            + "}"
            + "p {"
            + "    color: #666;"
            + "    text-align: center;"
            + "}"
            + "#forecast {"
            + "    padding: 20px;"
            + "    border-radius: 10px;"
            + "    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);"
            + "    background-color: #f9f9f9; /* Light gray background */"
            + "    margin: 0 auto;"
            + "    width: 80%;"
            + "    max-width: 600px;"
            + "    margin-top: 40px;"
            + "    border: 2px solid #4CAF50; /* Pop of color */"
            + "}"
            + "#quote {"
            + "    margin-top: 20px;"
            + "    text-align: center;"
            + "    font-style: italic;"
            + "    color: #777;"
            + "}";
        }

        private static final String[] quotes = {
        "Change is the only constant in life. - Heraclitus",
        "The pessimist complains about the wind; the optimist expects it to change; the realist adjusts the sails. - William Arthur Ward",
        "Life is like the weather, constantly changing. Embrace the storms and enjoy the sunshine. - Unknown",
        "The only way to make sense out of change is to plunge into it, move with it, and join the dance. - Alan Watts",
        "Just as the wind carries away clouds, so change clears the way for new opportunities. - Unknown",
        "Change your thoughts and you change your world. - Norman Vincent Peale",
        "Life is like the weather. Sometimes it's sunny, sometimes it's cloudy, but it always changes. - Unknown",
        "The winds of change blow us towards new beginnings. - Unknown",
        "Change brings opportunity. - Nido Qubein",
        "Life is about not knowing, having to change, taking the moment and making the best of it, without knowing what's going to happen next. - Gilda Radner"
        };
        
        public static String getRandomQuote() {
            Random random = new Random(System.nanoTime());
            int index = random.nextInt(quotes.length);
            return quotes[index];
        }

    }
    
        static class ForecastHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Forecast Handler");
            String remoteAddress = exchange.getRemoteAddress() != null ?
            exchange.getRemoteAddress().getAddress().getHostAddress() : "Unknown";
            logger.info("Connection attempt received from: " + remoteAddress);
            
            String[] parts = remoteAddress.split(":");
            String ipaddress = parts[0];
            String forecast = ForecastFetcher.runForecastWorker(ipaddress);
            String forecastString = ""
            + "<html><head><title>Your Personal Forecast</title>"
            + "<body>"
            + "<p>Your forecast for today:</p>"
            + "<p>" + forecast + "</p>"
            + "</body>";
            exchange.sendResponseHeaders(200, forecastString.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(forecastString.getBytes());
            os.close();
        }
    }
}
