import java.util.List;
import java.util.Random;

class ET extends OperatorNode {
    public ET(TreeNode left, TreeNode right) {
        super(left, right);
    }

    @Override
    int getValue() {
        return (left.getValue() != 0 && right.getValue() != 0) ? 1 : 0;
    }
}

class OU extends OperatorNode {
    public OU(TreeNode left, TreeNode right) {
        super(left, right);
    }

    @Override
    int getValue() {
        return (left.getValue() != 0 || right.getValue() != 0) ? 1 : 0;
    }
}

class NON extends OperatorNode {
    public NON(TreeNode left) {
        super(left);
    }

    @Override
    int getValue() {
        return (left.getValue() == 0) ? 1 : 0;
    }
}

class SUP extends OperatorNode {
    public SUP(TreeNode left, TreeNode right) {
        super(left, right);
    }

    @Override
    int getValue() {
        return (left.getValue() > right.getValue()) ? 1 : 0;
    }
}

class SUPEQ extends OperatorNode {
    public SUPEQ(TreeNode left, TreeNode right) {
        super(left, right);
    }

    @Override
    int getValue() {
        return (left.getValue() >= right.getValue()) ? 1 : 0;
    }
}

class EQ extends OperatorNode {
    public EQ(TreeNode left, TreeNode right) {
        super(left, right);
    }

    @Override
    int getValue() {
        return (left.getValue() == right.getValue()) ? 1 : 0;
    }
}

class ADD extends OperatorNode {
    public ADD(TreeNode left, TreeNode right) {
        super(left, right);
    }

    @Override
    int getValue() {
        return left.getValue() + right.getValue();
    }
}

class SUB extends OperatorNode {
    public SUB(TreeNode left, TreeNode right) {
        super(left, right);
    }

    @Override
    int getValue() {
        return left.getValue() - right.getValue();
    }
}

class MUL extends OperatorNode {
    public MUL(TreeNode left, TreeNode right) {
        super(left, right);
    }

    @Override
    int getValue() {
        return left.getValue() * right.getValue();
    }
}

class SI extends OperatorNode {
    public SI(TreeNode left, TreeNode middle, TreeNode right) {
        super(left, middle, right);
    }

    @Override
    int getValue() {
        return (left.getValue() != 0) ? middle.getValue() : right.getValue();
    }
}

class COMPTER extends TreeNode {
    private final STRUCT_Grid_ND grid;
    private final List<int[]> neighbors;

    public COMPTER(STRUCT_Grid_ND grid, String voisinage, int... position) {
        this.grid = grid;
        this.neighbors = TOOLS_Neighborhoods.getNeighborhoodByName(voisinage).getNeighbors(position);
    }

    // verifier voisinage en fonction de la grid si en 1D 2D 3D ou plus avec // pas a faire selon m.chausard

    @Override
    int getValue() {
        int liveNeighbors = 0;
        for (int[] neighbor : neighbors) {
            // System.out.println("Neighbor: " + neighbor[0] + ", " + neighbor[1]);
            // System.out.println("Value: " + grid.getCell(neighbor).getCellValue());
            try {
                if (grid.getCell(neighbor).getCellValue() == true) {
                    liveNeighbors++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // System.out.println("Out of bounds");
                // Si on est en dehors, les cellules sont mortes
            }
        }
        return liveNeighbors;
    }
}

class Rule3D {

    public boolean isAlive(STRUCT_Grid_ND grid, int ...position) 
    {
        int x = position[0];
        int y = position[1];
        int z = position[2];
        TreeNode compterG0 = new COMPTER(grid, "G0", x, y, z);
        TreeNode compterG26 = new COMPTER(grid, "G26*", x, y, z);

        TreeNode eqG0_1 = new EQ(compterG0, new ConstNode(1)); // Mort ou vivant
        TreeNode Eq_5 = new EQ(compterG26, new ConstNode(5)); // 5 voisins
        TreeNode Eq_6 = new EQ(compterG26, new ConstNode(6)); // 6 voisins
        TreeNode Eq_4 = new EQ(compterG26, new ConstNode(4)); // 4 voisins
        TreeNode Ou_2 = new OU(Eq_5, Eq_6); // 2 ou 3 voisins

        TreeNode siSupEqG8_4 = new SI(Ou_2, new ConstNode(1), new ConstNode(0)); // 5 ou 6 voisins ? 1 : 0
        TreeNode siEqG8_2 = new SI(Eq_4, new ConstNode(1), new ConstNode(0)); // 4 voisins ? 1 : 0

        TreeNode mainSi = new SI(eqG0_1, siSupEqG8_4, siEqG8_2);

        return mainSi.getValue() == 1;
    }
}

class Rule2D {

    public boolean isAlive(STRUCT_Grid_ND grid, int ...position) 
    {
        int x = position[0];
        int y = position[1];
        TreeNode compterG0 = new COMPTER(grid, "G0", x, y);
        TreeNode compterG8 = new COMPTER(grid, "G8*", x, y);
        
        TreeNode eqG0_1 = new EQ(compterG0, new ConstNode(1)); // Mort ou vivant
        TreeNode EqG8_3 = new EQ(compterG8, new ConstNode(3)); // 3 voisins
        TreeNode EqG8_2 = new EQ(compterG8, new ConstNode(2)); // 2 voisins
        TreeNode Ou_2 = new OU(EqG8_2, EqG8_3); // 2 ou 3 voisins

        TreeNode siSupEqG8_4 = new SI(Ou_2, new ConstNode(1), new ConstNode(0)); // 2 ou 3 voisins ? 1 : 0
        TreeNode siEqG8_2 = new SI(EqG8_3, new ConstNode(1), new ConstNode(0)); // 3 voisins ? 1 : 0

        TreeNode mainSi = new SI(eqG0_1, siSupEqG8_4, siEqG8_2);

        return mainSi.getValue() == 1;
    }
}

public class TOOLS_Rules {

    public static int evaluate(TreeNode tree) {
        return tree.getValue();
    }

    public static void main(String[] args) {
        int rows = 11;
        int cols = 11;
        Random random = new Random();
        STRUCT_Grid_ND grid = new STRUCT_Grid_ND(rows, cols);

        for (int i = 0; i < rows * cols * 0.25; i++) {
            grid.getCell(random.nextInt(rows), random.nextInt(cols)).setCellValue(true);
        }
       
        int[] position = { 5, 5 };

        // Using COMPTER with the G8 neighborhood
        TreeNode compterNode = new COMPTER(grid, "G4", position); // pb voisinage si pas 2d

        int result = evaluate(compterNode);
        System.out.println("Result: " + result);

        ConstNode val1 = new ConstNode(2);
        ConstNode val2 = new ConstNode(2);
        ConstNode val3 = new ConstNode(3);
        TreeNode node = new SI(val1, val2, val3);
        result = evaluate(node);
        System.out.println("Result: " + result);
    }
}
