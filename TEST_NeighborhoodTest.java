import java.io.IOException;
import java.util.List;

public class TEST_NeighborhoodTest {
    public static void main(String[] args) {
        try {
            testNeighborhoods();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testNeighborhoods() throws IOException {
        // Define a position to test the neighborhoods
        int[] position1D = {2};
        int[] position2D = {2, 2};
        int[] position3D = {2, 2, 2};

        System.out.println("Testing predefined neighborhoods:");
        printNeighbors("G0", TOOLS_Neighborhoods.getNeighborhoodByName("G0").getNeighbors(position1D));
        printNeighbors("G2", TOOLS_Neighborhoods.getNeighborhoodByName("G2").getNeighbors(position1D));
        printNeighbors("G2*", TOOLS_Neighborhoods.getNeighborhoodByName("G2*").getNeighbors(position1D));

        printNeighbors("G4", TOOLS_Neighborhoods.getNeighborhoodByName("G4").getNeighbors(position2D));
        printNeighbors("G8", TOOLS_Neighborhoods.getNeighborhoodByName("G8").getNeighbors(position2D));
        printNeighbors("G4*", TOOLS_Neighborhoods.getNeighborhoodByName("G4*").getNeighbors(position2D));
        printNeighbors("G8*", TOOLS_Neighborhoods.getNeighborhoodByName("G8*").getNeighbors(position2D));

        printNeighbors("G6", TOOLS_Neighborhoods.getNeighborhoodByName("G6").getNeighbors(position3D));
        printNeighbors("G26", TOOLS_Neighborhoods.getNeighborhoodByName("G26").getNeighbors(position3D));
        printNeighbors("G6*", TOOLS_Neighborhoods.getNeighborhoodByName("G6*").getNeighbors(position3D));
        printNeighbors("G26*", TOOLS_Neighborhoods.getNeighborhoodByName("G26*").getNeighbors(position3D));

        System.out.println("\nAdding and testing custom neighborhoods from file:");
        TOOLS_NeighborhoodParser.parseFile("ex.txt");
        
        printNeighbors("G32", TOOLS_Neighborhoods.getNeighborhoodByName("G32").getNeighbors(position3D));
        
        System.out.println("\nNeighborhoods overview:");
        System.out.println(new TOOLS_Neighborhoods().toString());
    }

    private static void printNeighbors(String neighborhoodName, List<int[]> neighbors) {
        System.out.println(neighborhoodName + " neighbors:");
        for (int[] neighbor : neighbors) {
            System.out.print("[");
            for (int i = 0; i < neighbor.length; i++) {
                System.out.print(neighbor[i]);
                if (i < neighbor.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
        System.out.println();
    }
}
