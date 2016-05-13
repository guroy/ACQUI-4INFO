package core.fpg;

/**
 * Class used to managed the fpGrowth tree
 * nothing in it because practically useless
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
