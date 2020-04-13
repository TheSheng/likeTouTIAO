package com.lisheng.project.user.repository;

import com.lisheng.project.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRespository extends JpaRepository<User,Integer> {
    User findByUserNameAndPassWord(String name,String pass);
    User findByUserName(String username);
    User findById(Long id);
    @Query(value = "select userName from User")
    List<String> find();

}
