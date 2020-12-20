package services;

import guess.WordRepository;

public class WordService {
    WordRepository repository = new WordRepository();

    public boolean isRightWord(String word){
        boolean guess = false;
        for(String string: WordRepository.allWords) {
            if (string.equals(word)) {
                guess = true;
                break;
            }
        } return guess;
    }
}
