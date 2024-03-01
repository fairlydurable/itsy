package io.temporal.weather;
import utility.webfetcher.WebFetcher;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class retrieves weather data from the National Weather Service API.
 *
 * @version 1.0
 * @see <a href="https://www.weather.gov/documentation/services-web-api">NationalWeatherService API documentation</a>
 */
public class WeatherFetcher {
    
    /**
     * Fetch weather data using coordinates and return a forecast string.
     *
     * @param coordinates an array containing the latitude and longitude coordinates.
     * @return the forecast as a string
     * @throws IOException if an I/O error occurs while fetching the forecast data
     * @throws RuntimeException if the number of coordinates is not exactly 2
     * @see <a href="https://www.weather.gov/documentation/services-web-api">NationalWeatherService API documentation</a>
     */
    public static String fetchForecast(String[] coordinates) throws IOException {
        try {
            if (coordinates.length != 2) {
                throw new RuntimeException("Expecting 2 arguments (longitude latitude). Got " + Integer.toString(coordinates.length));
            }
            
            String latitude = coordinates[0];
            String longitude = coordinates[1];
            
            // First, extract a URL with the local forecast information
            JSONObject weatherData = fetchWeatherData(latitude, longitude);
            JSONObject weatherProperties = weatherData.getJSONObject("properties");
            String forecastURLString = weatherProperties.getString("forecast");
            
            // Then break down the forecast JSON to retrievea detailed forecast
            JSONObject forecastData = WebFetcher.fetchDataFromURLString(forecastURLString);
            JSONObject forecastProperties = forecastData.getJSONObject("properties");
            JSONArray forecastPeriods = forecastProperties.getJSONArray("periods");
            
            // Ensure there's at least one location to select
            if (forecastPeriods.length() > 0) {
                JSONObject firstForecastProperties = forecastPeriods.getJSONObject(0);
                String forecastString = firstForecastProperties.getString("detailedForecast");
                return forecastString;
            } else {
                throw new RuntimeException("Missing forecast information.");
            }
        } catch (Exception exception) {
            throw new RuntimeException("Unexpected error occurred", exception);
        }
    }
    
    /**
     * Fetch weather data from the National Weather Service API.
     *
     * @param latitude  Latitude of the location.
     * @param longitude Longitude of the location.
     * @return Weather data as a JSONObject.
     * @throws IOException If an error occurs while fetching weather data.
     * @see <a href="https://www.weather.gov/documentation/services-web-api">NationalWeatherService API documentation</a>
     */
    private static JSONObject fetchWeatherData(String latitude, String longitude) throws IOException {
        String weatherEndpointString = "https://api.weather.gov/points/" + latitude + "," + longitude;
        return WebFetcher.fetchDataFromURLString(weatherEndpointString);
    }
}


