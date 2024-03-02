package io.temporal.learning;
import java.io.*;
import java.time.Duration;
import io.temporal.activity.*;
import commandutility.*;

@ActivityInterface
public interface TutorialActivities {
  // Vended activities.
  public String[] execute(String input);

    public ActivityOptions quickActivityOptions = ActivityOptions.newBuilder()
    .setStartToCloseTimeout(Duration.ofSeconds(12)) // Max Activity execution time only
    .setScheduleToCloseTimeout(Duration.ofSeconds(120)) // Entire duration from scheduling to completion, including queue time.
    .build();

  // Implement the vended activities, normally externally
  public static class TutorialActivitiesImpl implements TutorialActivities {
    @Override
    public String[] execute(String input) {
      return CommandExecutor.executeCommand(input);
    }
  }
}
