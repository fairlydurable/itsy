package io.temporal.learning;

import io.temporal.common.*;
import io.temporal.client.*;
import io.temporal.serviceclient.*;
import io.temporal.worker.*;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import java.io.*;
import java.lang.System;
import javax.net.ssl.SSLException;

public class ForecastFetcher {
    public static String runForecastWorker(String ipaddress) {
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
            WorkflowClient client = WorkflowClient
            .newInstance(serviceStub, clientOptions);
            
            // Create a new Workflow Execution
            // RetryOptions retryOptions = RetryOptions.newBuilder().setMaximumAttempts(2).build();
            WorkflowOptions options = WorkflowOptions
            .newBuilder()
            .setTaskQueue("ForecastWorkflow-queue")
            // .setRetryOptions(retryOptions)
            .build();
            
            // Build the Workflow stub for dynamic invocation
            ForecastWorkflow workflow =
            client.newWorkflowStub(ForecastWorkflow.class, options);
            
            // Run the Workflow and wait for the results array
            String results = workflow.startWorkflow(ipaddress);
            return results;
            
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return "Sorry! Configuration problems. Please try later.";
        } catch (SSLException e) {
            System.out.println(e.getMessage());
            return "Sorry! Configuration problems. Please try later.";
        }
    }
}

