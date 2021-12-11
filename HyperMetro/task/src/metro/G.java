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
//                System.out.printf("\nfor %s:\n", v);
                HashMap<Station, Integer> localAdj = getAdj(v, false);

                if (!v.transfer.isEmpty()) {
                    for (HashMap<String, String> transfer : v.transfer) {
                        Station tf = findStation(transfer.get("line"), transfer.get("station"));
//                        Adj.put(tf, getAdj(tf, true));
//                        System.out.print("    with transition: ");
                        localAdj.putAll(getAdj(tf, true));
                    }
                }
                Adj.put(v, localAdj);
//                System.out.printf("Representation: %s %s\n", v, localAdj);
            }
        }
    }

    private HashMap<Station, Integer> getAdj(Station s, Boolean isTransition) {
        HashMap<Station, Integer> adjNodes = new HashMap<>();
        String lineRef = findLineRef(s);

        int transitionTime = isTransition ? 5 : 0;

        for (String stationRef : s.prev) {
            Station prev = findStation(lineRef, stationRef);
            adjNodes.put(prev, prev.time + transitionTime);
//            System.out.printf(" Adj, put: (S <-> %s) with %d min.\n",
//                    prev, prev.time + transitionTime);
        }
        for (String stationRef : s.next) {
            Station next = findStation(lineRef, stationRef);
            adjNodes.put(next, s.time + transitionTime);
//            System.out.printf(" Adj, put: (S <-> %s) with %d min.\n",
//                    next, s.time + transitionTime);
        }
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
