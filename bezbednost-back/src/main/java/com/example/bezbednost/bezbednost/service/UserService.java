package com.example.bezbednost.bezbednost.service;

import com.example.bezbednost.bezbednost.dto.UserDto;
import com.example.bezbednost.bezbednost.exception.ResourceConflictException;
import com.example.bezbednost.bezbednost.iservice.IRoleService;
import com.example.bezbednost.bezbednost.iservice.IUserService;
import com.example.bezbednost.bezbednost.mapper.UserMapper;
import com.example.bezbednost.bezbednost.model.Role;
import com.example.bezbednost.bezbednost.model.User;
import com.example.bezbednost.bezbednost.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleService roleService;

    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder, IRoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(UserDto userDto) {
        User user = UserMapper.mapDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getUsername() + "123"));
        List<Role> roles = roleService.findByName(getUserRole(userDto.getRole()));
        user.setRoles(roles);
        userRepository.save(user);

        return user;
    }

    @Override
    public boolean isUserRegistered(String username) {
        User user = findByUsername(username);
        return user != null;
    }

    private String getUserRole(String role){
        if(role.contains("root")){
            return "organization";
        } else if(role.contains("intermediate")){
            return "service";
        } else if(role.contains("end-entity")) {
            return "user";
        } else {
            return "admin";
        }
    }

    @Override
    public String findUserRole(Integer id) {
        Optional<User> user = this.userRepository.findById(id);
        List<Role> roles = user.get().getRoles();
        return roles.get(0).getName();
    }

}
