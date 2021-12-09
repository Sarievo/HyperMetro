package metro;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

class G {
    final HashMap<String, LinkedList<Station>> V = new HashMap<>();
    final HashMap<Station, Set<Station>> Adj = new HashMap<>();

    G(HashMap<String, HashMap<Integer, Station>> rawMap) {
//        rawMap.forEach((entry, value) -> System.out.printf("data of %s: %s\n", entry, value.toString())); // debug only
        for (String entry : rawMap.keySet()) {
            HashMap<Integer, Station> map = rawMap.get(entry);
            LinkedList<Station> line = new LinkedList<>();
            // unordered map to ordered linked-list
            for (int i = 1; i <= map.size(); i++) {
//                System.out.println("putting " + map.get(i) + "...");
                line.add(map.get(i));
            }
            V.put(entry, line);
        }
        initAdj();
    }

    private void initAdj() {
        for (LinkedList<Station> list : V.values()) {
            for (Station v : list) {
//                System.out.println("putting " + v + ": " + getAdj(v) + "...");
                Adj.put(v, getAdj(v));
            }
        }
    }

    private Set<Station> getAdj(Station node) {
        Set<Station> adjNodes = new HashSet<>();

        LinkedList<Station> line = V.get(findLineRef(node));
        int index = line.indexOf(node);
        if (index > 0) adjNodes.add(line.get(index - 1));
        if (index < line.size() - 1) adjNodes.add(line.get(index + 1));

        Set<Station> transitions = new HashSet<>();
        for (Station currAdj : adjNodes) {
            if (!currAdj.transfer.isEmpty()) {
                for (HashMap<String, String> transfer : currAdj.transfer) {
                    String lineRef = transfer.get("line");
                    String stationRef = transfer.get("station");
                    transitions.add(findStation(lineRef, stationRef));
                }
            }
        }
        adjNodes.addAll(transitions);
        return adjNodes;
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
