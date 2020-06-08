package com.company.Repositories;

import com.company.Entities.User;
import com.company.Entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.company.Entities.SpecialSqlResults.UserAndWordCount;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select w from Word w where w.user.id=?1")
    List<Word> findByUserId(Integer id);
    User findUserByLogin(String login);
    @Query("select new com.company.Entities.SpecialSqlResults.UserAndWordCount(w.user.login, count(w.id)) from Word w " +
            "join User u on w.user.id=u.id " +
            "group by w.user.login " +
            "order by count(w.id) desc")
    List<UserAndWordCount> getUserRating();

}
