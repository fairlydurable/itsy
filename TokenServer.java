package token;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.util.logging.*;
import java.util.concurrent.*;
import java.util.Random;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.net.*;

public class TokenServer {
    private static final TokenStore tokenStore = new TokenStore();
    private static final Logger logger = Logger.getLogger(TokenServer.class.getName());
    private static Lock lock = new ReentrantLock();
    
    
    public static class QueryStringParser {
        public static Map<String, String> parseQueryString(String queryString) {
            Map<String, String> queryParams = new HashMap<>();
            
            if (queryString != null) {
                String[] pairs = queryString.split("&");
                for (String pair : pairs) {
                    try {
                        int idx = pair.indexOf("=");
                        String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                        String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                        
                        queryParams.put(key, value);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            return queryParams;
        }
    }
    
    public static void main(String[] args) throws IOException {
        int    port = 8409;
        String localhost = "127.0.0.1";
        String dropletHost = "146.190.45.234";
        String domainHost = "iq.temporal.io";
        String host = localhost;
        
        // Create server and set endpoints
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/", new IndexHandler());
        server.setExecutor(null);
        
        System.out.printf("Server running on port %d...\n", port);
        server.start();
    }
    
    // The site home page is mostly an overview of the service with how-to
    static class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            logger.setLevel(Level.INFO);
            String remoteAddress = exchange.getRemoteAddress() != null ? exchange.getRemoteAddress().toString() : "Unknown";
            logger.info("Connection received from: " + remoteAddress);
            
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            
            Map<String, String> queries = new HashMap<>();
            if (query != null && !query.isEmpty()) {
                queries = QueryStringParser.parseQueryString(query);
            }
            String keyValue = queries.get("key");
            String idValue = queries.get("id");
            String response = "unhandled";
            
            if (! "GET".equals(method)) {
                response = "Unsupported method";
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, response.length());
            }
            
            if ("/".equals(path) || "/index".equals(path)) {
                response = mainPageResponse;
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
            }
            
            int statusValue = HttpURLConnection.HTTP_OK;

            // Start Activity
            if ("/lock".equals(path)) {
                if (idValue == null) {
                    response = "Misconfigured request";
                    statusValue = HttpURLConnection.HTTP_BAD_REQUEST;
                } else {
                    lock.lock();
                    try {
                        
                        String status = tokenStore.get(idValue);

                        // No Lock, no Key
                        if (status == null) {
                            // Unlocked. Lock it.
                            String lockKey = UUID.randomUUID().toString();
                            tokenStore.set(idValue, "locked");
                            tokenStore.set(idValue + "_key", lockKey);
                            response = lockKey;
                            statusValue = HttpURLConnection.HTTP_OK;
                        } else {
                            switch (status) {
                                case "complete":
                                    // Already completed
                                    response = status;
                                    statusValue = HttpURLConnection.HTTP_CONFLICT;
                                    break;
                                case "locked":
                                    // Already locked
                                    response = status;
                                    statusValue = HttpURLConnection.HTTP_CONFLICT;
                                    break;
                                default:
                                    response = "Misconfigured request";
                                    statusValue = HttpURLConnection.HTTP_BAD_REQUEST;
                                    break;
                            }
                        }

                    } finally { lock.unlock(); }
                }
                exchange.sendResponseHeaders(statusValue, response.length());
            }
            
            // Unlock on Failed Activity
            if ("/unlock".equals(path)) {
                if (idValue == null || keyValue == null ) {
                    response = "Misconfigured request";
                    statusValue = HttpURLConnection.HTTP_BAD_REQUEST;
                } else {
                    lock.lock();
                    try {
                        String status = tokenStore.get(idValue);
                        response = status;
                        switch (status) {
                            case "complete":
                                // Already completed
                                response = status;
                                statusValue = HttpURLConnection.HTTP_CONFLICT;
                                break;
                            case "locked":
                                String storedKey = tokenStore.get(idValue + "_key");

                                // Incorrect Key
                                if (!storedKey.equals(keyValue)) {
                                    // Cannot unlock
                                    response = "Unauthorized request";
                                    statusValue = HttpURLConnection.HTTP_UNAUTHORIZED;
                                } else {
                                    // Unlock and remove the key
                                    response="unlocked";
                                    tokenStore.delete(idValue);
                                    tokenStore.delete(idValue + "_key");
                                    statusValue = HttpURLConnection.HTTP_OK;
                                }
                                break;
                                
                            default: // Either 'null' or misconfigured.
                                response = "Misconfigured request";
                                statusValue = HttpURLConnection.HTTP_BAD_REQUEST;
                                break;
                        }
                    } finally { lock.unlock(); }
                }
                exchange.sendResponseHeaders(statusValue, response.length());
            }
            
            // Completed Activity
            if ("/complete".equals(path)) {
                if (idValue == null || keyValue == null ) {
                    response = "Misconfigured request";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
                } else {
                    lock.lock();
                    try {
                        String status = tokenStore.get(idValue);
                        response = status;
                        switch (status) {
                            case "complete":
                                // Already Complete
                                response = status;
                                statusValue = HttpURLConnection.HTTP_CONFLICT;
                                break;
                                
                            case "locked":
                                // Attempting to complete locked activity
                                String completeKey = tokenStore.get(idValue + "_key");
                                
                                // Incorrect Key
                                if (!completeKey.equals(keyValue)) {
                                    // Cannot unlock
                                    response = "Unauthorized request";
                                    statusValue= HttpURLConnection.HTTP_UNAUTHORIZED;
                                } else {
                                    // Complete and remove the key
                                    tokenStore.set(idValue, "complete");
                                    tokenStore.delete(idValue + "_key");
                                    response = "complete";
                                    statusValue = HttpURLConnection.HTTP_OK;
                                }
                                break;
                                
                            default :
                                response = "Misconfigured request";
                                statusValue = HttpURLConnection.HTTP_BAD_REQUEST;
                                break;
                        }
                    } finally { lock.unlock(); }
                }
                exchange.sendResponseHeaders(statusValue, response.length());
            }
            
            if ("unhandled".equals(response)) {
                response = "Invalid request";
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, response.length());
            }
            
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
        
