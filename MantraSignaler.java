package io.temporal.learning;

import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.client.schedules.*;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import java.util.Collections;

public class MantraSignaler {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs
        .newServiceStubs(WorkflowServiceStubsOptions.newBuilder()
                         .setTarget("localhost:7233").build());
        WorkflowClient client = WorkflowClient.newInstance(service);

        WorkflowOptions options = WorkflowOptions.newBuilder()
        .setTaskQueue("TutorialWorkflow-queue")
        .build();
        
        TutorialWorkflow workflow = client.newWorkflowStub(TutorialWorkflow.class, options);
        workflow.startWorkflow();
    }
}
