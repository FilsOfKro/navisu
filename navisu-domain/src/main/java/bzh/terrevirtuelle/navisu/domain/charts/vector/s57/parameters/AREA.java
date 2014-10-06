package bzh.terrevirtuelle.navisu.domain.charts.vector.s57.parameters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AREA {

    public static final Map<String, String> ATT = Collections.unmodifiableMap(new HashMap<String, String>() {
        {
            put("MIPARE", "MilitaryPracticeArea");
            put("SEAARE", "SeaAreaNamedWaterArea");
            put("RESARE", "RestrictedArea");
        }
    });

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        Set<Map.Entry<String, String>> entries = AREA.ATT.entrySet();
        buffer.append("[");
        entries.stream().forEach((e) -> {
            buffer.append("[").append(e.getKey()).append(", ").append(e.getValue()).append("]");
        });
        buffer.append("]");
        return buffer.toString();
    }

}
