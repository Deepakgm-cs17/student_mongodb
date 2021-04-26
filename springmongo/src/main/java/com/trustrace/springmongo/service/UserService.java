package com.trustrace.springmongo.service;

import com.trustrace.springmongo.aop.InvalidException;
import com.trustrace.springmongo.dto.UserPatch;
import com.trustrace.springmongo.dto.UserRequest;
import com.trustrace.springmongo.model.UserModel;
import com.trustrace.springmongo.reporistory.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public void saveUser(UserModel userDetails){
        userRepository.save(userDetails);
    }

    public Page<UserModel> findByName(Optional<Integer> noOfPage) {
        return userRepository.findAll(PageRequest.of(noOfPage.orElse(0), 5));
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public String updateStudent(UserRequest userRequest, long id) {

        Optional<UserModel> userRequestOptional = userRepository.findById(id);

        if (!userRequestOptional.isPresent()) {
            return "User Id not found";
        }
        else {
            UserModel user = userRequestOptional.get();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            userRepository.save(user);
            return "User Id updated";
        }
    }

    public String deleteByID(Long id) {
        userRepository.deleteById(id);
        return "User Id Deleted";
    }

    public void patchActionSave(String action, UserPatch user, UserModel userModel) throws Exception {
        if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("replace")) {
            if (user.getFieldName().equals("username")) {
                if (user.getValue().isEmpty()) {
                    throw new InvalidException("Input is invalid.");
                }
                userModel.setUsername(user.getValue());
            } else if (user.getFieldName().equals("email")) {
                if (user.getValue().isEmpty()) {
                    throw new InvalidException("Input is invalid.");
                }

                userModel.setEmail(user.getValue());
            }
            else {
                throw new InvalidException("Input is invalid.");
            }
        } else if (action.equalsIgnoreCase("delete")) {
            if (user.getFieldName().equals("username")) {
                userModel.setUsername("");
            } else if (user.getFieldName().equals("email")) {
                userModel.setEmail("");
            }
            else {
                throw new InvalidException("Input is invalid.");
            }
        }
        userRepository.save(userModel);
    }

    public void updateUserByPatch(ArrayList<UserPatch> userPatch, Integer id) throws Exception {

        Optional<UserModel> userUpdate = userRepository.findById(id);

        for (UserPatch user : userPatch) {
            if (user.getAction().equals("add") || user.getAction().equals("replace") || user.getAction().equals("delete")) {
                patchActionSave(user.getAction(), user, userUpdate.get());
            }
        }
    }
}
