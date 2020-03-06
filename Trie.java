import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trie {
    static TrieNode root;
    public class TrieNode {


        String value;
        HashMap<Integer, TrieNode> children = new HashMap<>();
        Stop stopNode = null;
        Boolean isLeaf;

        TrieNode() {
            isLeaf = false;
        }


        public ArrayList<Stop> findAllStops(String key) {
            ArrayList<Stop> result = new ArrayList<>();
            if (children.isEmpty()) {
                result.add(stopNode);
                return result;
            } else {
                for (TrieNode child : children.values()) {
                    child.value = this.value + child.value;
                }
                for (TrieNode child : children.values()) {
                    List<Stop> childStops = child.findAllStops(key);
                    result.addAll(childStops);
                }
            }

            return result;
        }
    }
        public void insertKey(String key, Stop stop) {
            TrieNode iterator = root;
            int level = 0;
            int index;
            for (char c : key.toCharArray()) {
                if (key.charAt(level) != ' ' || key.charAt(level) != '-') {
                    index = key.charAt(level++) - 'a';
                } else if (key.charAt(level) != ' ') {
                    index = 27;
                } else {
                    index = 28;
                }
                if (iterator.children.get(index) == null) {
                    iterator.children.put(index, new TrieNode());
                    iterator.children.get(index).value = String.valueOf((char) ('a' + index));
                }
                iterator = iterator.children.get(index);
            }
            iterator.stopNode = stop;
            iterator.isLeaf = true;
        }


        public ArrayList<Stop> findAllNode(String key) {
            TrieNode iterator = root;
            int level = 0;

            for (char c : key.toCharArray()) {
                int index = key.charAt(level++) - 'a';
                if (iterator.children.get(index) == null) {
                    return new ArrayList<>();
                }
                iterator = iterator.children.get(index);
            }
            ArrayList<Stop> returnedList = iterator.findAllStops(key);
            return returnedList;

        }


        Trie(ArrayList<Stop> stops) {
            root = new TrieNode();

            for (Stop stop : stops) {
                insertKey(stop.getName(), stop);
            }
        }

}
