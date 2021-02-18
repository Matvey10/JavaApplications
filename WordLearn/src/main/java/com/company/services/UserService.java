package com.company.services;

import com.company.Entities.Account;
import com.company.Entities.SpecialSqlResults.UserAndScores;
import com.company.Entities.SpecialSqlResults.UserAndWordCount;
import com.company.Entities.User;
import com.company.Entities.Word;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService extends UserDetailsService {
    boolean addUser(User user);
    boolean removeUserById(Integer id);
    List<Word> findAllUserWords(Integer id);
    User findUserByLogin(String login);
    Iterable<User> getAllUsers();
    List<UserAndWordCount> getUserRating();
    Double getAvgUserResult(int id);
    Account getAccount(User user);
    boolean addAccount(Account account);
    List<UserAndScores> getUsersScores();
}
