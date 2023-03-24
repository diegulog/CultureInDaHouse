package edu.uoc.epcsd.showcatalog.domain.service;

import edu.uoc.epcsd.showcatalog.domain.Company;
import edu.uoc.epcsd.showcatalog.domain.Role;
import edu.uoc.epcsd.showcatalog.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    Optional<User> findUserByUsername(String username);

    Long createUser(User user);

    void deleteUser(Long id);
    Optional<Role> findRoleByName(String name);

    List<Company> getCompaniesUser(Long id);

    List<User> getUserByRole(Long roleId);
}
