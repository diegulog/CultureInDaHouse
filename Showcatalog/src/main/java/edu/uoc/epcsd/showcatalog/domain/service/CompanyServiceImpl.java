package edu.uoc.epcsd.showcatalog.domain.service;
import edu.uoc.epcsd.showcatalog.domain.Company;
import edu.uoc.epcsd.showcatalog.domain.Role;
import edu.uoc.epcsd.showcatalog.domain.User;
import edu.uoc.epcsd.showcatalog.domain.repository.CompanyRepository;
import edu.uoc.epcsd.showcatalog.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CompanyServiceImpl implements CompanyService  {

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    @Override
    public List<Company> findAllCompanies() {
        return companyRepository.findAllCompanies();
    }

    @Override
    public Optional<Company> findCompanyById(Long id) {

        return companyRepository.findCompanyById(id);

    }

    @Override
    public Long createCompany(Company company) {

        company.setId(null);
        Long id = companyRepository.createCompany(company);
        checkRoles(id);
        return id;
    }

    @Override
    public void deleteCompany(Long id) {

        companyRepository.deleteCompany(id);
        checkRoles(id);
    }

    @Override
    public void updateCompany(Long id, Company oldCompany) {
        Optional<Company> newCompany = companyRepository.findCompanyById(id);
        if(newCompany.isPresent()) {
            newCompany.get().setDescription(oldCompany.getDescription());
            newCompany.get().setImage(oldCompany.getImage());
            newCompany.get().setName(oldCompany.getName());
            newCompany.get().setManagers(oldCompany.getManagers());

            companyRepository.updateCompany(newCompany.get());
            checkRoles(id);
        } else {
            log.error("Error updating company. The company " + id.toString() + " doesn't exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The company with id " + id.toString() + " doesn't exist");
        }
    }

    private void checkRoles (Long idCompany){
        Optional<Role> roleBusiness = userRepository.findRoleByName("BUSINESS_USER");
        Optional<Role> roleStandard = userRepository.findRoleByName("STANDARD_USER");
        if(roleBusiness.isPresent() && roleStandard.isPresent()){
            // All users assigned to the company have the role "BUSINESS_USER".
            Optional<Company> company = companyRepository.findCompanyById(idCompany);
            company.ifPresent(value -> value.getManagers().forEach((user) -> {
                User userUpdate = userRepository.findUserById(user.getId()).get();
                if(!userUpdate.getRoles().contains(roleBusiness.get())){
                    Set<Role> roles = userUpdate.getRoles();
                    roles.add(roleBusiness.get());
                    if (roles.contains(roleStandard.get())){
                        roles.remove(roleStandard.get());
                    }
                    userUpdate.setRoles(roles);
                    userRepository.createUser(userUpdate);
                }
            }));
            // Unaccompanied users do not have the role "BUSINESS_USER"
            List<User> userBusiness= new ArrayList<User>();
            List<Company> companies = companyRepository.findAllCompanies();
            companies.forEach((comp) -> {
                userBusiness.addAll(new ArrayList<User>(comp.getManagers()));
            });
            List<User> allUsers = userRepository.findAllUsers();
            allUsers.forEach((us)-> {
                if(us.getRoles().contains(roleBusiness.get()) && userBusiness.stream().noneMatch(o -> o.getId().equals(us.getId()))){
                    Set<Role> usRoles = us.getRoles();
                    usRoles.remove(roleBusiness.get());
                    if (!usRoles.contains(roleStandard.get())){
                        usRoles.add(roleStandard.get());
                    }
                    us.setRoles(usRoles);
                    userRepository.createUser(us);
                }
            });
        }
    }
}
