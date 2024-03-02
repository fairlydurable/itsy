package io.temporal.learning;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Duration;
import io.temporal.activity.*;
import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.common.RetryOptions;
import commandutility.*;

@ActivityInterface
public interface TutorialActivities {
    public void executeLongRunningProcess();
    
    // Set retry policy
    public RetryOptions retryOptions = RetryOptions.newBuilder()
    .setInitialInterval(Duration.ofSeconds(1))
    .setMaximumInterval(Duration.ofSeconds(10))
    .build();
    
    // Timeout after 5 minutes. Feel free to adjust. Heartbeat sent every 30 seconds.
    // Default: ActivityCancellationType.TRY_CANCEL. Attempt cancel, don't wait for confirmation.
    // WAIT_CANCELLATION_COMPLETED (wait for completion confirmation)
    // ABANDON (abandon the activity cancellation, don't wait for confirmation)
    public ActivityOptions longRunningActivityOptions = ActivityOptions.newBuilder()
    .setStartToCloseTimeout(Duration.ofHours(5)) // Example long duration
    .setHeartbeatTimeout(Duration.ofSeconds(300)) // Require heartbeats every 5 minutes
    .setRetryOptions(retryOptions)
    .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
    .build();
    
    // Implement the vended activities, normally externally
    public static class TutorialActivitiesImpl implements TutorialActivities {
        
        // Grab the current time
        public String getDateTime() {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd @ HH:mm:ss");
            String formattedDateTime = dateFormat.format(currentDate);
            return String.format("Proof of life at %s", formattedDateTime);
        }
        
        
        @Override
        public void executeLongRunningProcess() {
            while (true) {
                try {
                    // Wait 30 seconds.
                    Thread.sleep(30000);
                    
                    // Send a proof-of-life heartbeat to indicate activity is still running.
                    Activity.getExecutionContext().heartbeat(getDateTime());
                    
                    // Execute an audio ping to the learner
                    System.out.println(CommandExecutor.executeCommand("/usr/bin/say beep"));
                    
                    // Could add a test here for execution duration and end the activity
                    // but for now, it just runs forever until it gets terminated after 5 hours.
                } catch (IOException e) {
                    throw Activity.wrap(e);
                } catch (InterruptedException e) {
                    throw Activity.wrap(e);
                }
                // removed ActivityCanceledException and InterruptedException
            }
        }
    }
}
