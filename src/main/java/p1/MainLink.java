package p1;

public class MainLink {
    String keyword;
    BabyLinkedList babyList;
    MainLink next;

    public MainLink(String keyword) {
        this.keyword = keyword;
        this.babyList = new BabyLinkedList();
    }
}
