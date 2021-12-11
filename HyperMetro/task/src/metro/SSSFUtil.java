package metro;

import java.util.Comparator;
//import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class SSSFUtil {
    static void INITIALIZE_SINGLE_SOURCE(G G, Station s) {
        for (LinkedList<Station> list : G.V.values()) {
            for (Station v : list) {
//                v.d = Integer.MAX_VALUE;
                v.d = 9999;
                v.p = null;
            }
        }
        s.d = 0;
    }

    static void RELAX(Station u, Station v, int w, PriorityQueue<Station> Q) {
//        System.out.printf(" -> Compare %d with %d.\n", v.d, u.d + w);
        if (v.d > u.d + w) {
            // UPDATE ALL EDGES IF HAS LESSER WEIGHT
            v.d = u.d + w; // w???
            v.p = u;

            Q.remove(v);
            Q.add(v); // !! reinsert object to the priority queue, very important
//            System.out.printf(" > Set \"%s\": d to %s min, p to \"%s\"\n", v, u.d + w, u);
//            System.out.println(Q);
        }
    }

    static void DIJKSTRA(G G, Station s) {
        INITIALIZE_SINGLE_SOURCE(G, s);
//        HashSet<Station> S = new HashSet<>(); // whose final shortest-path weights from the source s have already been determined.

        PriorityQueue<Station> Q = new PriorityQueue<>(Comparator.comparingInt(v -> v.d));
        for (LinkedList<Station> list : G.V.values()) {
            Q.addAll(list);
        }

        while (!Q.isEmpty()) {
            Station u = EXTRACT_MIN(Q);
//            S.add(u);
//            System.out.printf("\nGot %s, d: %s min\n", u, u.d);
//            if (u.d > 9999) throw new RuntimeException();
            G.Adj.get(u).forEach((v, w) -> {
//                System.out.printf("    a> \"%s\", d is %d min", v, w);
                RELAX(u, v, w, Q);
            });
        }
    }

    static <T> T EXTRACT_MIN(PriorityQueue<T> Q) {
        return Q.poll();
    }
}