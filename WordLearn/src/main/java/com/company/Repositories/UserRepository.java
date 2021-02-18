package com.company.Repositories;

import com.company.Entities.SpecialSqlResults.UserAndScores;
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

    @Query("select avg(wtr.score) from WordTestResult wtr "+
            "where wtr.user.id= :id " +
            "group by wtr.user.id")
    Double getUserAvgScore(int id);

    @Query("select new com.company.Entities.SpecialSqlResults.UserAndScores(wtr.user, avg(wtr.score)) from WordTestResult wtr "+
            "group by wtr.user")
    List<UserAndScores> getAvgScores();

}
