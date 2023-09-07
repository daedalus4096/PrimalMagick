package com.verdantartifice.primalmagick.client.books;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.Util;

/**
 * Maps the full set of words used in a book language, plus metadata.
 * 
 * @author Daedalus4096
 */
public class Lexicon {
    protected static final Comparator<Entry> BY_HASH_CODE = Comparator.comparingInt(Entry::hashCode);
    protected static final Comparator<Entry> BY_MOST_FREQUENT = Comparator.comparingInt(Entry::getCount).reversed().thenComparing(BY_HASH_CODE);
    
    protected final Map<String, Entry> entries = new HashMap<>();
    
    private Supplier<List<String>> cachedMostFrequent;
    private Function<Integer, List<String>> cachedOfLength;
    
    public Lexicon() {
        this(new String[0]);
    }
    
    public Lexicon(String... words) {
        this(List.of(words));
    }
    
    public Lexicon(Collection<String> words) {
        this.addWords(words);
        this.invalidate();
    }
    
    public static Lexicon parse(@Nonnull JsonObject json) throws Exception {
        return new Lexicon(json.get("words").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList());
    }
    
    public void clear() {
        this.entries.clear();
        this.invalidate();
    }
    
    public void invalidate() {
        this.cachedMostFrequent = Suppliers.memoize(this::getWordsByMostFrequentInner);
        this.cachedOfLength = Util.memoize(this::getWordsOfLengthInner);
    }
    
    public int size() {
        return this.entries.size();
    }
    
    public void addWord(String word) {
        this.addWordInner(word);
        this.invalidate();
    }
    
    public void addWords(Collection<String> words) {
        words.forEach(this::addWordInner);
        this.invalidate();
    }
    
    private void addWordInner(String word) {
        if (this.entries.containsKey(word)) {
            this.entries.get(word).incrementCount();
        } else {
            this.entries.put(word, new Entry(word));
        }
    }
    
    /**
     * Returns all words in this lexicon, sorted descending by frequency, so that the most frequently
     * appearing words come first.  Sub-sorting is done by hash code to ensure stable ordering.
     * 
     * @return sorted list of all lexicon words
     */
    public List<String> getWordsByMostFrequent() {
        return this.cachedMostFrequent.get();
    }
    
    private List<String> getWordsByMostFrequentInner() {
        return this.entries.values().stream().sorted(BY_MOST_FREQUENT).map(Entry::getWord).toList();
    }
    
    /**
     * Returns all words in this lexicon of the given length.  Words are sorted by hash code to ensure
     * stable ordering.
     * 
     * @param length the length of words to be returned
     * @return sorted list of all lexicon words of the given length
     */
    public List<String> getWordsOfLength(int length) {
        return this.cachedOfLength.apply(length);
    }
    
    private List<String> getWordsOfLengthInner(int length) {
        return this.entries.values().stream().filter(entry -> entry.getLength() == length).sorted(BY_HASH_CODE).map(Entry::getWord).toList();
    }
    
    /**
     * Returns a word from this lexicon that is the same length as the original word.  The output of this
     * function will be consistent so long as the lexicon's contents do not change.  Should no word of an
     * appropriate length be found, a placeholder word will be returned instead.
     * 
     * @param original the word to be replaced
     * @return a replacement word of equal length
     */
    public String getReplacementWord(String original) {
        List<String> candidates = this.getWordsOfLength(original.length());
        if (candidates.isEmpty()) {
            return "a".repeat(original.length());
        } else {
            return candidates.get(original.hashCode() % candidates.size());
        }
    }

    protected static class Entry {
        private final String word;
        private int count;
        
        public Entry(String word) {
            this.word = word;
            this.count = 1;
        }
        
        public String getWord() {
            return this.word;
        }
        
        public int getLength() {
            return this.word.length();
        }
        
        public int getCount() {
            return this.count;
        }
        
        public void incrementCount() {
            this.count++;
        }

        @Override
        public int hashCode() {
            return Objects.hash(word);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Entry other = (Entry) obj;
            return Objects.equals(word, other.word);
        }

        @Override
        public String toString() {
            return "Entry [word=" + word + ", count=" + count + "]";
        }
    }
}