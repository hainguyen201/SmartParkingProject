package com.hust.smartparking;

import com.hust.smartparking.entity.User;
import com.hust.smartparking.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository repo;
    private Integer testId=11;
    @Test
    public void testAddNew(){
        User user= new User();
        user.setEmail("hai.nv173089@sis.hust.edu.vn");
        user.setPassword("hainv");
        user.setName("hainvis_testAdd");
        User saveUser= repo.save(user);
        testId=saveUser.getId();
        Assertions.assertThat(saveUser).isNotNull();
        Assertions.assertThat(saveUser.getId()).isGreaterThan(0);
    }
    @Test
    public void testGetAll(){
        Iterable<User> users=repo.findAll();
        Assertions.assertThat(users).hasSizeGreaterThan(0);
    }
    @Test
    public void testUpdate(){
        Integer id=testId;
        Optional<User> optionalUser =repo.findById(id);
        User user=optionalUser.get();
        user.setPassword("Hello");
        repo.save(user);

        User userUpdate= repo.findById(id).get();
        Assertions.assertThat(userUpdate.getPassword()).isEqualTo("Hello");
    }
    @Test
    public void testGet(){
        Integer userId=testId;
        Optional<User> userOptional=repo.findById(userId);
        Assertions.assertThat(userOptional).isPresent();
        System.out.println(userOptional.get());
    }
    @Test
    public void testGetUserByUserName(){
        String username= "hainguyen202";
        Iterable<User> userIterable= repo.findByUsername(username);
        List<User> userList= new ArrayList<>();
        userIterable.forEach(userList::add);

        Assertions.assertThat(userList.size()).isGreaterThan(0);
    }
//    @Test
//    public void testDelete(){
//        Integer userId =testId;
//        repo.deleteById(userId);
//
//        Optional<User> optionalUser= repo.findById(userId);
//        Assertions.assertThat(optionalUser).isNotPresent();
//
//    }

}
