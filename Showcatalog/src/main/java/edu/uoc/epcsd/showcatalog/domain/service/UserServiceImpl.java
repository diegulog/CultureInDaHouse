package edu.uoc.epcsd.showcatalog.domain.service;

import edu.uoc.epcsd.showcatalog.domain.Company;
import edu.uoc.epcsd.showcatalog.domain.Role;
import edu.uoc.epcsd.showcatalog.domain.User;
import edu.uoc.epcsd.showcatalog.domain.repository.CompanyRepository;
import edu.uoc.epcsd.showcatalog.domain.repository.UserRepository;
import edu.uoc.epcsd.showcatalog.infrastructure.repository.jpa.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Long createUser(User user) {
        Optional<User> optUser = userRepository.findUserByUsername(user.getUsername());
        if (optUser.isPresent()) {
            log.error("Error creating user. The user " + optUser.get().getUsername() + " it already exists");
            return null;
        } else {
            user.setId(null);
            return userRepository.createUser(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    @Override
    public Optional<Role> findRoleByName(String name) {
        return userRepository.findRoleByName(name);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException("The username " + username + " doesn't exist");
        });
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public List<Company> getCompaniesUser(Long id) {
        return companyRepository.findAllCompanies().stream().filter(p -> p.getManagers().stream().anyMatch(o -> o.getId().equals(id))).collect(Collectors.toList());
    }

    @Override
    public List<User> getUserByRole(Long id) {
        return userRepository.findAllUsers().stream().filter(user -> user.getRoles().stream().anyMatch(role -> role.getId().equals(id))).collect(Collectors.toList());
    }

}
