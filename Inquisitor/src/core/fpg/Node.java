package core.fpg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by klamblot on 21/04/2016.
 * Class created to managed node of the FPTree
 */
public class Node {

    private Node father;
    private List<Node> suns;
    private Pair<String, Integer> value;

    // Constructor
    // Only used to create a root node
    private Node(){
        father = null;
        suns = new ArrayList<>();
        value = new Pair<>("root", -1);
    }

    public Node(Node father, Pair<String, Integer> value){
        this.father = father;
        suns = new ArrayList<>();
        this.value = value;
    }



    public Pair<String, Integer> getValue() {
        return value;
    }

    public static Node createRootNode(){
        return new Node();
    }


    // Method to managed the node and the tree
    // add a sun to the node
    public void addSun(String name){
        suns.add(new Node(this, new Pair<>(name, 1)));
    }


    // Method to check if contains direct suns
    public boolean containDirectSon(String Name){
        boolean find = false;
        int counter = 0;

        while(!find && suns.size() > counter){
            find = suns.get(counter).getValue().getLeft().equals(Name);
            counter ++;
        }

        return find;
    }

    // Method to return the direct Node of a sun
    public Node takeDirectSun(String Name){
        int counter = 0;
        boolean find = false;

        while(!find && suns.size() > counter){
            find = suns.get(counter).getValue().getLeft().equals(Name);
            counter ++;
        }

        return suns.get(counter - 1);
    }

    public void getAllAscendant(Set<String> parentWords){
        if(!father.getValue().getLeft().equals("root")){
            parentWords.add(father.getValue().getLeft());
            father.getAllAscendant(parentWords);
        }
    }
    // Method to check if this has a parent named name
    public boolean hasParent(String name){
        return (this.father.getValue().getLeft().equals(name) || (!this.father.getValue().getLeft().equals("root") && this.father.hasParent(name)));
    }
}
