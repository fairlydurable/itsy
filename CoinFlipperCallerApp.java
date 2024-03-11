package io.temporal.learning;

import io.temporal.common.*;
import io.temporal.client.*;
import io.temporal.serviceclient.*;
import io.temporal.worker.*;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import java.io.*;
import java.lang.System;
import javax.net.ssl.SSLException;

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

        String userHome = System.getProperty("user.home");
        String clientCertPath = userHome + "/.ssh/tcloud.pem";
        String clientKeyPath = userHome + "/.ssh/tcloud.key";
        
        try {
            // Generate SSL context
            InputStream clientCertInputStream = new FileInputStream(clientCertPath);
            InputStream clientKeyInputStream = new FileInputStream(clientKeyPath);
            SslContext sslContext = SimpleSslContextBuilder.forPKCS8(clientCertInputStream, clientKeyInputStream).build();

            
            // Set service stub options and generate the stub
            String namespace = "docs-assembly.a2dd6";
            String qualifiedNamespace = namespace + ".tmprl.cloud:7233";
            WorkflowServiceStubsOptions stubsOptions = WorkflowServiceStubsOptions
            .newBuilder()
            .setSslContext(sslContext)
            .setTarget(qualifiedNamespace)
            .build();
            WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newServiceStubs(stubsOptions);
            
            // Set the client options
            WorkflowClientOptions clientOptions = WorkflowClientOptions
            .newBuilder()
            .setNamespace(namespace)
            .build();
            
            // Initialize the Temporal Client
            WorkflowClient client = WorkflowClient.newInstance(serviceStub, clientOptions);
            
            // Create a new Workflow Execution
            RetryOptions retryOptions =
            RetryOptions.newBuilder().setMaximumAttempts(4).build();
            WorkflowOptions options = WorkflowOptions
            .newBuilder()
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
            
            // Retrieve the Workflow ID. For some reason this returned
            // the Workflow Definition name for me, and not the ID.
            String workflowId = WorkflowStub.fromTyped(workflow)
                .getExecution().getWorkflowId();
            
            // Share the results
            System.out.printf("Results [%s]\n", workflowId);
            for (int index = 0; index < results.length; index++) {
                System.out.printf("%d: %s\n", index + 1, results[index]);
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SSLException e) {
            System.out.println(e.getMessage());
        }

        System.exit(0);
    }
}

