import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class FamilyTree_Amplify {

    public static void main(String[] args) {
        System.out.println(common("{Donna:[Valrie,Monica],Claire:[Jessica,Britney,Jenna],Betty:[Claire, Donna]}", "Claire", "Donna"));
    }

    //The first step would be to convert the json object into a tree


    public static String common(String tree_str, String name1, String name2) {





        return  convertStringToTree(tree_str,name1,name2);


    }


    public static String convertStringToTree(String tree_str, String name1, String name2) {

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(tree_str);

        JsonObject object = element.getAsJsonObject();


        HashMap<String, TreeNode> parentMap = new HashMap<String, TreeNode>();
        HashMap<String, TreeNode> childrenMap = new HashMap<String, TreeNode>();


        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String key = entry.getKey();
            JsonArray jsonArray = entry.getValue().getAsJsonArray();

            TreeNode parent = new TreeNode(key);
            ArrayList<TreeNode> children = new ArrayList<TreeNode>();
            for (JsonElement child : jsonArray) {
                TreeNode json_child = new TreeNode(child.getAsString().replaceAll("\"", ""));
                children.add(json_child);
                childrenMap.put(json_child.getName(), json_child);
                json_child.parent = parent;
            }
            parent.setChildren(children);
            parentMap.put(parent.getName(), parent);


        }


        // Replace the duplicates
        // Get all parents
        Set<String> parentNames = parentMap.keySet();
        Set<String> childrenNames = childrenMap.keySet();
        TreeNode root=null;

        for (String parentName : parentNames) {
            if ( childrenNames.contains(parentName)) {

                // This parent is also a child, but that means you have 2 separate nodes for same name, one for parent, one for child
                // You need to eliminate one of them and make proper links. Will eliminate “parent”
                TreeNode parent = parentMap.get(parentName);
                TreeNode childSameAsParent = childrenMap.get(parentName);
                // We move all children to only one node
                ArrayList<TreeNode> children = new ArrayList<TreeNode>();

                for ( TreeNode child: parent.children) {
                    // Make new links
                    child.parent =childSameAsParent;
                    children.add(child);

                }
                childSameAsParent.setChildren(children);

            }
        }
        ancestor(childrenMap.get(name1),childrenMap.get(name2));

        return ancestor(childrenMap.get(name1),childrenMap.get(name2)).getName();
    }



    private static TreeNode ancestor(TreeNode name1, TreeNode name2){
        ArrayList<TreeNode> parentTree1=new ArrayList<TreeNode>();

        while (name1!=null){
            parentTree1.add(name1);
            name1=name1.parent;
        }

        ArrayList<TreeNode> parentTree2=new ArrayList<TreeNode>();

        while (name2!=null){
            parentTree2.add(name2);
            name2=name2.parent;
        }

        TreeNode oldTree=null;
        while (name1==name2 && !parentTree1.isEmpty()&& !parentTree2.isEmpty()){
            oldTree=name1;
            name1=parentTree1.remove(parentTree1.size()-1);
            name2=parentTree2.remove(parentTree2.size()-1);
        }
        if (name1==name2){
            return name1;
        }
        else{
            return oldTree;
        }

    }

    private static void showTree(TreeNode treeNode) {
        System.out.println("Node: " + treeNode.name);
        if ( treeNode.parent == null) {
            System.out.println("I am ROOT " );

        } else {
            System.out.println("My Parent is: " + treeNode.parent.name);
        }
        if ( treeNode.children != null ) {
            for (TreeNode child : treeNode.children) {
                showTree(child);
            }
        } else {
            return;
        }
    }
}


class TreeNode {
    ArrayList<TreeNode> children;
    TreeNode parent = null;
    String name;


    public TreeNode (String name){
        this.name=name;

    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }
}