        String trafficArt = ""
        + "                       ##\n"
        + "                      _[]_\n"
        + "                     [____]\n"
        + "                 .----'  '----.\n"
        + "             .===|    <font color=\"#ff5f5f\">.==.</font>    |===.\n" // Adjusted shade of red
        + "             \\   |   <font color=\"#ff5f5f\">/####\\</font>   |   /\n" // Adjusted shade of red
        + "             /   |   <font color=\"#ff5f5f\">\\####/</font>   |   \\\n" // Adjusted shade of red
        + "             '===|    <font color=\"#ff5f5f\">`\"\"`</font>    |==='\n" // Adjusted shade of red
        + "             .===|    <font color=\"orange\">.==.</font>    |===.\n"
        + "             \\   |   <font color=\"orange\">/::::\\</font>   |   /\n"
        + "             /   |   <font color=\"orange\">\\::::/</font>   |   \\\n"
        + "             '===|    <font color=\"orange\">`\"\"`</font>    |==='\n"
        + "             .===|    <font color=\"#59cc59\">.==.</font>    |===.\n" // Adjusted shade of green
        + "             \\   |   <font color=\"#59cc59\">/&&&&\\</font>   |   /\n" // Adjusted shade of green
        + "             /   |   <font color=\"#59cc59\">\\&&&&/</font>   |   \\\n" // Adjusted shade of green
        + "             '===|    <font color=\"#59cc59\">`\"\"`</font>    |==='\n" // Adjusted shade of green
        + "                 '--.______.--'\n"
        + "" ;
        
