package io.temporal.learning;

import java.io.*;
import java.time.Duration;
import io.temporal.activity.*;
import io.temporal.weather.*;

@ActivityInterface
public interface ForecastActivities {
    public String[] fetchGeolocation(String input);
    public String   fetchForecast(String[] input);

    public ActivityOptions quickActivityOptions = ActivityOptions.newBuilder()
    .setStartToCloseTimeout(Duration.ofSeconds(2)) // Max Activity execution time only
    .setScheduleToCloseTimeout(Duration.ofSeconds(60)) // Entire duration from scheduling to completion, including queue time.
    .build();

    public static class ForecastActivitiesImpl implements ForecastActivities {
        @Override
        public String[] fetchGeolocation(String input) {
            try { 
                return GeolocationFetcher.fetchApproximateGeolocation(input); 
            } catch (IOException exception) {
                throw Activity.wrap(exception);
            }
        }
        
        @Override
        public String fetchForecast(String[] input) {
            try {
                return WeatherFetcher.fetchForecast(input);
            } catch (IOException exception) {
                throw Activity.wrap(exception);
            }
        }
    }
}
