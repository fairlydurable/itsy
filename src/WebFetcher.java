package utility.webfetcher;

import java.io.IOException;
import java.net.*;
import java.net.http.*;
import org.json.*;

public class WebFetcher {
    /**
     * Fetch data from a URL string, returning a JSON object
     *
     * @param urlString The URL to fetch data from.
     * @return The fetched data as a JSONObject.
     * @throws IOException If an error occurs while fetching data.
     */
    public static JSONObject fetchDataFromURLString(String urlString) throws IOException {
        try {
            URI uri = new URI(urlString);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                throw new IOException("HTTP connection error. Code: " + response.statusCode());
            }
            return new JSONObject(response.body());
            
        } catch (URISyntaxException | InterruptedException e) {
            throw new IOException("Error fetching data from URL: " + e.getMessage());
        }
    }
}
