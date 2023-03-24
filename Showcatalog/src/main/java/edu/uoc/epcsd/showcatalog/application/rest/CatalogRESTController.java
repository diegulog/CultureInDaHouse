package edu.uoc.epcsd.showcatalog.application.rest;

import edu.uoc.epcsd.showcatalog.application.request.CreateCategoryRequest;
import edu.uoc.epcsd.showcatalog.application.request.CreatePerformanceRequest;
import edu.uoc.epcsd.showcatalog.application.request.CreateShowRequest;
import edu.uoc.epcsd.showcatalog.domain.Category;
import edu.uoc.epcsd.showcatalog.domain.Performance;
import edu.uoc.epcsd.showcatalog.domain.Show;
import edu.uoc.epcsd.showcatalog.domain.Status;
import edu.uoc.epcsd.showcatalog.domain.service.CatalogService;
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
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class CatalogRESTController {

    private final CatalogService catalogService;
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> findCategories() {
        log.trace("findCategories");

        return catalogService.findAllCategories();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER')")
    @GetMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Category> findCategoryById(@PathVariable Long id) {
        log.trace("findCategoryById");

        return catalogService.findCategoryById(id).map(category -> ResponseEntity.ok().body(category))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER')")
    @PostMapping("/categories")
    public ResponseEntity<Long> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        log.trace("createCategory");

        log.trace("Creating category " + createCategoryRequest);
        Long categoryId = catalogService.createCategory(createCategoryRequest.getCategory());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryId)
                .toUri();

        return ResponseEntity.created(uri).body(categoryId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER')")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable Long id) {
        log.trace("deleteCategory");

        catalogService.deleteCategory(id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping(value = "/shows")
    public ResponseEntity<List<Show>> findShows(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "categoryId", required = false) Long categoryId) {
        log.trace("findShows");
        List<Show> shows;
        if (name == null || name.isEmpty()) {
            if (categoryId == null) {
                shows = catalogService.findAllShows();
            } else {
                shows =  catalogService.findShowsByCategory(categoryId);
            }
        } else {
            shows = catalogService.findShowsByName(name);
        }
        boolean adminOrBusiness = getContext().getAuthentication().getAuthorities().stream().
                anyMatch(it -> it.getAuthority().equals("ADMIN_USER") || it.getAuthority().equals("BUSINESS_USER"));

        // si es un usuario estandar solo muestra los shows ABIERTOS O CANCELADOS
        return ResponseEntity.ok().body(shows.stream().filter(it ->
                adminOrBusiness || it.getStatus() != Status.CREATED).collect(Collectors.toList()));
    }



    @PreAuthorize("hasAnyAuthority('ADMIN_USER','BUSINESS_USER')")
    @GetMapping("/shows/{id}")
    public ResponseEntity<Show> getShowDetails(@PathVariable Long id) {
        log.trace("getShowDetails");

        return catalogService.findShowById(id).map(show -> ResponseEntity.ok().body(show))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/shows/{id}/performances")
    public ResponseEntity<Set<Performance>> getShowPerformances(@PathVariable Long id) {
        log.trace("getShowPerformances");

        return ResponseEntity.ok().body(catalogService.getShowPerformances(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER','BUSINESS_USER')")
    @PostMapping("/shows")
    public ResponseEntity<Long> createShow(@RequestBody CreateShowRequest createShowRequest) {
        log.trace("createShow");

        Long id = catalogService.createShow(createShowRequest.getShow());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).body(id);
    }

    @PutMapping("/shows/{id}")
    public ResponseEntity<Boolean> editShow(@PathVariable Long id, @RequestBody CreateShowRequest createShowRequest) {
        log.trace("createShow");
        catalogService.updateShow(id, createShowRequest.getShow());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_USER','BUSINESS_USER')")
    @PutMapping("/shows/{id}/performance")
    public ResponseEntity<Boolean> createPerformance(@PathVariable Long id, @RequestBody CreatePerformanceRequest createPerformanceRequest) {
        log.trace("createPerformance");

        catalogService.createShowPerformance(id, createPerformanceRequest.getPerformance());

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN_USER','BUSINESS_USER')")
    @DeleteMapping("/shows/{id}")
    public ResponseEntity<Boolean> cancelShow(@PathVariable Long id) {
        catalogService.cancelShow(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
