package engine;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Jerome on 29/05/2017.
 */
public class EngineUtils {

    public static String loadRessource(String fileName) throws Exception {
        String result;
        try (InputStream in = EngineUtils.class.getClass().getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
}
