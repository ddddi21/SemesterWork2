package services;

import guess.WordRepository;

import java.util.ArrayList;

import static guess.WordRepository.*;

public class WordService {
    WordRepository repository = new WordRepository();
    String[] text;

    public boolean isRightWord(String word){
        text = word.split(":");
        boolean guess = false;
        for (int i = 0; i < allWords.size(); i++) {
            if (allWords.get(i).equals(text[1].trim().toLowerCase())) {
                guess = true;
                break;
            }
        } return guess;
    }
}
