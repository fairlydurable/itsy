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

public class CoffeeServer {
    private static final Logger logger =
        Logger.getLogger(CoffeeServer.class.getName());
    
    static String localhost = "127.0.0.1";
    static String dropletHost = "146.190.45.234";

    public static void usage() {
        System.out.println(""
                           + "Arguments (in order):\n"
                           + "    [reliability (default: 0.2)]\n"
                           + "    [port (default 8080)]\n"
                           + "    [host_type (default localhost)]");
        System.out.println("    Available hosts: local, droplet");
        System.exit(0);
    }
    
    public static void main(String[] args) throws IOException {
        double reliability = 0.5;
        int    port = 8418;
        String host = localhost; //dropletHost;
        
        // Command-line invocation, mostly for debugging
        if (args.length > 0 && args[0].equals("help"))
          { usage(); System.exit(0); }
        if (args.length > 0) 
          { reliability = Double.parseDouble(args[0]); }
        if (args.length > 1) 
          { port = Integer.parseInt(args[1]); }
        if (args.length > 2 && args[2].equals("dropletHost")) 
          { host = dropletHost; }
        else if (args.length > 2) { host = args[2]; }
        
        // Create server and set endpoints
        HttpServer server =
            HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/", new IndexHandler());
        server.createContext("/coffee", new CoffeeHandler());
        server.setExecutor(null);
        
        System.out.printf("Server running on port %d...\n", port);
        server.start();
    }
    
    static String asciiCoffeeArt = ""
    + ""
    + ""
    + ""
    + ""
    + "  ( ( ( \n"
    + "   ) ) ) \n"
    + "  ........_\n"
    + "  |      |_)\n"
    + "  \\      /\n"
    + "   `----'\n";
    
    // The site home page is mostly an overview of the service with how-to
    static class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            logger.setLevel(Level.INFO);
            String remoteAddress = exchange.getRemoteAddress() != null ?
            exchange.getRemoteAddress().toString() : "Unknown";
            logger.info("Connection attempt received from: " + remoteAddress);
            
            String response = ""
            + "<!DOCTYPE html>"
            + "<html lang=\"en\">"
            + "<head>"
            + "<meta charset=\"UTF-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
            + "<title>Coffee Server</title>"
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
            + "<h1>Welcome to the Coffee Server</h1>"
            + "</header>"
            + "<div class=\"ascii-container\">"
            + "<pre>"
            + asciiCoffeeArt
            + "</pre>"
            + "</div>"
            + "<div style=\"padding-left: 100px; padding-right: 100px;\">"
            + "<h2>\"Coffee Server\" Learning Endpoint: Unreliable-by-Design</h2>"
            + "<p>If everything in the world worked predictably, there'd be no need for Temporal. Instead, handling unreliable services is central to real-world software development. Network issues, service outages, and other circumstances regularly interrupt or delay  business processes.</p><p>This microserve provides a simulated coffee server. It offers controlled end-point reliability with a failure rate that you choose. Use it to explore and experiment as you explore building resilient systems.</p>"
            + "<h2>Tutorial Endpoint:</h2>"
            + "<p>Gain practical experience with this learning-focused endpoint. Use this endpoint to:</p>"
            + "<ul>"
            + "<li>Test and validate against controlled levels of service reliability.</li>"
            + "<li>Implement retry and error handling strategies.</li>"
            + "<li>Mitigate service failures impact.</li>"
            + "<li>Design with reliable fault-tolerant durable execution.</li>"
            + "<li>Explore concepts of resilience like circuit breakers, timeouts, and exponential backoff.</li>"
            + "</ul>"
            + "<h2>Use Cases:</h2>"
            + "<ul>"
            + "<li>Incorporate this endpoint into your Temporal learning process.</li>"
            + "<li>Use an endpoint like this during your prototyping phase.</li><li>Experiment with controlled resilience patterns when validating designs.</li>"
            + "</ul>"
            + "<h2>Examples:</h2>"
            + "<div style=\"padding-left: 20px;\">"
            + "<p>Request a cup of coffee with default reliability (50% success).</p>"
            + "<pre style=\"font-family: monospace; font-size: inherit;\">GET /coffee</pre>"
            + "<p>Request a cup of coffee with increased reliability (80% success).</p>"
            + "<pre style=\"font-family: monospace; font-size: inherit;\">GET /coffee?reliability=0.8</pre>"
            + "<p>Request a cup of coffee with low reliability (20% success).</p>"
            + "<pre style=\"font-family: monospace; font-size: inherit;\">GET /coffee?reliability=0.2</pre>"
            + "</div>"
            + "<h2>RFC 2324 Section 2.3.2</h2>"
            + "<p>The <a href=\"https://www.rfc-editor.org/rfc/rfc2324#section-2.3.2\">418 \"I'm a teapot\"</a>  code originates from a 1998 April Fool's joke, the \"Hyper Text Coffee Pot Control Protocol (HTCPCP/1.0) Standards RFC\". Error 418 is a much beloved, warm, and humorous Internet Easter Egg. Although it serves no practical purpose, it has been more <a href=\"https://www.google.com/teapot\">widely adopted</a> than you might expect.</p>"
            + "</div>"
            + "</body>"
            + "</html>";


            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    static class CoffeeHandler implements HttpHandler {
        private double reliability;
        
        public CoffeeHandler() {
            this.reliability = 0.5;
        }
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String reliabilityParam = exchange.getRequestURI().getQuery();
            if (reliabilityParam != null && reliabilityParam.contains("reliability=")) {
                try {
                    String reliabilityValue = reliabilityParam.split("reliability=")[1];
                    this.reliability = Double.parseDouble(reliabilityValue);
                } catch (NumberFormatException e) {
                    this.reliability = 0.5;
                }
            }
            
            String responseMessage;
            int statusCode;
            
            Random rand = new Random(System.currentTimeMillis());
            double randomValue = rand.nextDouble();
            
            // Early return the exception path
            if (randomValue > reliability) {
                responseMessage = "I'm a teapot";
                statusCode = 418;
                exchange.getResponseHeaders()
                .set("Content-Type", "text/plain");
                
                byte[] responseBytes = responseMessage.getBytes();
                exchange.sendResponseHeaders(statusCode, responseBytes.length);
                
                // Allow future versions to send exception-specific content
                // that differs from the response message.
                String textResponse = responseMessage;
                byte[] textBytes = textResponse.getBytes();
                
                OutputStream os = exchange.getResponseBody();
                os.write(textBytes);
                os.close();
                
                return;
            }
            
            responseMessage = "Enjoy some fresh coffee!";
            statusCode = 200;
            
            String coffeeResponse = "{\n"
            + "  \"beverage\": \"coffee\",\n"
            + "  \"message\": \"%s\",\n"
            + "  \"asciiCoffeeArt\": \"%s\"\n"
            + "}";
            
            String jsonResponse =
            String.format(coffeeResponse, responseMessage, asciiCoffeeArt);
            byte[] jsonResponseBytes = jsonResponse.getBytes();
            
            exchange.getResponseHeaders()
            .set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, jsonResponseBytes.length);
            
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponseBytes);
            os.close();
        }
    }
}
