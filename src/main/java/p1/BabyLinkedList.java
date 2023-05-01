package p1;
import java.util.*;
import java.util.Iterator;

public class BabyLinkedList implements Iterable<String> {
    private BabyLink first;

    public BabyLinkedList() {
        first = null;
    }

    public boolean isEmpty() {
        return (first == null);
    }

    public void insert(String data) {
        BabyLink newLink = new BabyLink(data);
        newLink.next = first;
        first = newLink;
    }

    @Override
    public Iterator<String> iterator() {
        return new BabyListIterator();
    }

    private class BabyListIterator implements Iterator<String> {
        private BabyLink current;

        public BabyListIterator() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String data = current.data;
            current = current.next;
            return data;
        }
    }
}
