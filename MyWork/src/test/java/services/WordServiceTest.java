package services;

import guess.WordRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class WordServiceTest {
    WordService wordService;

    @Test
    void isRightWord() {
        String word = "bob: hi";
        String guessword = "hi";
         wordService = new WordService();
        Assertions.assertTrue(wordService.isRightWord(word, guessword));

    }

    @Test
    void randomChoosing() {
        wordService = new WordService();
        String word = wordService.randomChoosing();
        boolean contains = WordRepository.allWords.contains(word);
        Assertions.assertTrue(contains);
    }
}