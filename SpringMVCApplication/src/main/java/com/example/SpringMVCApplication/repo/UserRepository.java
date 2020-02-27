package com.example.SpringMVCApplication.repo;

import com.example.SpringMVCApplication.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
