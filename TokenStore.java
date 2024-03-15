package token;

import java.util.Properties;
import java.io.*;

public class TokenStore {
    private static final String FILENAME = "tokens.properties";
    private Properties properties;
    
    public TokenStore() {
        properties = new Properties();
        File file = new File(FILENAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error initializing TokenStore: " + e.getMessage());
                System.exit(1);
            }
        }
        try (FileInputStream fileInputStream = new FileInputStream(FILENAME)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println("Error loading properties from file: " + e.getMessage());
        }
    }
    
    private void createStoreIfNeeded() {
    }
    
    public String get(String key) {
        String value;
        value = properties.getProperty(key);
        return value;
    }
    
    public void set(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }
    
    public void delete(String key) {
        properties.remove(key);
        saveProperties();
    }
    
    private void saveProperties() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILENAME)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            System.err.println("Error saving properties to file: " + e.getMessage());
        }
    }
}
