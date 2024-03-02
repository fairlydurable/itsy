package io.temporal.learning;

import java.io.*;
import java.time.Duration;
import io.temporal.activity.*;

@ActivityInterface
public interface TutorialActivities {
    public String mainActivity(String input);
    
    public ActivityOptions quickActivityOptions = ActivityOptions.newBuilder()
        .setStartToCloseTimeout(Duration.ofSeconds(12)) // Max Activity execution time only
        .setScheduleToCloseTimeout(Duration.ofSeconds(120)) // Entire duration from scheduling to completion, including queue time.
        .build();
    
    public static class TutorialActivitiesImpl implements TutorialActivities {
        @Override
        public String mainActivity(String input) {
            return "Processed \"" + input + "\"";
        }
    }
}
