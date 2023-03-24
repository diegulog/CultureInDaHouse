package edu.uoc.epcsd.showcatalog.domain.service;

import edu.uoc.epcsd.showcatalog.domain.Category;
import edu.uoc.epcsd.showcatalog.domain.Company;
import edu.uoc.epcsd.showcatalog.domain.Performance;
import edu.uoc.epcsd.showcatalog.domain.Show;
import edu.uoc.epcsd.showcatalog.domain.repository.CategoryRepository;
import edu.uoc.epcsd.showcatalog.domain.repository.ShowRepository;
import edu.uoc.epcsd.showcatalog.infrastructure.kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CatalogServiceImpl implements CatalogService {

    private static final String EL_ACTO = "El acto ";
    private final ShowRepository showRepository;

    private final CategoryRepository categoryRepository;

    private final KafkaTemplate<String, Show> kafkaTemplate;

    @Override
    public List<Show> findAllShows() {
        return showRepository.findAllShows();
    }

    @Override
    public Optional<Show> findShowById(Long id) {

        return showRepository.findShowById(id);
    }

    @Override
    public List<Show> findShowsByName(String name) {
        return showRepository.findShowsByName(name);
    }

    @Override
    public List<Show> findShowsByCategory(Long id) {

        return showRepository.findShowsByCategory(id);

    }

    @Override
    public Set<Performance> getShowPerformances(Long id) {
        Set<Performance> performance = null;
        Optional<Show> show = showRepository.findShowById(id);
        if(show.isPresent()) {
            performance = show.get().getPerformances();
        }
        return performance;
    }

    @Override
    public Long createShow(Show show) {
        Long id = null;
        Optional<Category> optCategory = categoryRepository.findCategoryById(show.getCategory().getId());

        if(optCategory.isPresent()) {
            show.setCategory(optCategory.get());
            id = showRepository.createShow(show);

            Optional<Show> optShow = showRepository.findShowById(id);

            if (optShow.isPresent()) {
                log.trace("Sending " + show + " to " + KafkaConstants.SHOW_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD + " topic.");
                kafkaTemplate.send(KafkaConstants.SHOW_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD, optShow.get());
            }else{
                log.error(EL_ACTO + id + " no existe y no se ha podido enviar.");
            }
        }else{
            log.error(EL_ACTO + id + " no ha sido creado ya que la categoria " + show.getCategory().getId() + " no existe.");
        }

        return id;
    }

    @Override
    public void updateShow(Long id, Show newShow) {
        Optional<Show> oldShow = showRepository.findShowById(id);
        if(oldShow.isPresent()) {
            oldShow.get().setDescription(newShow.getDescription());
            oldShow.get().setImage(newShow.getImage());
            oldShow.get().setName(newShow.getName());
            oldShow.get().setCapacity(newShow.getCapacity());
            oldShow.get().setDuration(newShow.getDuration());
            oldShow.get().setOnSaleDate(newShow.getOnSaleDate());
            oldShow.get().setPrice(newShow.getPrice());
            oldShow.get().setStatus(newShow.getStatus());
            Optional<Category> optCategory = categoryRepository.findCategoryById(newShow.getCategory().getId());
            if(optCategory.isPresent()) {
                oldShow.get().setCategory(optCategory.get());
            }else {
                log.error(EL_ACTO + id + " no ha sido modificado ya que la categoria " + oldShow.get().getCategory().getId() + " no existe.");
            }
            showRepository.updateShow(oldShow.get());
        } else {
            log.error("Error updating show. The show " + id.toString() + " doesn't exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The show with id " + id + " doesn't exist");
        }
    }

    @Override
    public void cancelShow(Long id) {
        Optional<Show> optShow = showRepository.findShowById(id);
        if(optShow.isPresent()) {
            Show show = optShow.get();
            show.cancel();
            showRepository.updateShow(show);
        }else{
            log.error(EL_ACTO + id + " no existe y no se ha podido cancelar.");
        }
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAllCategories();
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {

        return categoryRepository.findCategoryById(id);

    }

    @Override
    public Long createCategory(Category category) {

        category.setId(null);
        return categoryRepository.createCategory(category);
    }

    @Override
    public void deleteCategory(Long id) {

        categoryRepository.deleteCategory(id);
    }

    @Override
    public void createShowPerformance(Long id, Performance performance) {

        showRepository.addShowPerformance(id, performance);
    }
}
