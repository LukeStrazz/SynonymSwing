package p1;

import java.util.ArrayList;
import java.util.Random;

public class BabyLinkedList {
    private BabyLink first;
    private int size;

    void insert(String word) {
        BabyLink newLink = new BabyLink(word);
        newLink.next = first;
        first = newLink;
        size++;
    }

    String getRandomWord() {
        ArrayList<String> words = new ArrayList<>();
        BabyLink current = first;
        while (current != null) {
            words.add(current.word);
            current = current.next;
        }
        if (words.isEmpty()) {
            return null;
        }
        int randomIndex = new Random().nextInt(words.size());
        return words.get(randomIndex);
    }

}
