package io.temporal.learning;
import java.io.BufferedOutputStream;
import java.io.PrintStream;

public class ForecastWorkerApp {
    static {
        // Enable print statements
        System.setOut(new PrintStream(new BufferedOutputStream(System.out), true));
        System.setErr(new PrintStream(new BufferedOutputStream(System.err), true));
    }

    public static void main(String[] args) {
        ForecastWorker.runForecastWorker(args);
    }
}
