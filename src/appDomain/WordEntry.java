package appDomain;

import java.io.Serializable;
import java.util.*;

public class WordEntry implements Comparable<WordEntry>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String word;
    private final Map<String, List<Integer>> hitsByFile = new LinkedHashMap<>();

    public WordEntry(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public Map<String, List<Integer>> getHits() {
        return hitsByFile;
    }

    public void addOccurrence(String file, int line) {
        List<Integer> lines = hitsByFile.get(file);
        if (lines == null) {
            lines = new ArrayList<>();
            hitsByFile.put(file, lines);
        }
        lines.add(line);
    }

    public int totalCount() {
        int sum = 0;
        for (List<Integer> v : hitsByFile.values())
            sum += v.size();
        return sum;
    }

    @Override
    public int compareTo(WordEntry o) {
        return this.word.compareTo(o.word);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WordEntry other = (WordEntry) o;
        return word.equals(other.word);
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public String toString() {
        return word;
    }
}