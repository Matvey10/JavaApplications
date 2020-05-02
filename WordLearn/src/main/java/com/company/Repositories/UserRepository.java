package com.company.Repositories;

import com.company.Entities.User;
import com.company.Entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Query("select w from Word w where w.user.id=?1")
    List<Word> findByUserId(Integer id);
    User findUserByLogin(String login);
}
