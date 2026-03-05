package software_testing.bplustree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BPlusTreeTest {

    @Test
    public void testSimpleInsertNoSplit() {
        BPlusTree tree = new BPlusTree(7);
        tree.clearTrace();
        tree.insert(10);
        List<String> t = tree.getTrace();
        Assertions.assertEquals(List.of("START_INSERT","FIND_LEAF","INSERT_IN_LEAF"), t);
    }

    @Test
    public void testLeafSplitSequenceSmallOrder() {
        // для контроля используем порядок 3, чтобы быстро получить split
        BPlusTree tree = new BPlusTree(3);
        for (int k : new int[]{1,2,3}) tree.insert(k);
        tree.clearTrace();
        tree.insert(4);
        List<String> t = tree.getTrace();
        // ожидаем: старт, поиск листа, вставка, переполнение, разделение, продвижение ключа и (потенциально) root split
        Assertions.assertTrue(t.contains("LEAF_OVERFLOW"));
        Assertions.assertTrue(t.contains("LEAF_SPLIT"));
        Assertions.assertTrue(t.contains("PROMOTE_KEY") || t.contains("PROMOTE_ROOT"));
    }

    @Test
    public void testSearchFoundNotFound() {
        BPlusTree tree = new BPlusTree(7);
        tree.insert(5);
        tree.insert(7);
        boolean f = tree.search(5);
        Assertions.assertTrue(f);
        List<String> t = tree.getTrace();
        Assertions.assertTrue(t.contains("SEARCH_FOUND"));
        tree.clearTrace();
        boolean nf = tree.search(100);
        Assertions.assertFalse(nf);
        Assertions.assertTrue(tree.getTrace().contains("SEARCH_NOT_FOUND"));
    }

    @Test
    public void testInsertDuplicateKeyBranch() {
        BPlusTree tree = new BPlusTree(7);
        tree.insert(10);

        tree.clearTrace();
        tree.insert(10); // повторная вставка

        List<String> t = tree.getTrace();

        Assertions.assertTrue(t.contains("START_INSERT"));
        Assertions.assertTrue(t.contains("FIND_LEAF"));
        // INSERT_IN_LEAF не должно появиться
        Assertions.assertFalse(t.contains("INSERT_IN_LEAF"));
    }

    @Test
    public void testRootSplitBranch() {
        BPlusTree tree = new BPlusTree(3);

        tree.insert(1);
        tree.insert(2);
        tree.insert(3);

        tree.clearTrace();
        tree.insert(4); // вызовет split корня

        List<String> t = tree.getTrace();

        Assertions.assertTrue(t.contains("LEAF_OVERFLOW"));
        Assertions.assertTrue(t.contains("LEAF_SPLIT"));
        Assertions.assertTrue(t.contains("PROMOTE_ROOT"));
        Assertions.assertTrue(t.contains("ROOT_SPLIT"));
    }

    @Test
    public void testPromoteKeyBranch() {
        BPlusTree tree = new BPlusTree(3);

        // создаем структуру с внутренним узлом
        for (int k : new int[]{1,2,3,4}) {
            tree.insert(k);
        }

        tree.clearTrace();
        tree.insert(5);

        List<String> t = tree.getTrace();

        Assertions.assertTrue(t.contains("PROMOTE_KEY"));
    }

    @Test
    public void testInternalOverflowBranch() {
        BPlusTree tree = new BPlusTree(3);

        for (int i = 1; i <= 10; i++) {
            tree.insert(i);
        }

        tree.clearTrace();
        tree.insert(11);

        List<String> t = tree.getTrace();

        Assertions.assertTrue(t.contains("INTERNAL_OVERFLOW") || t.contains("INTERNAL_SPLIT"));
    }

    @Test
    public void testInsertIntoDifferentLeaves() {
        BPlusTree tree = new BPlusTree(3);

        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);

        tree.clearTrace();
        tree.insert(25);

        List<String> t = tree.getTrace();

        Assertions.assertTrue(t.contains("START_INSERT"));
        Assertions.assertTrue(t.contains("FIND_LEAF"));
    }
}