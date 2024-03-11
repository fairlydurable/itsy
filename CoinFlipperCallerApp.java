package io.temporal.learning;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.common.RetryOptions;

public class CoinFlipperCallerApp {
    private static void usage() {
        System.err.println("Usage: gradle runCaller --args=<count>");
        System.exit(1);
    }

    public static void main(String[] args) {
        // Check invocation
        if (args.length != 1) { usage(); }
        int flips; try { flips = Integer.parseInt(args[0]); }
        catch (NumberFormatException e) {
            System.err.println("Error: Argument must be an integer");
            usage(); return;
        }

        // Create a new Workflow Client
        WorkflowServiceStubs service =
            WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        
        // Create a new Workflow Execution
        RetryOptions retryOptions =
            RetryOptions.newBuilder().setMaximumAttempts(4).build();
        WorkflowOptions options = WorkflowOptions
            .newBuilder()
            .setWorkflowId("CoinFlipperWorkflow")
            .setTaskQueue("CoinFlipperWorkflow-queue")
            .setRetryOptions(retryOptions)
            .build();
        
        // Build the Workflow stub for dynamic invocation
        CoinFlipperWorkflow workflow =
            client.newWorkflowStub(CoinFlipperWorkflow.class, options);
        
        // Run the Workflow and wait for the results array
        String[] results = workflow.startWorkflow(flips);
        if (results.length != flips) {
            System.err.printf("Expected %d flips, got %d\n",
                flips, results.length);
            System.exit(1);
        }
        
        // Retrieve the Workflow ID, to help locate it with the Web UI
        String workflowId = WorkflowStub.fromTyped(workflow)
            .getExecution().getWorkflowId();
        
        // Share the results
        System.out.printf("Results [%s]\n", workflowId);
        for (int index = 0; index < results.length; index++) {
            System.out.printf("%d: %s\n", index + 1, results[index]);
        }

        System.exit(0);
    }
}

