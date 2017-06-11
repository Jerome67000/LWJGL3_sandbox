package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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

    public static List<String> readAllLines(String fileName) {
        List<String> list = new ArrayList<>();

        InputStream in = EngineUtils.class.getResourceAsStream(fileName);
        InputStreamReader streamReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(streamReader);

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
