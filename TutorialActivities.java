package io.temporal.learning;

import java.io.*;
import java.util.Random;
import java.time.Duration;
import io.temporal.activity.*;

@ActivityInterface
public interface TutorialActivities {
    public String flip();
    
    public ActivityOptions quickActivityOptions = ActivityOptions.newBuilder()
    .setStartToCloseTimeout(Duration.ofSeconds(12)) // Max Activity execution time only
    .setScheduleToCloseTimeout(Duration.ofSeconds(120)) // Entire duration from scheduling to completion, including queue time.
    .build();
    
    // Implement the vended activities, normally externally
    public static class TutorialActivitiesImpl implements TutorialActivities {
        @Override
        public String flip() {
            return (new Random()).nextBoolean() ? "head" : "tail";
        }
    }
}
