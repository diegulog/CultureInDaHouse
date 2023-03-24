package edu.uoc.epcsd.showcatalog.application.rest;

import edu.uoc.epcsd.showcatalog.application.request.CreateCompanyRequest;
import edu.uoc.epcsd.showcatalog.domain.Company;
import edu.uoc.epcsd.showcatalog.domain.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class CompanyRESTController {

    private final CompanyService companyService;

    @GetMapping("/companies")
    @ResponseStatus(HttpStatus.OK)
    public List<Company> findCompanies() {
        log.trace("findCompanies");

        return companyService.findAllCompanies();
    }

    @GetMapping("/companies/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Company> findCompanyById(@PathVariable Long id) {
        log.trace("findCompanyById");

        return companyService.findCompanyById(id).map(company -> ResponseEntity.ok().body(company))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER')")
    @PostMapping("/companies")
    public ResponseEntity<Long> createCompany(@RequestBody CreateCompanyRequest createCompanyRequest) {
        log.trace("createCompany");

        log.trace("Creating company " + createCompanyRequest);
        Long companyId = companyService.createCompany(createCompanyRequest.getCompany());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(companyId)
                .toUri();

        return ResponseEntity.created(uri).body(companyId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER')")
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Boolean> deleteCompany(@PathVariable Long id) {
        log.trace("deleteCompany");

        companyService.deleteCompany(id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER','BUSINESS_USER')")
    @PutMapping("/companies/{id}")
    public ResponseEntity<Boolean> updateCompany(@PathVariable Long id, @RequestBody CreateCompanyRequest createCompanyRequest) {
        log.trace("updateCompany");

        log.trace("updating Company: " + createCompanyRequest);

        companyService.updateCompany(id, createCompanyRequest.getCompany());

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
