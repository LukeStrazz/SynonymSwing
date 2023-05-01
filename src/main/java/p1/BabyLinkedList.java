package p1;
import java.util.*;

public class BabyLinkedList {
    BabyLink first;

    void insert(String word) {
        BabyLink newLink = new BabyLink(word);
        newLink.next = first;
        first = newLink;
    }

    String getRandomWord() {
        ArrayList<String> words = new ArrayList<>();
        BabyLink current = first;
        while (current != null) {
            words.add(current.word);
            current = current.next;
        }
        int randomIndex = new Random().nextInt(words.size());
        return words.get(randomIndex);
    }
}
