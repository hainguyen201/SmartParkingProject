package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.Device;
import com.hust.smartparking.entity.User;
import com.hust.smartparking.repository.UserRepository;
import com.hust.smartparking.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired private UserRepository userRepo;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Override
    public Iterable<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepo.findById(id);
    }

    @Override
    public User save(User user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public void remove(int id) {
        userRepo.deleteById(id);
    }

    @Override
    public Iterable<User> searchData(User user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query=cb.createQuery(User.class);
        Root<User> usersearch=query.from(User.class);
        Path<String> name=usersearch.get("name");
        Path<String> phoneNumber=usersearch.get("phoneNumber");
        Path<String> email=usersearch.get("email");
        Path<String> username=usersearch.get("username");
        Path<String> role=usersearch.get("role");
        List<Predicate> predicateList=new ArrayList<>();
        if(user.getName()!=null && !user.getName().isEmpty())
            predicateList.add(cb.like(name, "%"+user.getName()+"%"));
        if(user.getPhoneNumber()!=null && !user.getPhoneNumber().isEmpty())
            predicateList.add(cb.like(phoneNumber, "%"+user.getPhoneNumber()+"%"));
        if(user.getEmail()!=null && !user.getEmail().isEmpty())
            predicateList.add(cb.like(email, "%"+user.getEmail()+"%"));
        if(user.getUsername()!=null && !user.getUsername().isEmpty())
            predicateList.add(cb.like(username, "%"+user.getUsername()+"%"));
        if(user.getRole()!=null && !user.getRole().isEmpty())
            predicateList.add(cb.equal(role, user.getRole()));
        query.select(usersearch).where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
        return entityManager.createQuery(query).getResultList();
    }
    @Override
    public Iterable<User> getUserByUserName(String username){
        return userRepo.findByUsername(username);
    }
}
