package com.kindred.game.service;

import com.kindred.game.entity.User;
import com.kindred.game.exception.UserNotFoundException;
import com.kindred.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User addUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        var user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException((long) id));
        if(user != null)
            userRepository.deleteById(user.getId());
    }


}
