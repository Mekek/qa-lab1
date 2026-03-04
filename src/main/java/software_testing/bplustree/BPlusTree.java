package software_testing.bplustree;

import java.util.*;

public class BPlusTree {
    public final int MAX_KEYS; // например 7
    private Node root;
    private final List<String> trace = new ArrayList<>();

    public BPlusTree(int maxKeys) {
        this.MAX_KEYS = maxKeys;
        this.root = new LeafNode();
    }

    public List<String> getTrace() {
        return new ArrayList<>(trace);
    }

    public void clearTrace() { trace.clear(); }

    public boolean search(int key) {
        Node n = root;
        while (!(n instanceof LeafNode)) {
            InternalNode in = (InternalNode)n;
            int idx = in.findChildIndex(key);
            n = in.children.get(idx);
        }
        LeafNode leaf = (LeafNode) n;
        boolean found = leaf.keys.contains(key);
        trace.add(found ? "SEARCH_FOUND" : "SEARCH_NOT_FOUND");
        return found;
    }

    public void insert(int key) {
        trace.add("START_INSERT");
        LeafNode leaf = findLeafForKey(key);
        trace.add("FIND_LEAF");
        if (leaf.insertIntoLeaf(key)) {
            trace.add("INSERT_IN_LEAF");
            if (leaf.keys.size() > MAX_KEYS) {
                trace.add("LEAF_OVERFLOW");
                LeafNode sibling = leaf.split();
                trace.add("LEAF_SPLIT");
                int promoteKey = sibling.keys.get(0);
                promoteUp(leaf, sibling, promoteKey);
            }
        }
    }

    private void promoteUp(Node left, Node right, int promoteKey) {
        if (left.parent == null) {
            trace.add("PROMOTE_ROOT");
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(promoteKey);
            newRoot.children.add(left);
            newRoot.children.add(right);
            left.parent = newRoot;
            right.parent = newRoot;
            root = newRoot;
            trace.add("ROOT_SPLIT");
            return;
        }
        InternalNode parent = (InternalNode) left.parent;
        trace.add("PROMOTE_KEY");
        parent.insertChildAfter(left, right, promoteKey);
        if (parent.keys.size() > MAX_KEYS) {
            trace.add("INTERNAL_OVERFLOW");
            InternalNode sibling = parent.splitInternal();
            trace.add("INTERNAL_SPLIT");
            int med = parent.keys.remove(parent.keys.size() - 1);
            // Note: for simplicity we promote last key of left to parent-of-parent
            promoteUp(parent, sibling, med);
        }
    }

    private LeafNode findLeafForKey(int key) {
        Node n = root;
        while (!(n instanceof LeafNode)) {
            InternalNode in = (InternalNode)n;
            int idx = in.findChildIndex(key);
            n = in.children.get(idx);
        }
        return (LeafNode)n;
    }

    // Node classes
    private abstract class Node {
        InternalNode parent;
    }

    private class InternalNode extends Node {
        List<Integer> keys = new ArrayList<>();
        List<Node> children = new ArrayList<>();

        int findChildIndex(int key) {
            int idx = 0;
            while (idx < keys.size() && key >= keys.get(idx)) idx++;
            return idx;
        }

        void insertChildAfter(Node left, Node right, int promoteKey) {
            // find position of left
            int pos = children.indexOf(left);
            if (pos == -1) pos = children.size() - 1;
            children.add(pos + 1, right);
            keys.add(pos, promoteKey);
            right.parent = this;
        }

        InternalNode splitInternal() {
            int from = (keys.size() + 1) / 2;
            InternalNode sib = new InternalNode();
            // move keys and children
            List<Integer> moveKeys = new ArrayList<>(keys.subList(from, keys.size()));
            List<Node> moveChildren = new ArrayList<>(children.subList(from + 0, children.size()));
            // remove from this
            for (int i = keys.size() - 1; i >= from; i--) keys.remove(i);
            while (children.size() > from + 1) children.remove(children.size() - 1);
            // assign to sibling
            sib.keys.addAll(moveKeys);
            sib.children.addAll(moveChildren);
            for (Node c : sib.children) c.parent = sib;
            sib.parent = this.parent;
            return sib;
        }
    }

    private class LeafNode extends Node {
        List<Integer> keys = new ArrayList<>();
        LeafNode next;

        boolean insertIntoLeaf(int key) {
            int pos = Collections.binarySearch(keys, key);
            if (pos >= 0) return false; // already present: do nothing
            int insertPos = -pos - 1;
            keys.add(insertPos, key);
            return true;
        }

        LeafNode split() {
            int mid = (keys.size() + 1) / 2;
            LeafNode sibling = new LeafNode();
            List<Integer> move = new ArrayList<>(keys.subList(mid, keys.size()));
            for (int i = keys.size() - 1; i >= mid; i--) keys.remove(i);
            sibling.keys.addAll(move);
            sibling.next = this.next;
            this.next = sibling;
            sibling.parent = this.parent;
            return sibling;
        }
    }
}