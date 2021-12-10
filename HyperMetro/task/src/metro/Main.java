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
    static Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    static G G;
    static String[] errMsg = {
            "Error! Such a file doesn't exist!",
            "Incorrect file",
            "Invalid command"
    };
    static List<String> commands = List.of(
            "/add", "/append", "/add-head", "/remove", "/output",
            "/connect", "/route", "/fastest-route"
    );

    public static void main(String[] args) {
        Path path = Paths.get(args[0]);

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            G = new G(gson.fromJson(reader,
                    new TypeToken<HashMap<String, HashMap<Integer, Station>>>() {}.getType()
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
                            add(params.get(1), params.get(2), Integer.parseInt(params.get(3)));
                            break;
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
                        case "/connect":
                            connect(params.get(1), params.get(2), params.get(3), params.get(4));
                            break;
                        case "/route":
                            route(params.get(1), params.get(2), params.get(3), params.get(4));
                            break;
                        case "/fastest-route":
                            fastestRoute(params.get(1), params.get(2), params.get(3), params.get(4));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();  // debug
//                    err(2); // catch any exceptions
                }
                params = input();
            }
        } catch (IOException e) {
            err(0);
        } catch (JsonSyntaxException e) {
            err(1);
        }
    }

    private static void fastestRoute(String l1, String s1, String l2, String s2) {
        Station st = G.findStation(l1, s1);
        Station ed = G.findStation(l2, s2);
        SSSFUtil.DIJKSTRA(G, st);

        printRt(st, ed);
        System.out.printf("Total: %d minutes in the way\n", ed.d);
    }

    private static void route(String l1, String s1, String l2, String s2) {
        Station st = G.findStation(l1, s1);
        Station ed = G.findStation(l2, s2);
        BFSUtil.BFS(G, st);

        printRt(st, ed);
    }

    private static void printRt(Station st, Station ed) {
        Station tp = ed;
        LinkedList<String> msg = new LinkedList<>();

        String currLine = G.findLineRef(tp.p);
        while (tp.p != null) {
            if (currLine.equals(G.findLineRef(tp.p))) {
                msg.add(tp.name);
            } else {
                msg.add(tp.name);
                msg.add("Transition to line " + currLine);
                msg.add(tp.name);
                currLine = G.findLineRef(tp.p);
            }
            tp = tp.p;
        }
        msg.add(st.name); // ???
//        BFSUtil.PRINT_PATH(st, ed);
        while (!msg.isEmpty()) {
            System.out.println(msg.removeLast());
        }
    }

    private static void connect(String l1, String s1, String l2, String s2) {
        Objects.requireNonNull(G.findStation(l1, s1))
                .addTransfer(l2, s2); // connect first occurrence
        Objects.requireNonNull(G.findStation(l2, s2))
                .addTransfer(l1, s1);
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

    private static void add(String lineRef, String stationRef, int time) {
        G.V.get(lineRef).addLast(new Station(stationRef, time));
    }

    private static void append(String lineRef, String stationRef) {
        G.V.get(lineRef).addLast(new Station(stationRef));
    }

    private static void addHead(String lineRef, String stationRef) {
        G.V.get(lineRef).addFirst(new Station(stationRef));
    }

    private static void remove(String lineRef, String stationRef) {
        LinkedList<Station> line = G.V.get(lineRef);
        line.removeIf(s -> stationRef.equals(s.name));
    }

    private static void output(String lineRef) {
//        HashMap<Integer, String> lineRef = lineMap.get(ref);
        LinkedList<Station> line = G.V.get(lineRef);
//        System.out.printf("Fetching %s... %s\n\n", ref, line); // debug only
        System.out.println("depot");
        for (Station s : line) {
            System.out.println(s);
        }
        System.out.println("depot");

//        String s1 = "depot", s2 = line.get(0).getName(), s3;
//        for (int i = 1; i < line.size(); i++) {
//            s3 = line.get(i).getName();
//            System.out.printf("%s - %s - %s\n", s1, s2, s3);
//            s1 = s2;
//            s2 = s3;
//        }
//        System.out.printf("%s - %s - %s\n", s1, s2, "depot");
    }

    static void err(int i) {
        System.out.println(errMsg[i]);
    }
}
