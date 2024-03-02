package io.temporal.learning;

import java.io.*;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.*;

@WorkflowInterface
public interface TutorialWorkflow {
    // Vend a single `startWorkflow` method
    @WorkflowMethod void executeLongRunningProcess();
    
    // Implement `startWorkflow` and vend a prebuilt `activityStub`
    public static class TutorialWorkflowImpl implements TutorialWorkflow {
        
        // Deferred binding determines the implementation at runtime.
        private TutorialActivities activityStub = Workflow.newActivityStub(TutorialActivities.class, TutorialActivities.longRunningActivityOptions);
        
        // Simple Workflow content: Update this method to do other things.
        @Override
        public void executeLongRunningProcess() {
            // NO TRY-CATCH HERE
            activityStub.executeLongRunningProcess();
        }
    }
}
