package io.temporal.learning;
import io.temporal.worker.*;
import io.temporal.client.*;
import io.temporal.serviceclient.*;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import java.io.*;
import java.lang.System;
import javax.net.ssl.SSLException;

public class ForecastWorker {
    public static void runForecastWorker(String[] args) {
        String userHome = System.getProperty("user.home");
        String clientCertPath = userHome + "/.ssh/tcloud.pem";
        String clientKeyPath = userHome + "/.ssh/tcloud.key";
        
        try {
            // Generate SSL context
            InputStream clientCertInputStream = new FileInputStream(clientCertPath);
            InputStream clientKeyInputStream = new FileInputStream(clientKeyPath);
            SslContext sslContext = SimpleSslContextBuilder.forPKCS8(clientCertInputStream, clientKeyInputStream).build();
            
            // Set service stub options and generate the stub
            String namespace = "ericasadun-dev.a2dd6";
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
            
            // A Workflow Factory creates workers.
            WorkerFactory factory = WorkerFactory.newInstance(client);
            
            // A Worker listens to one task queue, processing workflows and activities.
            Worker worker = factory.newWorker("ForecastWorkflow-queue");
            
            // Register a Workflow implementation with this worker.
            // The implementation must be known at runtime to dispatch workflow tasks.
            worker.registerWorkflowImplementationTypes(ForecastWorkflow.ForecastWorkflowImpl.class);
            
            // Register Activity Types from the Activity vendor with the worker.
            // Each activity is stateless and thread-safe, so use a single shared instance.
            worker.registerActivitiesImplementations(new ForecastActivities.ForecastActivitiesImpl());
            
            // Start all registered workers. The workers will start polling.
            factory.start();
            
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SSLException e) {
            System.out.println(e.getMessage());
        }
    }
}
