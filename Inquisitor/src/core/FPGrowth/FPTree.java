package core.fpGrowth;

/**
 * Class used to managed the fpGrowth tree
 */
public class FPTree {
    private Node root;

    public FPTree(){
        root = Node.createRootNode();
    }

    public Node getRoot(){
        return root;
    }
}
