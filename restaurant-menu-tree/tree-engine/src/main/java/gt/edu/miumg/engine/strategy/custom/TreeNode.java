package gt.edu.miumg.engine.strategy.custom;

public class TreeNode {

    private String id;
    private String value;
    private String parentId;
    private TreeNode[] children;
    private int childCount;
    private static final int MAX_CHILDREN = 100;

    public TreeNode(String id, String value, String parentId) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;
        this.children = new TreeNode[MAX_CHILDREN];
        this.childCount = 0;
    }

    public void addChild(TreeNode child) {
        if (childCount < MAX_CHILDREN) {
            children[childCount] = child;
            childCount++;
        }
    }

    public TreeNode[] getChildren() { return children; }
    public int getChildCount() { return childCount; }
    public String getId() { return id; }
    public String getValue() { return value; }
    public String getParentId() { return parentId; }
}