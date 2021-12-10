package metro;

import java.util.HashMap;
import java.util.LinkedList;

class G {
    final HashMap<String, LinkedList<Station>> V = new HashMap<>();
    final HashMap<Station, HashMap<Station, Integer>> Adj = new HashMap<>();

    G(HashMap<String, HashMap<Integer, Station>> rawMap) {
//        rawMap.forEach((entry, value) -> System.out.printf("data of %s: %s\n", entry, value.toString())); // debug only
        for (String entry : rawMap.keySet()) {
            HashMap<Integer, Station> map = rawMap.get(entry);
            LinkedList<Station> line = new LinkedList<>();
            // unordered map to ordered linked-list
            if (map.get(0) != null) {
                for (int i = 0; i < map.size(); i++) {
                    line.add(map.get(i));
                }
            } else {
                for (int i = 1; i <= map.size(); i++) {
                    line.add(map.get(i)); // normalization
                }
            }
            V.put(entry, line);
        }
        initAdj();
    }

    private void initAdj() {
        for (LinkedList<Station> list : V.values()) {
            for (Station v : list) {
                Adj.put(v, getAdj(v));
            }
        }
    }

    private HashMap<Station, Integer> getAdj(Station s) {
        HashMap<Station, Integer> adjNodes = new HashMap<>();
//        System.out.print("\nAssociate " + s + ": \n");

        LinkedList<Station> line = V.get(findLineRef(s));
        int index = line.indexOf(s);
        if (index > 0) {
            Station prev = line.get(index - 1);
            putAdj(adjNodes, prev, prev.time);
        }
        if (index < line.size() - 1) {
            Station next = line.get(index + 1);
            putAdj(adjNodes, next, s.time);
        }
        return adjNodes;
    }

    private void putAdj(HashMap<Station, Integer> adjNodes, Station s, int time) {
        if (!s.transfer.isEmpty()) {
            for (HashMap<String, String> transfer : s.transfer) {
                String lineRef = transfer.get("line");
                String stationRef = transfer.get("station");
                Station trans = findStation(lineRef, stationRef);
//                System.out.printf("Adj, put: %s with %d, (transfer)\n", trans, time + 5);
                adjNodes.put(trans, time + 5); // transition takes 5 minutes
            }
        }

//        System.out.printf("Adj, put: %s with %d.\n", s, time);
        adjNodes.put(s, time);
    }


    public String findLineRef(Station station) {
        for (String ref : V.keySet()) {
            if (V.get(ref).contains(station)) {
                return ref;
            }
        }
        return null;
    }

    public Station findStation(String lineRef, String stationRef) {
        for (Station s : V.get(lineRef)) {
            if (stationRef.equals(s.name)) {
                return s;
            }
        }
        return null;  // return null if we didn't find any
    }
}
