package io.temporal.learning;

import java.io.*;
import java.time.Duration;
import io.temporal.activity.*;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.*;
import java.net.http.*;

@ActivityInterface
public interface TutorialActivities {
    // Vended activities.
    public void changeMantra();
    
    public ActivityOptions quickActivityOptions = ActivityOptions
        .newBuilder()
        .setStartToCloseTimeout(Duration.ofSeconds(2)) // Max Activity execution time only
        .setScheduleToCloseTimeout(Duration.ofSeconds(5)) // Entire duration from scheduling to completion, including queue time.
        .build();
    
    // Implement the vended activities, normally externally
    public static class TutorialActivitiesImpl implements TutorialActivities {
        @Override
        public void changeMantra() {
            String mantra = Mantra.getRandomMantra();
            try {
                String encodedString = URLEncoder.encode(mantra, "UTF-8");
                // URI uri = new URI("http://146.190.45.234:8888/mantra?content=" + encodedString);
                URI uri = new URI("http://127.0.0.1:8080/mantra?update=" + encodedString);
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    throw new IOException("HTTP connection error. Code: " + response.statusCode());
                }
            } catch (UnsupportedEncodingException e) {
                throw Activity.wrap(e);
            } catch (URISyntaxException | InterruptedException e) {
                throw Activity.wrap(new IOException("Error fetching data from URL: " + e.getMessage()));
            } catch (IOException e) {
                throw Activity.wrap(e);
            }
        }
    }
}
