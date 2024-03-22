package io.temporal.learning;

import java.io.*;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.*;

@WorkflowInterface
public interface TutorialWorkflow {
    @WorkflowMethod void startWorkflow();
    
    public static class TutorialWorkflowImpl implements TutorialWorkflow {
        private TutorialActivities activityStub = Workflow
            .newActivityStub(TutorialActivities.class,
                             TutorialActivities.quickActivityOptions);
        
        @Override
        public void startWorkflow() {
            activityStub.changeMantra();
        }
    }
}
