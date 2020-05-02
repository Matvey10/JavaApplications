package com.company.services;

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

}
