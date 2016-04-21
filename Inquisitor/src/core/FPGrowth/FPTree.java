package core.FPGrowth;

/**
 * Class used to managed the FPGrowth tree
 */
public class FPTree {
    private Node root;

    public FPTree(){
        root = Node.createNode();
    }

    public Node getRoot(){
        return root;
    }
}
