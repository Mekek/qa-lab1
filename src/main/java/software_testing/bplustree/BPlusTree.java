package software_testing.bplustree;

import java.util.*;

public class BPlusTree {

    public final int MAX_KEYS; // максимальное количество ключей в узле

    private Node root; // корень дерева

    // список событий алгоритма
    private final List<String> trace = new ArrayList<>();


    public BPlusTree(int maxKeys) {
        this.MAX_KEYS = maxKeys;
        this.root = new LeafNode(); // изначально дерево состоит из одного листа
    }


    // возвращает копию списка событий алгоритма
    public List<String> getTrace() {
        return new ArrayList<>(trace);
    }

    public void clearTrace() { trace.clear(); }


    // поиск ключа в дереве
    public boolean search(int key) {

        Node n = root;

        // спускаемся по дереву пока не дойдём до листа
        while (!(n instanceof LeafNode)) {
            InternalNode in = (InternalNode)n;
            int idx = in.findChildIndex(key);
            n = in.children.get(idx);
        }

        // теперь мы в листе
        LeafNode leaf = (LeafNode) n;

        boolean found = leaf.keys.contains(key);

        // записываем результат поиска в trace
        trace.add(found ? "SEARCH_FOUND" : "SEARCH_NOT_FOUND");

        return found;
    }


    // вставка ключа в дерево
    public void insert(int key) {

        trace.add("START_INSERT");

        // находим лист, куда нужно вставить ключ
        LeafNode leaf = findLeafForKey(key);

        trace.add("FIND_LEAF");

        // вставляем ключ в лист
        if (leaf.insertIntoLeaf(key)) {

            trace.add("INSERT_IN_LEAF");

            // если лист переполнился — нужно разделить
            if (leaf.keys.size() > MAX_KEYS) {

                trace.add("LEAF_OVERFLOW");

                LeafNode sibling = leaf.split();

                trace.add("LEAF_SPLIT");

                // первый ключ правого узла поднимается
                int promoteKey = sibling.keys.get(0);

                promoteUp(leaf, sibling, promoteKey);
            }
        }
    }


    // поднимает ключ вверх по дереву после split
    private void promoteUp(Node left, Node right, int promoteKey) {

        // если у узла нет родителя — создаём новый корень
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

        // иначе вставляем ключ в родительский узел
        InternalNode parent = (InternalNode) left.parent;

        trace.add("PROMOTE_KEY");

        parent.insertChildAfter(left, right, promoteKey);

        // если родитель тоже переполнился — делаем split
        if (parent.keys.size() > MAX_KEYS) {

            trace.add("INTERNAL_OVERFLOW");

            InternalNode sibling = parent.splitInternal();

            trace.add("INTERNAL_SPLIT");

            int med = parent.keys.remove(parent.keys.size() - 1);

            promoteUp(parent, sibling, med);
        }
    }


    // ищем лист, куда должен попасть ключ
    private LeafNode findLeafForKey(int key) {

        Node n = root;

        while (!(n instanceof LeafNode)) {
            InternalNode in = (InternalNode)n;
            int idx = in.findChildIndex(key);
            n = in.children.get(idx);
        }

        return (LeafNode)n;
    }



    // базовый класс узла дерева
    private abstract class Node {
        InternalNode parent; // ссылка на родительский узел
    }



    // внутренний узел дерева
    private class InternalNode extends Node {

        List<Integer> keys = new ArrayList<>();
        List<Node> children = new ArrayList<>();


        // определяет в какого ребёнка нужно идти при поиске
        int findChildIndex(int key) {

            int idx = 0;

            while (idx < keys.size() && key >= keys.get(idx))
                idx++;

            return idx;
        }


        // вставляет нового ребёнка справа от указанного узла
        void insertChildAfter(Node left, Node right, int promoteKey) {

            // находим позицию левого узла
            int pos = children.indexOf(left);

            if (pos == -1)
                pos = children.size() - 1;

            // вставляем нового ребёнка
            children.add(pos + 1, right);

            // добавляем ключ-разделитель
            keys.add(pos, promoteKey);

            right.parent = this;
        }


        // разделение внутреннего узла
        InternalNode splitInternal() {

            int from = (keys.size() + 1) / 2;

            InternalNode sib = new InternalNode();

            // ключи которые переедут в новый узел
            List<Integer> moveKeys =
                    new ArrayList<>(keys.subList(from, keys.size()));

            // потомки которые переедут
            List<Node> moveChildren =
                    new ArrayList<>(children.subList(from, children.size()));

            // удаляем их из текущего узла
            for (int i = keys.size() - 1; i >= from; i--)
                keys.remove(i);

            while (children.size() > from + 1)
                children.remove(children.size() - 1);

            // переносим в новый узел
            sib.keys.addAll(moveKeys);
            sib.children.addAll(moveChildren);

            // обновляем родителей
            for (Node c : sib.children)
                c.parent = sib;

            sib.parent = this.parent;

            return sib;
        }
    }



    // листовой узел дерева
    private class LeafNode extends Node {

        List<Integer> keys = new ArrayList<>();

        LeafNode next; // ссылка на следующий лист (для B+)


        // вставка ключа в лист
        boolean insertIntoLeaf(int key) {

            // ищем позицию бинарным поиском
            int pos = Collections.binarySearch(keys, key);

            // если ключ уже есть — ничего не делаем
            if (pos >= 0)
                return false;

            int insertPos = -pos - 1;

            keys.add(insertPos, key);

            return true;
        }


        // разделение листа
        LeafNode split() {

            int mid = (keys.size() + 1) / 2;

            LeafNode sibling = new LeafNode();

            // ключи которые переедут в новый лист
            List<Integer> move =
                    new ArrayList<>(keys.subList(mid, keys.size()));

            for (int i = keys.size() - 1; i >= mid; i--)
                keys.remove(i);

            sibling.keys.addAll(move);

            // обновляем связанный список листьев
            sibling.next = this.next;
            this.next = sibling;

            sibling.parent = this.parent;

            return sibling;
        }
    }
}
