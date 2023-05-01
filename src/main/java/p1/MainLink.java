package p1;

import java.util.Iterator;

public class MainLink {
    public String keyword;
    public BabyLinkedList babyList;
    public MainLink next;

    public MainLink(String keyword) {
        this.keyword = keyword;
        babyList = new BabyLinkedList();
        next = null;
    }

    public Iterator<String> babyListIterator() {
        return babyList.iterator();
    }
}

