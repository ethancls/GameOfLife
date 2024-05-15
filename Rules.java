import java.util.List;
import java.util.Random;
import java.util.ArrayList;

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

    public COMPTER(Grid_ND grid, String voisinage, int[] position) {
        this.grid = grid;
        this.neighbors = Neighborhoods.getNeighborhoodByName("G2").getNeighbors(position);
    }

    @Override
    int getValue() {
        int liveNeighbors = 0;
        for (int[] neighbor : neighbors) {
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
        int rows = 5;
        int cols = 5;
        Random random = new Random();
        Grid_ND grid = new Grid_ND(rows, cols);

        for (int i = 0; i < rows*cols*0.25; i++) 
        {
            grid.getCell(random.nextInt(rows), random.nextInt(cols)).setValue(true);
        }
        GrilleGraphique Grid_2D = new GrilleGraphique(grid.getDimensions()[0], grid.getDimensions()[1], 12);

        int i, j;
        for (i = 0; i < rows; i++) {
            for (j = 0; j < cols; j++) 
            {
                if (grid.getCell(i,j).getCellValue()) 
                {
                    Grid_2D.colorierCase(i, j);
                }
            }
        }

        int[] position = {3, 3};

        // Using COMPTER with the G8 neighborhood
        TreeNode compterNode = new COMPTER(grid, "G0", position);
        TreeNode eqNode = new EQ(compterNode, new ConstNode(2));
        TreeNode siNode = new SI(eqNode, new ConstNode(1), new ConstNode(0));

        int result = evaluate(siNode);
        System.out.println("Result: " + result);
    }
}


/*
 * Fonction recursive
 * public getValue(Tree t) // bien mettre val 1 en noeud gauche et val 2 en
 * noeud droit
 * {
 * if(leaf is int)
 * {
 * return leaf;
 * }
 * else if (leaf is operator)
 * {
 * if(operator is ET)
 * {
 * if(getValue(left) != 0 && getValue(right) != 0)
 * {
 * return 1;
 * }
 * else
 * {
 * return 0;
 * }
 * }
 * else if(operator is OU)
 * {
 * if(getValue(left) != 0 || getValue(right) != 0)
 * {
 * return 1;
 * }
 * else
 * {
 * return 0;
 * }
 * }
 * else if(operator is NON)
 * {
 * if(getValue(left) == 0) // Val1 node left
 * {
 * return 1;
 * }
 * else
 * {
 * return 0;
 * }
 * }
 * else if(operator is SUP)
 * {
 * if(getValue(left) > getValue(right))
 * {
 * return 1;
 * }
 * else
 * {
 * return 0;
 * }
 * }
 * else if(operator is SUPEQ)
 * {
 * if(getValue(left) >= getValue(right))
 * {
 * return 1;
 * }
 * else
 * {
 * return 0;
 * }
 * }
 * else if(operator is EQ)
 * {
 * if(getValue(left) == getValue(right))
 * {
 * return 1;
 * }
 * else
 * {
 * return 0;
 * }
 * }
 * else if(operator is COMPTER)
 * {
 * if(getValue(left) or getValue(right) is voisinage)
 * {
 * return nb de cellules voisines a l'état 1;
 * }
 * }
 * else if(operator is ADD)
 * {
 * return getValue(left) + getValue(right);
 * }
 * else if(operator is SUB)
 * {
 * return getValue(left) - getValue(right);
 * }
 * else if(operator is MUL)
 * {
 * return getValue(left) * getValue(right);
 * }
 * else if(operator is SI) // 3 enfants car 3 valeurs
 * {
 * if(getValue(left) != 0)
 * {
 * return getValue(middle);
 * }
 * else
 * {
 * return getValue(right);
 * }
 * }
 * }
 * }
 */
