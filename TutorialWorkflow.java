package io.temporal.learning;

import java.io.*;
import java.io.IOException;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.*;
import io.temporal.weather.*;

@WorkflowInterface
public interface TutorialWorkflow {
    @WorkflowMethod String startWorkflow(String input);
    
    public static class TutorialWorkflowImpl implements TutorialWorkflow {
        
        private TutorialActivities activityStub = Workflow.newActivityStub(TutorialActivities.class, TutorialActivities.quickActivityOptions);
        
        @Override
        public String startWorkflow(String input) {
            String[] latlon = activityStub.fetchGeolocation(input);
            String forecast = activityStub.fetchForecast(latlon);
            return forecast;
        }
    }
}
