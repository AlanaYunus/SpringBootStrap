package com.bootstrap.bootstrap.service;

import com.bootstrap.bootstrap.DAO.RoleDao;
import com.bootstrap.bootstrap.DAO.UserDao;
import com.bootstrap.bootstrap.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private RoleDao roleDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public void createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleDao.setRole(user.getRoleInd()));
        user.setRoles(roleDao.setRole(user.getRoleInd()));
        userDao.createUser(user);
    }

    public User readUser(Long id) {
        return userDao.readUser(id);
    }

    @Transactional
    public void updateUser(User user) {
        User userOld = userDao.getUserById(user.getId());
        user.setRoles(roleDao.setRole(user.getRoleInd()));

        if (user.getPassword().equals(userOld.getPassword())) {
            userDao.updateUser(user);
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDao.updateUser(user);
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    public List<User> allUsers() {
        return userDao.allUsers();
    }

    @Override
    public boolean isAllowed(Long id, Principal principal) {
        User user = getUserByName(principal.getName());
        return user.getId() == id || user.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().contains("ADMIN"));
    }
}

