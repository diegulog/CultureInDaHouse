package edu.uoc.epcsd.showcatalog.domain.repository;

import edu.uoc.epcsd.showcatalog.domain.Role;
import edu.uoc.epcsd.showcatalog.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    Optional<User> findUserByUsername(String username);

    Long createUser(User user);

    void deleteUser(Long id);

    Optional<Role> findRoleByName(String name);


}
