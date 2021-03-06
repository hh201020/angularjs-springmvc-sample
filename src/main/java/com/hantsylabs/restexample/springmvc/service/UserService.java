/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylabs.restexample.springmvc.service;

import com.hantsylabs.restexample.springmvc.DTOUtils;
import com.hantsylabs.restexample.springmvc.domain.User;
import com.hantsylabs.restexample.springmvc.model.PasswordForm;
import com.hantsylabs.restexample.springmvc.model.ProfileForm;
import com.hantsylabs.restexample.springmvc.model.UserDetails;
import com.hantsylabs.restexample.springmvc.model.UserForm;
import com.hantsylabs.restexample.springmvc.repository.UserRepository;
import com.hantsylabs.restexample.springmvc.repository.UserSpecifications;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 *
 * @author hantsy
 */
@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    public Page<UserDetails> findAll(String q, String role, Pageable page) {
        if (log.isDebugEnabled()) {
            log.debug("findAll by keyword@" + q + ", role:" + role);
        }

        Page<User> users= userRepository.findAll(UserSpecifications.filterUsersByKeyword(q, role), page);
        
        return DTOUtils.mapPage(users, UserDetails.class);
    }

    public UserDetails saveUser(UserForm form) {
        Assert.notNull(form, " @@ UserForm is null");

        if (log.isDebugEnabled()) {
            log.debug("saving user@" + form);
        }

        if (userRepository.findByUsername(form.getUsername()) != null) {
            throw new UsernameExistedException(form.getUsername());
        }

        User user = DTOUtils.map(form, User.class);
        user.setPassword(passwordEncoder.encode(form.getPassword()));

        User saved = userRepository.save(user);

        return DTOUtils.map(saved, UserDetails.class);
    }

    public void updateUser(Long id, UserForm form) {
        Assert.notNull(id, "user id can not be null");

        if (log.isDebugEnabled()) {
            log.debug("find user by id @" + id);
        }

        User user = userRepository.findOne(id);

        if (user == null) {
            throw new ResourceNotFoundException(id);
        }

        DTOUtils.mapTo(form, user);

        User userSaved = userRepository.save(user);

        if (log.isDebugEnabled()) {
            log.debug("updated user @" + userSaved);
        }
    }

    public void updatePassword(Long id, PasswordForm form) {
        Assert.notNull(id, "user id can not be null");
        if (log.isDebugEnabled()) {
            log.debug("find user by id @" + id);
        }

        User user = userRepository.findOne(id);

        if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            throw new PasswordMismatchedException();
        }

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));

        User saved = userRepository.save(user);

        if (log.isDebugEnabled()) {
            log.debug("updated user @" + saved);
        }
    }

    public void updateProfile(Long id, ProfileForm form) {
        Assert.notNull(id, "user id can not be null");

        if (log.isDebugEnabled()) {
            log.debug("update profile for user @" + id + ", profile form@" + form);
        }

        User user = userRepository.findOne(id);

        DTOUtils.mapTo(form, user);

        User saved = userRepository.save(user);

        if (log.isDebugEnabled()) {
            log.debug("updated user @" + saved);
        }
    }

    public UserDetails findUserById(Long id) {
        Assert.notNull(id, "user id can not be null");
        if (log.isDebugEnabled()) {
            log.debug("find user by id @" + id);
        }

        User user = userRepository.findOne(id);

        if (user == null) {
            throw new ResourceNotFoundException(id);
        }

        return DTOUtils.map(user, UserDetails.class);
    }

    public void deleteUser(Long id) {
        Assert.notNull(id, "user id can not be null");
        if (log.isDebugEnabled()) {
            log.debug("find user by id @" + id);
        }

        userRepository.delete(id);
    }

}
