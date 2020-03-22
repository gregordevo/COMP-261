import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * generic trie tree that will take any collection and returns all
 * elements corresponding to a substring
 * @param <E>
 * @param <K>
 */

public class Trie<E, K> {
    private TrieNode root;

    private class TrieNode {

        private boolean isLeaf;
        private HashMap<Character, TrieNode> children = new HashMap<>();
        E element = null;

        TrieNode() {
            isLeaf = false;
        }

        TrieNode(char c) {
            isLeaf = false;
         }

        private ArrayList<E> listElements( ) {
            ArrayList<E> returnList = new ArrayList<>();
            if (isLeaf) returnList.add(element);
            if (children.isEmpty()) return returnList;
            for (TrieNode child : children.values()) {
                ArrayList<E> endNodes = child.listElements( );
                returnList.addAll(endNodes);
            }
            return returnList;
        }
    }


    public void insert(E element, String key) {

        TrieNode iterator = root;
        for (char character : key.toCharArray()) {
            if (iterator.children.containsKey(character)) iterator = iterator.children.get(character);
            else {
                iterator.children.put(character, new TrieNode(character));
                iterator = iterator.children.get(character);
            }
        }
        iterator.element = element;
        iterator.isLeaf = true;

    }

    public ArrayList<E> listElements(K key) {
        TrieNode iterator = root;
        for (char character : key.toString().toCharArray()) {
            if (iterator.children.get(character) == null) {
                return new ArrayList<>();
            }
            iterator = iterator.children.get(character);
        }
        ArrayList<E> returnedList = iterator.listElements( );
        return returnedList;

    }


    Trie(Collection<E> elements) {
        root = new TrieNode();

        for (E e : elements) {
            insert(e, e.toString());
        }
    }

}