package edu.uoc.epcsd.showcatalog;

import static org.assertj.core.api.Assertions.assertThat;

import edu.uoc.epcsd.showcatalog.domain.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class ShowCatalogApplicationTests {

    @Autowired
    CatalogService catalogService;

    @Test
    void contextLoads() {
        assertThat(catalogService).isNotNull();

    }

}
