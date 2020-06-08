package com.company;

import com.company.Application;
import com.company.Entities.SpecialSqlResults.UserAndWordCount;
import com.company.Entities.User;
import com.company.Repositories.UserRepository;
import com.company.services.UserService;
import com.company.services.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReposMethodsTests {
    @Autowired
    private TestEntityManager entityManager;
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public BCryptPasswordEncoder getEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserByLogin(){
        User user = new User("hh", "hh", 56, "hh", bCryptPasswordEncoder.encode("hh"));
        entityManager.persist(user);
        entityManager.flush();
        User foundedUser = userRepository.findUserByLogin("hh");
        System.out.println(foundedUser.getLogin());
        assertThat(user.getLogin()).isEqualTo(foundedUser.getLogin());
    }

}
