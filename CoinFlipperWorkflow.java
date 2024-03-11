package io.temporal.learning;

import java.io.*;

import java.util.List;
import java.util.ArrayList;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.*;

@WorkflowInterface
public interface CoinFlipperWorkflow {
    @WorkflowMethod String[] startWorkflow(int count);
    public static class CoinFlipperWorkflowImpl implements CoinFlipperWorkflow {
        private CoinFlipperActivities activityStub = Workflow
             .newActivityStub(CoinFlipperActivities.class,
                              CoinFlipperActivities.flipActivityOptions);
        @Override
        public String[] startWorkflow(int count) {
            List<String> results = new ArrayList<>();
            for (int index = 0; index < count; index++) {
                long startTime = System.nanoTime();
                String value = activityStub.intermittentFlip();
                results.add(value);
                long duration = (System.nanoTime() - startTime) / 1_000_000;
                System.out.printf(
                    "Flip duration: %d milliseconds\n", duration);
            }
            return results.toArray(new String[0]);
        }
    }
}
