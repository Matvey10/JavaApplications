package com.company.services;

import com.company.Entities.User;
import com.company.Entities.Word;
import com.company.Repositories.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordServiceImpl implements WordService {
    @Autowired
    WordRepository wordRepository;
    public boolean addWord(Word word){
        wordRepository.save(word);
        return true;
    };
   public boolean removeWordById(Integer id){
        wordRepository.deleteById(id);
        return true;
    };
    public List<Word> getAllWords(){
        return wordRepository.findAll();
    };
}
