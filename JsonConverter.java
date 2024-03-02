package jsonutilities;
import com.google.gson.Gson;

public class JsonConverter {
    public static String convertToJson(String[] array) {
        Gson gson = new Gson();
        return gson.toJson(array);
    }
}
