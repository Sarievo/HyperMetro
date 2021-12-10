package metro;

import java.util.HashMap;
import java.util.LinkedList;

class G {
    final HashMap<String, LinkedList<Station>> V = new HashMap<>();
    final HashMap<Station, HashMap<Station, Integer>> Adj = new HashMap<>();

    G(HashMap<String, LinkedList<Station>> rawMap) {
        V.putAll(rawMap);
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
        String lineRef = findLineRef(s);

        for (String stationRef : s.prev) {
            Station prev = findStation(lineRef, stationRef);
            putAdj(adjNodes, prev, prev.time);
        }

        for (String stationRef : s.next) {
            Station next = findStation(lineRef, stationRef);
            putAdj(adjNodes, next, s.time);
        }
        return adjNodes;
    }

    private void putAdj(HashMap<Station, Integer> adjNodes, Station s, int time) {
        if (!s.transfer.isEmpty()) {
            putTransition(adjNodes, s, time);
        }

        adjNodes.put(s, time);
//        System.out.printf("Adj, put: %s with %d.\n", s, time);
    }

    private void putTransition(HashMap<Station, Integer> adjNodes, Station s, int time) {
        for (HashMap<String, String> transfer : s.transfer) {
            String lineRef = transfer.get("line");
            String stationRef = transfer.get("station");
            Station trans = findStation(lineRef, stationRef);
            adjNodes.put(trans, time + 5); // transition takes 5 minutes
//            System.out.printf("Adj, put: %s with %d, (transfer)\n", trans, time + 5);
        }
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
