package services;

import guess.WordRepository;
import static guess.WordRepository.*;

public class WordService {
    WordRepository repository = new WordRepository();
    String[] text;

    //надо будет добавить на проверку не любого слова а загаданного
    //нужен фикс
    public boolean isRightWord(String word, String guessWord){
        text = word.split(":");
        boolean guess = false;
        if (guessWord.equals(text[1].trim().toLowerCase())) {
            guess = true;
        } return guess;
    }

    public String randomChoosing(){
        int random = (int) (Math.random() * 21); //всего 11слов в репе (пока)
        String word = allWords.get(random);
        return word;
    }
}
