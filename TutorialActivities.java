package io.temporal.learning;

import java.io.*;
import java.time.Duration;
import io.temporal.activity.*;

@ActivityInterface
public interface TutorialActivities {
    // Vended activities.
    public void doSomethingFirst(String input);
    public void doSomethingSecond(String input);
    
    public ActivityOptions quickActivityOptions = ActivityOptions.newBuilder()
    .setStartToCloseTimeout(Duration.ofSeconds(12)) // Max Activity execution time only
    .setScheduleToCloseTimeout(Duration.ofSeconds(120)) // Entire duration from scheduling to completion, including queue time.
    .build();
    
    // Implement the vended activities, normally externally
    public static class TutorialActivitiesImpl implements TutorialActivities {
        @Override
        public void doSomethingFirst(String input) {
            System.out.println("Starting Activity 1");
            try { Thread.sleep(8000); } // sleep for a few seconds
            catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println("Finished first activity. Processed " + input);
        }
        
        @Override
        public void doSomethingSecond(String input) {
            System.out.println("Starting Activity 2");
            try { Thread.sleep(8000); } // sleep for a few seconds
            catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println("Finished second activity. Processed " + input);
        }
    }
}
