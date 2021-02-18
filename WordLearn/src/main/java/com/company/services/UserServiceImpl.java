package com.company.services;

import com.company.Entities.*;
import com.company.Entities.SpecialSqlResults.UserAndScores;
import com.company.Entities.SpecialSqlResults.UserAndWordCount;
import com.company.Repositories.AccountRepository;
import com.company.Repositories.UserRepository;
import com.company.Repositories.WordTestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WordTestResultRepository wordTestResultRepository;
    @Autowired
    AccountRepository accountRepository;
    public boolean addUser(User user){
        User userFromDB = userRepository.findUserByLogin(user.getLogin());
        if (userFromDB != null) {
            System.out.println("Пользователь с таким логином уже существует");
            return false;
        }
        Set<Role> roles = new HashSet<>();
        Collections.addAll(roles, new Role("ROLE_USER"));
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }
    @Override
    public boolean removeUserById(Integer id){
        userRepository.deleteById(id);
        return true;
    };

    @Override
    public User findUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Override
    public List<Word> findAllUserWords(Integer id){
        return userRepository.findByUserId(id);
    };
    @Override
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    };

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("load user by username");
        return user;
    }

    @Override
    public List<UserAndWordCount> getUserRating() {
        return userRepository.getUserRating();
    }

    @Override
    public Double getAvgUserResult(int id) {
        double avgScore = userRepository.getUserAvgScore(id);
        return avgScore;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Account getAccount(User user) {
        WordTestResult result = wordTestResultRepository.findFirstByUser(user);
        if (result == null){
            Account account = new Account(user, Account.INITIAL_POINTS);
            return account;
        }
        Account account = accountRepository.findFirstByUser(user);
        return account;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean addAccount(Account account) {
        accountRepository.save(account);
        return true;
    }

    @Override
    public List<UserAndScores> getUsersScores() {
        return userRepository.getAvgScores();
    }
}
