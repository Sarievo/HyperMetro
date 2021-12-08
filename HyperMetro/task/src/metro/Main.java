package metro;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static Scanner s = new Scanner(System.in);
    static String[] errMsg = {
            "Error! Such a file doesn't exist!",
            "Incorrect file",
            "Invalid command"
    };
    static List<String> commands = List.of("/add", "/append", "/add-head", "/remove", "/output");
    static Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    static HashMap<String, LinkedList<String>> lineMap = new HashMap<>();

    static void loadMap(HashMap<String, HashMap<Integer, String>> rawMap) {
//        rawMap.forEach((entry, value) -> System.out.printf("data of %s: %s\n", entry, value)); // debug only
        for (String entry : rawMap.keySet()) {
            HashMap<Integer, String> map = rawMap.get(entry);
            LinkedList<String> line = new LinkedList<>();
            // unordered map to ordered linked-list
            for (int i = 1; i <= map.size(); i++) {
//                System.out.println("putting " + map.get(i) + "...");
                line.add(map.get(i));
            }
            lineMap.put(entry, line);
        }
    }

    public static void main(String[] args)  {
        Path path = Paths.get(args[0]);

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            //    static HashMap<String, HashMap<Integer, String>> lineMap;
            loadMap(gson.fromJson(reader,
                    new TypeToken<HashMap<String, HashMap<Integer, String>>>(){}.getType()
            ));

            List<String> params = input();

            while (!"/exit".equals(params.get(0))) {
                if (!commands.contains(params.get(0))) {
                    err(2);
                    params = input();
                    continue;
                }

                try {
                    switch (params.get(0)) {
                        case "/add":
                        case "/append":
                            append(params.get(1), params.get(2));
                            break;
                        case "/add-head":
                            addHead(params.get(1), params.get(2));
                            break;
                        case "/remove":
                            remove(params.get(1), params.get(2));
                            break;
                        case "/output":
                            output(params.get(1));
                            break;
                    }
                } catch (Exception e) {
//                    e.printStackTrace();  // debug
                    err(2); // catch any exceptions
                }

                params = input();
            }
        } catch (IOException e) {
            err(0);
        } catch (JsonSyntaxException e) {
            err(1);
        }
    }

    private static List<String> input() {
        List<String> matchList = new ArrayList<>();
        Matcher regexMatcher = regex.matcher(s.nextLine());
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                matchList.add(regexMatcher.group(1)); // Add double-quoted string without the quotes
            } else if (regexMatcher.group(2) != null) {
                matchList.add(regexMatcher.group(2)); // Add single-quoted string without the quotes
            } else {
                matchList.add(regexMatcher.group()); // Add unquoted word
            }
        }
        return matchList;
    }

    private static void append(String ref, String station) {
        lineMap.get(ref).addLast(station);
    }

    private static void addHead(String ref, String station) {
        lineMap.get(ref).addFirst(station);
    }

    private static void remove(String ref, String station) {
        lineMap.get(ref).remove(station);
    }

    private static void output(String ref) {
//        HashMap<Integer, String> lineRef = lineMap.get(ref);
        LinkedList<String> line = lineMap.get(ref);
//        System.out.printf("Fetching %s... %s\n\n", ref, line); // debug only
        String s1 = "depot", s2 = line.get(0), s3;

        for (int i = 1; i < line.size(); i++) {
            s3 = line.get(i);
            System.out.printf("%s - %s - %s\n", s1, s2, s3);
            s1 = s2;
            s2 = s3;
        }
        System.out.printf("%s - %s - %s\n", s1, s2, "depot");
    }

    static void err(int i) {
        System.out.println(errMsg[i]);
    }
}