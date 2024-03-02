package io.temporal.learning;

import java.io.*;
import java.io.IOException;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.*;
import io.temporal.weather.*;

@WorkflowInterface
public interface ForecastWorkflow {
    @WorkflowMethod String startWorkflow(String input);
    
    public static class ForecastWorkflowImpl implements ForecastWorkflow {
        
        private ForecastActivities activityStub = Workflow.newActivityStub(ForecastActivities.class, ForecastActivities.quickActivityOptions);
        
        @Override
        public String startWorkflow(String input) {
            String[] latlon = activityStub.fetchGeolocation(input);
            String forecast = activityStub.fetchForecast(latlon);
            return forecast;
        }
    }
}
