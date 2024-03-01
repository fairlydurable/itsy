package io.temporal.weather;
import utility.webfetcher.WebFetcher;
import java.io.*;
import java.util.regex.Pattern;
import org.json.JSONObject;

/**
 * Fetch geolocation data from IP address.
 *
 * <p>Validates an IPv4 address, fetches its geolocation data
 * from the ipapi API, and prints the latitude and longitude coordinates
 * to the console.
 *
 * @see <a href="https://ipapi.co/">ipapi.co</a>
 */
public class GeolocationFetcher {
    
    /**
     * Return the approximate geolocation data of an IP address.
     *
     * @param ipAddress the IP address for which geolocation data is to be fetched
     * @return a String array containing the latitude and longitude as strings
     * @throws IOException if an I/O error occurs while fetching the geolocation data
     */
    public static String[] fetchApproximateGeolocation(String ipAddress) throws IOException {
        try {
            JSONObject geolocationData = fetchGeolocationData(ipAddress);
            return new String[]{
                Double.toString(geolocationData.getDouble("latitude")),
                Double.toString(geolocationData.getDouble("longitude"))
            };
        } catch (IOException exception) {
            throw exception;
        }
    }
    
    /**
     * Returns Boolean indicating whether this string is a likely IP address.
     *
     * @apiNote A rough approximation. Not meant for production use.
     *
     * @param ipAddress The string-based IP address to validate
     * @return A Boolean. True if the IP address is likely valid, false otherwise
     */
    private static boolean isValidIPAddress(String ipAddress) {
        // Regular expression to match IPv4 address format
        String ipRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return Pattern.matches(ipRegex, ipAddress);
    }
    
    /**
     * Return JSONObject geolocation data for an IP address string
     *
     * @param ipAddress A string-based IP address
     * @return A JSONObject containing Geolocation data for the IP address
     * @throws IOException If an error occurs while fetching geolocation
     */
    private static JSONObject fetchGeolocationData(String ipAddress) throws IOException {
        // See: https://ipapi.co/
        String geolocationEndpointString = "https://ipapi.co/" + ipAddress + "/json/";
        return WebFetcher.fetchDataFromURLString(geolocationEndpointString);
    }
}
