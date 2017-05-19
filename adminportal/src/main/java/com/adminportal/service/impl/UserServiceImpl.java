package com.adminportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adminportal.entities.PasswordResetToken;
import com.adminportal.entities.User;
import com.adminportal.entities.security.UserRole;
import com.adminportal.enumeration.Category;
import com.adminportal.enumeration.City;
import com.adminportal.repository.PasswordResetTokenRepository;
import com.adminportal.repository.RoleRepository;
import com.adminportal.repository.SkillRespository;
import com.adminportal.repository.UserRepository;
import com.adminportal.repository.UserRoleRepository;
import com.adminportal.service.UserService;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SkillRespository skillRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return this.passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        this.passwordResetTokenRepository.save(myToken);
    }

    @Override
    public User findByEmail(final String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User createUser(final User user, final Set<UserRole> userRoles) {
        User localUser = this.userRepository.findByEmail(user.getEmail());

        if (localUser != null) {
            LOG.info("user {} already exists. Nothing will be done.", user.getEmail());
        } else {
            for (final UserRole ur : userRoles) {
                this.roleRepository.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            localUser = this.userRepository.save(user);
        }

        return localUser;
    }

    @Override
    public User save(final User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User findById(final Long id) {
        return this.userRepository.findOne(id);
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public List<User> findByCategory(final Category category) {
        return this.userRepository.findByCategory(category);
    }

    @Override
    public List<User> blurrySearch(final String name) {
        final List<User> firstNameList = this.userRepository.findByfirstNameContaining(name);
        final List<User> lastNameList = this.userRepository.findBylastNameContaining(name);
        final List<User> activeUserList = new ArrayList<>();

        for (final User user : firstNameList) {
            if (user.getEnabled()) {
                activeUserList.add(user);
            }
        }

        for (final User user : lastNameList) {
            if (user.getEnabled() && !firstNameList.contains(user)) {
                activeUserList.add(user);
            }
        }

        return activeUserList;
    }

    @Override
    public List<User> findByCity(final City city) {
        return this.userRepository.findByCity(city);
    }

    @Override
    public void removeOne(final Long id) {
        final User user = this.userRepository.findOne(id);
        for (final UserRole userRole : user.getUserRoles()) {
            userRole.setRole(null);
            userRole.setUser(null);
            this.userRoleRepository.delete(userRole.getUserRoleId());
        }
        this.userRepository.delete(id);
    }

}
