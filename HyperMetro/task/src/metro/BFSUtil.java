package metro;

import java.util.LinkedList;

class BFSUtil {
    static void BFS(G G, Station s) {
        for (LinkedList<Station> list : G.V.values()) {
            for (Station v : list) {
                v.color = "WHITE";
                v.d = Integer.MAX_VALUE;
                v.p = null;
            }
        }
        s.color = "GRAY";
        s.d = 0;
        s.p = null;
        LinkedList<Station> Q = new LinkedList<>();
        ENQUEUE(Q, s);
        while (!Q.isEmpty()) {
            Station u = DEQUEUE(Q);
            G.Adj.get(u).forEach((v, w) -> {
                if ("WHITE".equals(v.color)) {
                    v.color = "GRAY";
                    v.d = u.d + 1;
                    v.p = u;
                    ENQUEUE(Q, v);
                }
            });
            u.color = "BLACK";
        }
    }

    static void ENQUEUE(LinkedList<Station> Q, Station v) {
        Q.addFirst(v);
    }

    static Station DEQUEUE(LinkedList<Station> Q) {
        return Q.removeLast();
    }
//    static void PRINT_PATH(Station s, Station v) {
//        if (v == s) {
//            System.out.println(s);
//        } else if (v.p == null) {
//            System.out.printf("No path from %s to %s exists.\n", s, v);
//        } else {
//            PRINT_PATH(s, v.p);
//            System.out.println(v);
//        }
//    }
}
