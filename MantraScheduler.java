package io.temporal.learning;

import io.temporal.client.WorkflowOptions;
import io.temporal.client.schedules.*;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import java.util.Collections;

public class MantraScheduler {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs
            .newServiceStubs(WorkflowServiceStubsOptions.newBuilder()
            .setTarget("localhost:7233").build());
        ScheduleClient scheduleClient = ScheduleClient.newInstance(service);
        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
            .setWorkflowId("tutorial-mantra-workflow")
            .setTaskQueue("TutorialWorkflow-queue").build();
        ScheduleActionStartWorkflow action = ScheduleActionStartWorkflow.newBuilder()
            .setWorkflowType(TutorialWorkflow.class)
            .setOptions(workflowOptions)
            .build();
        
        // Schedule every 3 minutes
        ScheduleSpec spec = ScheduleSpec.newBuilder()
            .setCronExpressions(Collections.singletonList("*/3 * * * *")).build();
        Schedule schedule = Schedule.newBuilder()
            .setAction(action).setSpec(spec).build();
        scheduleClient.createSchedule("mantra-update", schedule,
            ScheduleOptions.newBuilder().build());
    }
}
