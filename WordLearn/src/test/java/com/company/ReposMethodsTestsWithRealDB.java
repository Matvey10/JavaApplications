package com.company;

import com.company.Entities.SpecialSqlResults.UserAndWordCount;
import com.company.Entities.SpecialSqlResults.WordsAndCount;
import com.company.Entities.User;
import com.company.Repositories.UserRepository;
import com.company.services.UserService;
import com.company.services.UserServiceImpl;
import com.company.services.WordService;
import com.company.services.WordServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        //SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class ReposMethodsTestsWithRealDB {
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
        @Bean
        public BCryptPasswordEncoder getEncoder(){
            return new BCryptPasswordEncoder();
        }
        @Bean
        public WordService wordService(){
            return new WordServiceImpl();
        }
    }
    @Autowired
    private MockMvc mvc;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WordService wordService;

    @Test
    public void testFindUserByLogin(){
        User user = new User("first", "second", 20, "sdk",
                bCryptPasswordEncoder.encode("password"));
        User found = userRepository.findUserByLogin("sdk");
        System.out.println(found.getLogin() + " " + found.getFirstName());
        assertThat(user.getLogin()).isEqualTo(found.getLogin());
    }

    @Test
    public void userRatingTest(){
        List<UserAndWordCount> rating = userRepository.getUserRating();
        if (rating != null){
            for (UserAndWordCount userAndWordCount : rating){
                System.out.println(userAndWordCount.getUserLogin() + ":" + userAndWordCount.getCount());
            }
        }
    }

    @Test
    public void popularWordsTest(){
        List<WordsAndCount> popularWords = wordService.popularWords();
        popularWords.stream().forEach(System.out::println);
    }

}
