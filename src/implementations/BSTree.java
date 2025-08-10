package implementations;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import utilities.BSTreeADT;
import utilities.Iterator;

public class BSTree<E extends Comparable<? super E>>
        implements BSTreeADT<E>, Serializable {

    private static final long serialVersionUID = 1L;

    private BSTreeNode<E> root;
    private int size;

    // ----- Constructors -----
    public BSTree() {
        /* empty */ }

    public BSTree(E rootElement) {
        if (rootElement == null)
            throw new NullPointerException("rootElement is null");
        this.root = new BSTreeNode<>(rootElement);
        this.size = 1;
    }

    // ----- ADT methods -----
    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null)
            throw new NullPointerException("empty tree");
        return root;
    }

    @Override
    public int getHeight() {
        return height(root);
    }

    private int height(BSTreeNode<E> n) {
        if (n == null)
            return 0; // empty=0, single node=1 (matches tests)
        int lh = height(n.getLeft());
        int rh = height(n.getRight());
        return 1 + (lh >= rh ? lh : rh);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean contains(E entry) throws NullPointerException {
        if (entry == null)
            throw new NullPointerException("entry is null");
        return search(entry) != null;
    }

    @Override
    public BSTreeNode<E> search(E entry) throws NullPointerException {
        if (entry == null)
            throw new NullPointerException("entry is null");
        BSTreeNode<E> cur = root;
        while (cur != null) {
            int cmp = entry.compareTo(cur.getElement());
            if (cmp == 0)
                return cur;
            cur = (cmp < 0) ? cur.getLeft() : cur.getRight();
        }
        return null;
    }

    @Override
    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null)
            throw new NullPointerException("newEntry is null");
        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size = 1;
            return true;
        }
        BSTreeNode<E> parent = null;
        BSTreeNode<E> cur = root;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = newEntry.compareTo(cur.getElement());
            if (cmp == 0)
                return false; // no duplicates
            cur = (cmp < 0) ? cur.getLeft() : cur.getRight();
        }
        BSTreeNode<E> node = new BSTreeNode<>(newEntry);
        if (cmp < 0)
            parent.setLeft(node);
        else
            parent.setRight(node);
        size++;
        return true;
    }

    @Override
    public BSTreeNode<E> removeMin() {
        if (root == null)
            return null;
        if (root.getLeft() == null) {
            BSTreeNode<E> removed = root;
            root = root.getRight();
            removed.setRight(null);
            size--;
            return removed;
        }
        BSTreeNode<E> parent = root;
        BSTreeNode<E> cur = root.getLeft();
        while (cur.getLeft() != null) {
            parent = cur;
            cur = cur.getLeft();
        }
        if (cur.getRight() != null)
            parent.setLeft(cur.getRight());
        else
            parent.setLeft(null);
        cur.setRight(null);
        size--;
        return cur;
    }

    @Override
    public BSTreeNode<E> removeMax() {
        if (root == null)
            return null;
        if (root.getRight() == null) {
            BSTreeNode<E> removed = root;
            root = root.getLeft();
            removed.setLeft(null);
            size--;
            return removed;
        }
        BSTreeNode<E> parent = root;
        BSTreeNode<E> cur = root.getRight();
        while (cur.getRight() != null) {
            parent = cur;
            cur = cur.getRight();
        }
        if (cur.getLeft() != null)
            parent.setRight(cur.getLeft());
        else
            parent.setRight(null);
        cur.setLeft(null);
        size--;
        return cur;
    }

    // ----- Iterators -----
    @Override
    public Iterator<E> inorderIterator() {
        return new InOrder(root);
    }

    @Override
    public Iterator<E> preorderIterator() {
        return new PreOrder(root);
    }

    @Override
    public Iterator<E> postorderIterator() {
        return new PostOrder(root);
    }

    private final class InOrder implements Iterator<E> {
        private final Deque<BSTreeNode<E>> stack = new ArrayDeque<>();

        InOrder(BSTreeNode<E> n) {
            pushLeft(n);
        }

        private void pushLeft(BSTreeNode<E> n) {
            while (n != null) {
                stack.push(n);
                n = n.getLeft();
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            if (stack.isEmpty())
                throw new NoSuchElementException();
            BSTreeNode<E> n = stack.pop();
            if (n.getRight() != null)
                pushLeft(n.getRight());
            return n.getElement();
        }
    }

    private final class PreOrder implements Iterator<E> {
        private final Deque<BSTreeNode<E>> stack = new ArrayDeque<>();

        PreOrder(BSTreeNode<E> n) {
            if (n != null)
                stack.push(n);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            if (stack.isEmpty())
                throw new NoSuchElementException();
            BSTreeNode<E> n = stack.pop();
            if (n.getRight() != null)
                stack.push(n.getRight());
            if (n.getLeft() != null)
                stack.push(n.getLeft());
            return n.getElement();
        }
    }

    private final class PostOrder implements Iterator<E> {
        private final Deque<BSTreeNode<E>> s1 = new ArrayDeque<>();
        private final Deque<BSTreeNode<E>> s2 = new ArrayDeque<>();

        PostOrder(BSTreeNode<E> n) {
            if (n != null)
                s1.push(n);
            while (!s1.isEmpty()) {
                BSTreeNode<E> cur = s1.pop();
                s2.push(cur);
                if (cur.getLeft() != null)
                    s1.push(cur.getLeft());
                if (cur.getRight() != null)
                    s1.push(cur.getRight());
            }
        }

        @Override
        public boolean hasNext() {
            return !s2.isEmpty();
        }

        @Override
        public E next() {
            if (s2.isEmpty())
                throw new NoSuchElementException();
            return s2.pop().getElement();
        }
    }
}