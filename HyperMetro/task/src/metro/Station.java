package metro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Station {
    String name;
    List<HashMap<String, String>> transfer = new ArrayList<>();

    transient String color = "WHITE";
    transient int d = Integer.MAX_VALUE;
    transient Station p = null;

    public Station(String name) {
        this.name = name;
    }

    public void addTransfer(String ref, String station) {
        HashMap<String, String> map = new HashMap<>();
        map.put("station", station);
        map.put("line", ref);
        transfer.add(map);
    }

    @Override
    public String toString() {
        if (!transfer.isEmpty()) {
            StringBuilder description = new StringBuilder();
            transfer.forEach(x -> description.append(
                    String.format(" - %s (%s)", x.get("station"), x.get("line"))
            ));
            return name + description;
        }
        return name;
    }
}
