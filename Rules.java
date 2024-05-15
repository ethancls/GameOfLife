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
    private final Grid_ND grid;
    private final List<int[]> neighbors;

    public COMPTER(Grid_ND grid, String voisinage, int... position) {
        this.grid = grid;
        this.neighbors = Neighborhoods.getNeighborhoodByName(voisinage).getNeighbors(position);
    }

    
    // verifier voisinage en fonction de la grid si en 1D 2D 3D ou plus avec
    // grid.getDimensions().lenght!!
    // out of bounds exception

    @Override
    int getValue() {
        int liveNeighbors = 0;
        for (int[] neighbor : neighbors) {
            // System.out.println("Neighbor: " + neighbor[0] + ", " + neighbor[1]);
            // System.out.println("Value: " + grid.getCell(neighbor).getCellValue());
            if (grid.getCell(neighbor).getCellValue() == true) {
                liveNeighbors++;
            }
        }
        return liveNeighbors;
    }
}

public class Rules {

    public static int evaluate(TreeNode tree) {
        return tree.getValue();
    }

    public static void main(String[] args) {
        int rows = 11;
        int cols = 11;
        Random random = new Random();
        Grid_ND grid = new Grid_ND(rows, cols);

        for (int i = 0; i < rows * cols * 0.25; i++) {
            grid.getCell(random.nextInt(rows), random.nextInt(cols)).setValue(true);
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
