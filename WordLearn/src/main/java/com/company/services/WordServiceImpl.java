package com.company.services;

import com.company.Entities.SpecialSqlResults.WordsAndCount;
import com.company.Entities.User;
import com.company.Entities.Word;
import com.company.Repositories.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class WordServiceImpl implements WordService {
    @Autowired
    WordRepository wordRepository;
    @PersistenceContext
    EntityManager entityManager;
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

    @Override
    public List<WordsAndCount> popularWords() {
        return entityManager.createQuery("select new com.company.Entities.SpecialSqlResults.WordsAndCount(w.word, count(w.word)) from Word w "+
                "group by w.word " +
                "order by count(w.word) desc", WordsAndCount.class).setMaxResults(5).getResultList();
    }
}
