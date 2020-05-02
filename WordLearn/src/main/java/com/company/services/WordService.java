package com.company.services;

import com.company.Entities.User;
import com.company.Entities.Word;

import java.util.List;

public interface WordService {
    boolean addWord(Word word);
    boolean removeWordById(Integer id);
    List<Word> getAllWords();
}
