package commandutility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {
    
    public static String[] executeCommand(String command) {
        List<String> stdoutLines = new ArrayList<>();
        List<String> stderrLines = new ArrayList<>();
        
        try {
            // Start the process
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            Process process = processBuilder.start();
            
            // Read output from the process's stdout
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = stdoutReader.readLine()) != null) {
                stdoutLines.add(line);
            }
            stdoutReader.close();
            
            // Read output from the process's stderr
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = stderrReader.readLine()) != null) {
                stderrLines.add(line);
            }
            stderrReader.close();
            
            // Wait for process to finish
            int exitCode = process.waitFor();
            System.out.println("Process exited with code " + exitCode);
            String exitCodeStr = Integer.toString(exitCode);
            
            return new String[]{String.join("\n", stdoutLines), String.join("\n", stderrLines), exitCodeStr};
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
        return new String[]{String.join("\n", stdoutLines), String.join("\n", stderrLines)};
    }
}
