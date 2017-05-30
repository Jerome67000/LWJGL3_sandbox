package engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Jerome on 29/05/2017.
 */
public class EngineUtils {

    public static String loadResource(String fileName) throws Exception {
        String result = "";
        try (InputStream in = EngineUtils.class.getClass().getResourceAsStream(fileName)) {
            Scanner scanner = new Scanner(in, "UTF-8");
            result = scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            System.err.println("Can't load resource: " + fileName);
        }

        System.out.println("****** Resource loaded: " + fileName +  " \n" + result + "\n");

        return result;
    }

}
