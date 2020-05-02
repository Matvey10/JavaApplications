package com.company.services;

import com.company.Entities.Role;
import com.company.Entities.User;
import com.company.Entities.Word;
import com.company.Repositories.UserRepository;
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
}
