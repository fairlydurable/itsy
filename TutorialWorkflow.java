package io.temporal.learning;
import java.io.*;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.*;
import jsonutilities.*;


@WorkflowInterface
public interface TutorialWorkflow {
    @WorkflowMethod String startWorkflow(String input);
    
    public static class TutorialWorkflowImpl implements TutorialWorkflow {
        
        // Deferred binding determines the implementation at runtime.
        private TutorialActivities activityStub = Workflow.newActivityStub(TutorialActivities.class, TutorialActivities.quickActivityOptions);
        
        @Override
        public String startWorkflow(String input) {
            String[] results = activityStub.execute(input);
            return JsonConverter.convertToJson(results);
        }
    }
}
