import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TOOLS_NeighborhoodParser {

    public static void parseFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && line.contains("=")) {
                    String[] parts = line.split("=");
                    String name = parts[0].trim();
                    String coordinatesPart = parts[1].trim();
                    List<int[]> neighbors = parseCoordinates(coordinatesPart);
                    TOOLS_Neighborhoods.addCustomNeighborhood(name, neighbors);
                }
            }
        }
    }

    private static List<int[]> parseCoordinates(String coordinatesPart) {
        List<int[]> neighbors = new ArrayList<>();
        // Remove the outer parentheses
        coordinatesPart = coordinatesPart.substring(1, coordinatesPart.length() - 1);
        // Split the coordinates by "), ("
        String[] coordinatePairs = coordinatesPart.split("\\),\\s*\\(");
        for (String pair : coordinatePairs) {
            pair = pair.replace("(", "").replace(")", "");
            String[] coords = pair.split(",");
            int[] neighbor = new int[coords.length];
            for (int i = 0; i < coords.length; i++) {
                neighbor[i] = Integer.parseInt(coords[i].trim());
            }
            neighbors.add(neighbor);
        }
        return neighbors;
    }
}
