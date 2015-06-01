package bzh.terrevirtuelle.navisu.instruments.gpstrack.view.targets;

import gov.nasa.worldwind.render.Material;
import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShipTypeColor {

    public static final Map<Integer, Material> MATERIAL = Collections.unmodifiableMap(new HashMap<Integer, Material>() {
        {
            put(0, Material.RED);
            put(20, Material.ORANGE);
            put(21, Material.WHITE);
            put(22, Material.WHITE);
            put(23, Material.WHITE);
            put(24, Material.WHITE);
            put(25, Material.WHITE);
            put(26, Material.WHITE);
            put(27, Material.WHITE);
            put(28, Material.WHITE);
            put(29, Material.WHITE);
            put(30, new Material(new Color(0xFFA07A)));
            put(31, new Material(new Color(0xFFA07A)));
            put(32, Material.WHITE);
            put(33, Material.WHITE);
            put(34, Material.WHITE);
            put(35, Material.GRAY);
            put(36, Material.BLUE);
            put(37, new Material(new Color(0xFF00FF)));
            put(38, Material.WHITE);
            put(39, Material.WHITE);
            put(40, new Material(new Color(0xFF6347)));
            put(41, new Material(new Color(0xFF6347)));
            put(42, new Material(new Color(0xFF6347)));
            put(43, new Material(new Color(0xFF6347)));
            put(44, new Material(new Color(0xFF6347)));
            put(45, new Material(new Color(0xFF6347)));
            put(46, new Material(new Color(0xFF6347)));
            put(47, new Material(new Color(0xFF6347)));
            put(48, new Material(new Color(0xFF6347)));
            put(49, new Material(new Color(0xFF6347)));
            put(50, new Material(new Color(0x40E0D0)));
            put(51, new Material(new Color(0x40E0D0)));
            put(52, new Material(new Color(0x40E0D0)));
            put(53, new Material(new Color(0x40E0D0)));
            put(54, new Material(new Color(0x40E0D0)));
            put(55, Material.WHITE);
            put(56, Material.WHITE);
            put(57, Material.WHITE);
            put(58, Material.WHITE);
            put(59, Material.WHITE);
            put(60, Material.BLUE);
            put(61, Material.BLUE);
            put(62, Material.BLUE);
            put(63, Material.BLUE);
            put(64, Material.BLUE);
            put(65, Material.BLUE);
            put(66, Material.BLUE);
            put(67, Material.BLUE);
            put(68, Material.BLUE);
            put(69, Material.BLUE);
            put(70, Material.GREEN);
            put(71, Material.GREEN);
            put(72, Material.GREEN);
            put(73, Material.GREEN);
            put(74, Material.GREEN);
            put(75, Material.GREEN);
            put(76, Material.GREEN);
            put(77, Material.GREEN);
            put(78, Material.GREEN);
            put(79, Material.GREEN);
            put(80, Material.RED);
            put(81, Material.RED);
            put(82, Material.RED);
            put(83, Material.RED);
            put(84, Material.RED);
            put(85, Material.RED);
            put(86, Material.RED);
            put(87, Material.RED);
            put(88, Material.RED);
            put(89, Material.RED);
            put(90, Material.WHITE);
            put(91, Material.WHITE);
            put(92, Material.WHITE);
            put(93, Material.WHITE);
            put(94, Material.WHITE);
            put(95, Material.WHITE);
            put(96, Material.WHITE);
            put(97, Material.WHITE);
            put(98, Material.WHITE);
            put(99, Material.WHITE);
        }
    });
    public static final Map<Integer, javafx.scene.paint.Color> COLOR = Collections.unmodifiableMap(new HashMap<Integer, javafx.scene.paint.Color>() {
        {
            put(0, javafx.scene.paint.Color.RED);
            put(20, javafx.scene.paint.Color.ORANGE);
            put(21, javafx.scene.paint.Color.WHITE);
            put(22, javafx.scene.paint.Color.WHITE);
            put(23, javafx.scene.paint.Color.WHITE);
            put(24, javafx.scene.paint.Color.WHITE);
            put(25, javafx.scene.paint.Color.WHITE);
            put(26, javafx.scene.paint.Color.WHITE);
            put(27, javafx.scene.paint.Color.WHITE);
            put(28, javafx.scene.paint.Color.WHITE);
            put(29, javafx.scene.paint.Color.WHITE);
            put(30, javafx.scene.paint.Color.valueOf("0xffa07a"));
            put(31, javafx.scene.paint.Color.valueOf("0xffa07a"));
            put(32, javafx.scene.paint.Color.WHITE);
            put(33, javafx.scene.paint.Color.WHITE);
            put(34, javafx.scene.paint.Color.WHITE);
            put(35, javafx.scene.paint.Color.GRAY);
            put(36, javafx.scene.paint.Color.valueOf("0xff00ff"));
            put(37, javafx.scene.paint.Color.valueOf("0xff00ff"));
            put(38, javafx.scene.paint.Color.WHITE);
            put(39, javafx.scene.paint.Color.WHITE);
            put(40, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(41, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(42, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(43, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(44, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(45, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(46, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(47, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(48, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(49, javafx.scene.paint.Color.valueOf("0xff6347"));
            put(50, javafx.scene.paint.Color.valueOf("0x40e0d0"));
            put(51, javafx.scene.paint.Color.valueOf("0x40e0d0"));
            put(52, javafx.scene.paint.Color.valueOf("0x40e0d0"));
            put(53, javafx.scene.paint.Color.valueOf("0x40e0d0"));
            put(54, javafx.scene.paint.Color.valueOf("0x40e0d0"));
            put(55, javafx.scene.paint.Color.WHITE);
            put(56, javafx.scene.paint.Color.WHITE);
            put(57, javafx.scene.paint.Color.WHITE);
            put(58, javafx.scene.paint.Color.WHITE);
            put(59, javafx.scene.paint.Color.WHITE);
            put(60, javafx.scene.paint.Color.BLUE);
            put(61, javafx.scene.paint.Color.BLUE);
            put(62, javafx.scene.paint.Color.BLUE);
            put(63, javafx.scene.paint.Color.BLUE);
            put(64, javafx.scene.paint.Color.BLUE);
            put(65, javafx.scene.paint.Color.BLUE);
            put(66, javafx.scene.paint.Color.BLUE);
            put(67, javafx.scene.paint.Color.BLUE);
            put(68, javafx.scene.paint.Color.BLUE);
            put(69, javafx.scene.paint.Color.BLUE);
            put(70, javafx.scene.paint.Color.GREEN);
            put(71, javafx.scene.paint.Color.GREEN);
            put(72, javafx.scene.paint.Color.GREEN);
            put(73, javafx.scene.paint.Color.GREEN);
            put(74, javafx.scene.paint.Color.GREEN);
            put(75, javafx.scene.paint.Color.GREEN);
            put(76, javafx.scene.paint.Color.GREEN);
            put(77, javafx.scene.paint.Color.GREEN);
            put(78, javafx.scene.paint.Color.GREEN);
            put(79, javafx.scene.paint.Color.GREEN);
            put(80, javafx.scene.paint.Color.RED);
            put(81, javafx.scene.paint.Color.RED);
            put(82, javafx.scene.paint.Color.RED);
            put(83, javafx.scene.paint.Color.RED);
            put(84, javafx.scene.paint.Color.RED);
            put(85, javafx.scene.paint.Color.RED);
            put(86, javafx.scene.paint.Color.RED);
            put(87, javafx.scene.paint.Color.RED);
            put(88, javafx.scene.paint.Color.RED);
            put(89, javafx.scene.paint.Color.RED);
            put(90, javafx.scene.paint.Color.WHITE);
            put(91, javafx.scene.paint.Color.WHITE);
            put(92, javafx.scene.paint.Color.WHITE);
            put(93, javafx.scene.paint.Color.WHITE);
            put(94, javafx.scene.paint.Color.WHITE);
            put(95, javafx.scene.paint.Color.WHITE);
            put(96, javafx.scene.paint.Color.WHITE);
            put(97, javafx.scene.paint.Color.WHITE);
            put(98, javafx.scene.paint.Color.WHITE);
            put(99, javafx.scene.paint.Color.WHITE);
        }
    });

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        Set<Map.Entry<Integer, Material>> entries = ShipTypeColor.MATERIAL.entrySet();
        buffer.append("[");
        entries.stream().forEach((e) -> {
            buffer.append("[").append(e.getKey()).append(", ").append(e.getValue()).append("]");
        });
        buffer.append("]");
        return buffer.toString();
    }

    public static Material getMaterial(int type) {
        if (type > 0 && type < 20) {
            return Material.WHITE;
        }
        if (type > 99) {
            return Material.WHITE;
        }
        return MATERIAL.get(type);
    }

    public static javafx.scene.paint.Color getColor(int type) {
        if (type > 0 && type < 20) {
            return javafx.scene.paint.Color.WHITE;
        }
        if (type > 99) {
            return javafx.scene.paint.Color.WHITE;
        }
        return COLOR.get(type);
    }
}
