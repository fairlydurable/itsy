package io.temporal.learning;

import java.io.*;
import java.util.Random;
import java.time.Duration;
import io.temporal.activity.*;

@ActivityInterface
public interface TutorialActivities {
    public String echo(String input);
    
    public ActivityOptions quickActivityOptions = ActivityOptions.newBuilder()
    // Max Activity execution
    .setStartToCloseTimeout(Duration.ofSeconds(12))
    // Entire duration: scheduling to completion, including queue time.
    .setScheduleToCloseTimeout(Duration.ofSeconds(20))
    .build();
    
    // Implement the vended activities, normally externally
    public static class TutorialActivitiesImpl implements TutorialActivities {
        @Override
        public String echo(String input) {
            StringBuilder reversed = new StringBuilder(input).reverse();
            return reversed.toString();
        }
    }
}
