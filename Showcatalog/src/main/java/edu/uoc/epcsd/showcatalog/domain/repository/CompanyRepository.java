package edu.uoc.epcsd.showcatalog.domain.repository;

import edu.uoc.epcsd.showcatalog.domain.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {

    List<Company> findAllCompanies();

    Optional<Company> findCompanyById(Long id);

    Long createCompany(Company company);

    void deleteCompany(Long id);

    void updateCompany(Company company);
}
