package core.fpGrowth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atomsk on 21/04/2016.
 */
public class Node {

    private Node father;
    private List<Node> suns;
    private List<Node> me;
    private Pair<String, Integer> value;

    // Only used to create a root node
    private Node(){
        father = null;
        suns = new ArrayList<>();
        me = null;
        value = null;
    }

    public Pair<String, Integer> getValue() {
        return value;
    }

    public Node(Node father, Pair<String, Integer> value){
        this.father = father;
        suns = new ArrayList<>();
        me = null;
        this.value = value;
    }

    public static Node createNode(){
        return new Node();
    }

    public void addSun(String name){
        suns.add(new Node(this, new Pair<>(name, 1)));
    }

    public boolean containsSon(String Name){
        boolean find = false;
        int counter = 0;

        while(!find){
            find = suns.get(counter).getValue().getLeft().equals(Name);
            counter ++;
        }

        return find;
    }

    public Node takeSun(String Name){
        int counter = 0;
        boolean find = false;


        while(!find){
            find = suns.get(counter).getValue().getLeft().equals(Name);
            counter ++;
        }

        return suns.get(counter - 1);
    }
}
