abstract class TreeNode {
    abstract int getValue();
}

class ConstNode extends TreeNode {
    private final int value;

    public ConstNode(int value) {
        this.value = value;
    }

    @Override
    int getValue() {
        return value;
    }
}

abstract class OperatorNode extends TreeNode {
    protected TreeNode left;
    protected TreeNode middle;
    protected TreeNode right;

    public OperatorNode(TreeNode left, TreeNode middle, TreeNode right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    public OperatorNode(TreeNode left, TreeNode right) {
        this(left, null, right);
    }

    public OperatorNode(TreeNode left) {
        this(left, null, null);
    }

    public OperatorNode() {
        this(null, null, null);
    }

    @Override
    abstract int getValue();
}
