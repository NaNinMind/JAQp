package com.example.JAQpApi.Repository;

import com.example.JAQpApi.Entity.User.User;
import com.example.JAQpApi.Entity.UserResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserResultRepo extends JpaRepository<UserResult, Integer>
{
    List<UserResult> findAllByUser(User user);
    List<UserResult> findAllByUser(User user, Sort sort);

}
