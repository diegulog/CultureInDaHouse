package edu.uoc.epcsd.showcatalog.infrastructure.repository.jpa;
import edu.uoc.epcsd.showcatalog.domain.Company;
import edu.uoc.epcsd.showcatalog.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyRepositoryImpl implements CompanyRepository {

    private final SpringDataCompanyRepository jpaRepository;

    @Override
    public List<Company> findAllCompanies() {
        return jpaRepository.findAll().stream().map(CompanyEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Company> findCompanyById(Long id) {
        return jpaRepository.findById(id).map(CompanyEntity::toDomain);
    }

    @Override
    public Long createCompany(Company company) {
        return jpaRepository.save(CompanyEntity.fromDomain(company)).getId();
    }

    @Override
    public void deleteCompany(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void updateCompany(Company company) {
        CompanyEntity comp = CompanyEntity.fromDomain(company);
        comp.setId(company.getId());
        jpaRepository.save(comp);
    }

}
