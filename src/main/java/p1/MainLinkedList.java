package p1;

import java.util.*;


public class MainLinkedList {
    private MainLink head;

    public MainLinkedList() {
        head = null;
    }

    public void insert(String keyword, String word) {
        MainLink current = findMainLink(keyword);
        if (current == null) {
            MainLink newMainLink = new MainLink(keyword);
            newMainLink.babyList.insert(word);
            newMainLink.next = head;
            head = newMainLink;
        } else {
            current.babyList.insert(word);
        }
    }

    private MainLink findMainLink(String keyword) {
        MainLink current = head;
        while (current != null) {
            if (current.keyword.equals(keyword)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public String generateText(String startingWord, int wordCount) {
        StringBuilder result = new StringBuilder();
        String currentWord = startingWord;
        for (int i = 0; i < wordCount; i++) {
            result.append(currentWord);
            if ((i + 1) % 15 == 0 && i != 0) {
                result.append("\n");
            } else {
                result.append(" ");
            }
            MainLink currentMainLink = findMainLink(currentWord);
            LinkedList<String> babyList = new LinkedList<>();
            if (currentMainLink != null) {
                Iterator<String> iterator = currentMainLink.babyListIterator();
                while (iterator.hasNext()) {
                    babyList.add(iterator.next());
                }
            }

            if (babyList.isEmpty()) {
                currentWord = findSimilarWord(currentWord);
                if (currentWord == null) {
                    break;
                }
            } else {
                currentWord = babyList.get((int) (Math.random() * babyList.size()));
            }
        }
        return result.toString().trim();
    }

    private String findSimilarWord(String word) {
        // Calculate the frequency of each character in the given word
        Map<Character, Integer> charFrequency = new HashMap<>();
        for (char c : word.toCharArray()) {
            charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
        }
        int candidatesSize = 10;
        PriorityQueue<Map.Entry<String, Integer>> candidates = new PriorityQueue<>(
                (e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())
        );

        MainLink current = head;
        while (current != null) {
            int commonChars = 0;
            for (char c : current.keyword.toCharArray()) {
                if (charFrequency.containsKey(c)) {
                    commonChars += charFrequency.get(c);
                }
            }

            if (candidates.size() < candidatesSize || commonChars > candidates.peek().getValue()) {
                if (candidates.size() == candidatesSize) {
                    candidates.poll();
                }
                candidates.offer(Map.entry(current.keyword, commonChars));
            }

            current = current.next;
        }

        String similarWord = candidates.peek().getKey();
        return similarWord;
    }
}
