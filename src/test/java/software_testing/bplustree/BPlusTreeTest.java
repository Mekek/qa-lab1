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
    public void testInsertDuplicate() {
        BPlusTree tree = new BPlusTree(7);

        tree.insert(5);
        tree.insert(5);

        Assertions.assertTrue(tree.search(5));
    }
}