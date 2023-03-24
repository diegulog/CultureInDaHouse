package edu.uoc.epcsd.showcatalog.application.rest;

import edu.uoc.epcsd.showcatalog.application.request.CreateUserRequest;
import edu.uoc.epcsd.showcatalog.domain.Company;
import edu.uoc.epcsd.showcatalog.domain.Role;
import edu.uoc.epcsd.showcatalog.domain.User;
import edu.uoc.epcsd.showcatalog.domain.service.MailSenderService;
import edu.uoc.epcsd.showcatalog.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class UserRESTController {
    private final UserService userService;
    private final MailSenderService mailSenderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasAnyAuthority('ADMIN_USER','STANDARD_USER')")
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findUsers() {
        log.trace("findUsers");
        return userService.findAllUsers();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER')")
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        log.trace("findUserById");
        return userService.findUserById(id).map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER','BUSINESS_USER','STANDARD_USER')")
    @GetMapping("/users/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> findUserMe() {
        log.trace("findUserMe");
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return findUserByUsername(user);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER','BUSINESS_USER')")
    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        log.trace("findUserByUsername");
        return userService.findUserByUsername(username).map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<Long> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.trace("createUser");
        log.trace("Creating createUser " + createUserRequest);
        User user = createUserRequest.getUser();
        //Encriptamos el password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //Asignamos rol
        Optional<Role> role = userService.findRoleByName("STANDARD_USER");
        if(role.isPresent()){
            Set<Role> roles = new HashSet<>();
            roles.add(role.get());
            user.setRoles(roles);
        }
        Long userId = userService.createUser(user);
        mailSenderService.sendWelcomeEmail(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userId)
                .toUri();
        return ResponseEntity.created(uri).body(userId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        log.trace("deleteUser");
        userService.deleteUser(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER','BUSINESS_USER','STANDARD_USER')")
    @GetMapping("/users/{id}/companies")
    @ResponseStatus(HttpStatus.OK)
    public List<Company> getCompaniesUser(@PathVariable Long id) {
        log.trace("getCompaniesUser");
        return userService.getCompaniesUser(id);
    }


    @GetMapping("/users/byRole/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsersByRole(@PathVariable Long roleId) {
        log.trace("getUserByRole");
        return userService.getUserByRole(roleId);
    }

}
