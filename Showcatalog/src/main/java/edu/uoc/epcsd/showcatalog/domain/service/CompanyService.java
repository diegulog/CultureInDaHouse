package edu.uoc.epcsd.showcatalog.domain.service;

import edu.uoc.epcsd.showcatalog.domain.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    List<Company> findAllCompanies();

    Optional<Company> findCompanyById(Long id);

    Long createCompany(Company company);

    void deleteCompany(Long id);

    void updateCompany(Long id, Company company);
}