        String mainPageResponse = ""
        + "<!DOCTYPE html>"
        + "<html lang=\"en\">"
        + "<head>"
        + "<meta charset=\"UTF-8\">"
        + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
        + "<title>Token Server</title>"
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
        + "  padding: 20px; "
        + "  text-align: center;"
        + "}"
        + ".ascii-container {"
        + "  color:343a40;"
        + "  display: flex;"
        + "  justify-content: center;"
        + "  padding-top: 20px; padding-left: 100px; padding-right: 100px; "
        + "}"
        + "pre {"
        + "  font-size: 14px;"
        + "  white-space: pre-wrap;"
        + "  color: #343a40;"
        + "}"
        + "</style>"
        + "</head>"
        + "<body>"
        + "<header>"
        + "<h1>Welcome to the Token Server</h1>"
        + "</header>"
        + "<div class=\"ascii-container\">"
        + "<pre>"
        + trafficArt
        + "</pre>"
        + "</div>"
        + "<div style=\"padding-left: 100px; padding-right: 100px;\">"
        + "<h2>Token Server: There can be only one</h2>"
        + "<p>This server's tokens support run-once execution. You begin by locking the activity with a unique identifier. You receive a key that enables your activity to complete or (in the case of error) reset. These endpoints are easily integrated into your application's workflow to provide a simple approach for run-once execution. Locks ensure you don't end up ordering a dozen pizzas or sending a hundred emails when you meant for your process to be run only once.</p>"
        + "<h3>Endpoints:</h3>"
        + "<ul>"
        + "<li><strong>`GET /lock?id=lock_id`</strong> - Locks a new activity and returns an API key that enables 'unlock' and 'complete' for this process_id.</li>"
        + "<li><strong>`GET /unlock?id=lock_id&key=generated_key`</strong> - Unlocks a failed activity using the provided key and lock id.</li>"
        + "<li><strong>`GET /complete?id=lock_id&key=generated_key`</strong> - Marks a successful activity as complete. Subsequent calls to this endpoint are not allowed.</li>"
        + "</ul>"
        + "<h3>Explanation and Examples:</h3>"
        + "<h4>Lock a New Activity</h4>"
        + "<p>This endpoint locks a new activity and returns a key.</p>"
        + "<pre>GET /lock?id=lock_id</pre>"
        + "<p>For example</p>"
        + "<pre>"
        + "$ curl http://localhost:8409/lock?id=my_process\n"
        + "abc123\n"
        + "$\n"
        + "</pre>"
        + "<p>This call fails when the lock identifier is already in use or has been completed.</p>"
        + "<h4>Unlock a Failed Activity</h4>"
        + "<p>This endpoint unlocks a failed activity using the provided key and lock id.</p>"
        + "<pre>GET /unlock?id=lock_id&key=generated_key</pre>"
        + "<p>This call fails when the lock identifier is unknown, when the key is incorrect, or the activity been completed.</p>"
        + "<h4>Complete a Successful Activity</h4>"
        + "<p>This endpoint marks a successful activity as complete. Subsequent calls to update a completed lock are not allowed. The call also fails when the lock_id or key aren't recognized.</p>"
        + "<pre>GET /complete?id=lock_id&key=generated_key</pre>"
        + "<h2>Overview</h2>"
        + "<pre>"
        + "&#9484;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9488; Try to   &#9484;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9488;<br>"
        + "&#9474;       &#9474;   Lock   &#9474; already &#9474;<br>"
        + "&#9474; Start &#9500;&#9472;&#9472;&#9516;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9508; locked  &#9500;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#x25BA;FAIL<br>"
        + "&#9474;       &#9474;  &#9474;       &#9500;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9508;<br>"
        + "&#9492;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9496;  &#9474;       &#9500;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9508;<br>"
        + "           &#9474;       &#9474;already  &#9500;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#x25BA;FAIL<br>"
        + "&#9484;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9488;  &#9474;       &#9474;complete &#9474;         &#x25B2; &#x25B2;<br>"
        + "&#9474;       &#9474;  &#9474;       &#9492;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9496;         &#9474; &#9474;<br>"
        + "&#9474;locked &#x25C4;&#9472;&#9472;&#9496;                           &#9474; &#9474;<br>"
        + "&#9474;       &#9474;                              &#9474; &#9474;<br>"
        + "&#9474; (key) &#9474;                              &#9474; &#9474;<br>"
        + "&#9474;       &#9500;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9488;               &#9474; &#9474;<br>"
        + "&#9492;&#9472;&#9472;&#9472;&#9516;&#9472;&#9472;&#9472;&#9496;              &#9474;               &#9474; &#9474;<br>"
        + "    &#9474;                  &#9474;               &#9474; &#9474;<br>"
        + "    &#9474; Activity         &#9474; Activity      &#9474; &#9474;<br>"
        + "    &#9474; Failure          &#9474; Completes     &#9474; &#9474;<br>"
        + "&#9484;&#9472;&#9472;&#9472;&#x25BC;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9488;      &#9484;&#9472;&#9472;&#9472;&#x25BC;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9488;        &#9474; &#9474;<br>"
        + "&#9474;           &#9474;      &#9474;          &#9474;        &#9474; &#9474;<br>"
        + "&#9474; Unlock    &#9474;      &#9474; Complete &#9500;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9496; &#9474;<br>"
        + "&#9474; with key  &#9474;      &#9474; with key &#9474;  Lock or &#9474;<br>"
        + "&#9474;           &#9474;      &#9474;          &#9474;  Complete&#9474;<br>"
        + "&#9492;&#9472;&#9472;&#9472;&#9472;&#9516;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9496;      &#9492;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9496;          &#9474;<br>"
        + "     &#9474;                                   &#9474;<br>"
        + "     &#9474;      Lock or Complete             &#9474;<br>"
        + "     &#9492;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9496;<br>"
        + "</pre>"
        + "</div>"
        + "</body>"
        + "</html>";
    }
}
