package p1;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class MainHashTable {
    private final HashMap<String, BabyLinkedList> mainTable;
    private final ConcurrentHashMap<String, String> similarWordCache = new ConcurrentHashMap<>();

    public MainHashTable() {
        mainTable = new HashMap<>();
    }

    public void insert(String keyword, String word) {
        BabyLinkedList babyList = mainTable.get(keyword);
        if (babyList == null) {
            babyList = new BabyLinkedList();
            mainTable.put(keyword, babyList);
        }
        babyList.insert(word);
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
            BabyLinkedList babyList = mainTable.get(currentWord);
            if (babyList == null) {
                if (!similarWordCache.containsKey(currentWord)) {
                    similarWordCache.put(currentWord, findSimilarWord(currentWord));
                }
                currentWord = similarWordCache.get(currentWord);
                if (currentWord == null) {
                    break;
                }
            } else {
                currentWord = babyList.getRandomWord();
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

        for (String key : mainTable.keySet()) {
            int commonChars = 0;
            for (char c : key.toCharArray()) {
                if (charFrequency.containsKey(c)) {
                    commonChars += charFrequency.get(c);
                }
            }

            if (candidates.size() < candidatesSize || commonChars > candidates.peek().getValue()) {
                if (candidates.size() == candidatesSize) {
                    candidates.poll();
                }
                candidates.offer(Map.entry(key, commonChars));
            }
        }

        String similarWord = candidates.peek().getKey();
        return similarWord;
    }
}
