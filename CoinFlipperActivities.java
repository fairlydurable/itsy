package io.temporal.learning;

import java.io.*;
import java.time.Duration;
import java.util.Random;

import io.temporal.activity.*;
import io.temporal.common.RetryOptions;

@ActivityInterface
public interface CoinFlipperActivities {
    public String intermittentFlip(); // Activity
    
    public Exception flipException = new Exception("Coin flip error");
    
    public ActivityOptions flipActivityOptions = ActivityOptions.newBuilder()
        .setStartToCloseTimeout(Duration.ofSeconds(2)) // Activity duration
        .setScheduleToCloseTimeout(Duration.ofSeconds(10)) // Total duration
        .build();
    
    public static class CoinFlipperActivitiesImpl 
                        implements CoinFlipperActivities {
        @Override
        public String intermittentFlip() {
            // Create a random "service connection" delay
            Random randomGenerator = new Random(System.nanoTime());
            try { Thread.sleep(randomGenerator.nextInt(1000)); }
            catch (InterruptedException e) { e.printStackTrace(); }

            // Should there be an activity failure? If so pause and
            // throw an exception.
            if (randomGenerator.nextBoolean()) {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
                System.out.printf("Encountered intermittent service error.\n");
                throw Activity.wrap(flipException);
            }
            
            // Return heads or tails
            return randomGenerator.nextBoolean() ? "Heads" : "Tails";
        }
    }
}
