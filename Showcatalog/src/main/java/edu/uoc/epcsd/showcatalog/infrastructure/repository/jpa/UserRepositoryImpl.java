package edu.uoc.epcsd.showcatalog.infrastructure.repository.jpa;

import edu.uoc.epcsd.showcatalog.domain.Role;
import edu.uoc.epcsd.showcatalog.domain.User;
import edu.uoc.epcsd.showcatalog.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryImpl implements UserRepository {
    private final SpringDataUserRepository jpaUserRepository;
    private final SpringDataRoleRepository jpaRoleRepository;


    @Override
    public List<User> findAllUsers() {
        return jpaUserRepository.findAll().stream().map(UserEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return jpaUserRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Optional<User>  user = jpaUserRepository.findByUsername(username).map(UserEntity::toDomain);
        return user;
    }

    @Override
    public Long createUser(User user) {
        return jpaUserRepository.save(UserEntity.fromDomain(user)).getId();
    }

    @Override
    public void deleteUser(Long id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public Optional<Role> findRoleByName(String name) {
        return jpaRoleRepository.findByRoleName(name).map(RoleEntity::toDomain);
    }
}
