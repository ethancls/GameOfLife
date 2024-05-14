import java.util.ArrayList;
import java.util.List;

public class Neighborhood extends Grid_ND
{

    private List<int[]> offsets;
    private boolean includeCenter;

    public Neighborhood(List<int[]> offsets, boolean includeCenter) {
        this.offsets = offsets;
        this.includeCenter = includeCenter;
    }

    public List<int[]> getOffsets() {
        return offsets;
    }

    public boolean isIncludeCenter() {
        return includeCenter;
    }

    // Méthode pour obtenir les coordonnées des voisins d'une cellule donnée
    public List<int[]> getNeighbors(int[] position) {
        List<int[]> neighbors = new ArrayList<>();
        for (int[] offset : offsets) {
            int[] neighborPos = new int[position.length];
            for (int i = 0; i < position.length; i++) {
                neighborPos[i] = position[i] + offset[i];
            }
            neighbors.add(neighborPos);
        }
        if (includeCenter) {
            neighbors.add(position);
        }
        return neighbors;
    }

    public static Neighborhood G0() {
        List<int[]> offsets = new ArrayList<>();
        offsets.add(new int[]{0});
        return new Neighborhood(offsets, true);
    }

    public static Neighborhood G2() {
        List<int[]> offsets = new ArrayList<>();
        offsets.add(new int[]{0});
        offsets.add(new int[]{1});
        offsets.add(new int[]{-1});
        return new Neighborhood(offsets, true);
    }

    public static Neighborhood G4() {
        List<int[]> offsets = new ArrayList<>();
        offsets.add(new int[]{0, 1});
        offsets.add(new int[]{0, -1});
        offsets.add(new int[]{1, 0});
        offsets.add(new int[]{-1, 0});
        return new Neighborhood(offsets, true);
    }

    public static Neighborhood G8() {
        List<int[]> offsets = new ArrayList<>();
        offsets.add(new int[]{0, 1});
        offsets.add(new int[]{0, -1});
        offsets.add(new int[]{1, 0});
        offsets.add(new int[]{-1, 0});
        offsets.add(new int[]{1, 1});
        offsets.add(new int[]{1, -1});
        offsets.add(new int[]{-1, 1});
        offsets.add(new int[]{-1, -1});
        return new Neighborhood(offsets, true);
    }

    public static Neighborhood G6() {
        List<int[]> offsets = new ArrayList<>();
        offsets.add(new int[]{0, 0, 1});
        offsets.add(new int[]{0, 0, -1});
        offsets.add(new int[]{0, 1, 0});
        offsets.add(new int[]{0, -1, 0});
        offsets.add(new int[]{1, 0, 0});
        offsets.add(new int[]{-1, 0, 0});
        return new Neighborhood(offsets, true);
    }

    public static Neighborhood G26() {
        List<int[]> offsets = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (!(x == 0 && y == 0 && z == 0)) {
                        offsets.add(new int[]{x, y, z});
                    }
                }
            }
        }
        return new Neighborhood(offsets, true);
    }
}

class TestNeighborhood {
    public static void main(String[] args) {
        Neighborhood neighborhood = Neighborhood.G0();
        int[] position = {0, 0};
        List<int[]> neighbors = neighborhood.getNeighbors(position);
        System.out.println("Neighbors of position (" + position[0] + ", " + position[1] + "):");
        for (int[] neighbor : neighbors) {
            System.out.println("(" + neighbor[0] + ", " + neighbor[1] + ")");
        }
    }
}