package edu.uoc.epcsd.showcatalog;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.name;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "edu.uoc.epcsd.showcatalog", importOptions = { ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class })
public class ShowCatalogApplicationArchitectureTest {

    @ArchTest
    static final ArchRule domain_service_with_spring_annotation =
            classes()
                    .that().resideInAPackage("..domain.service..")
                    .and().areAnnotatedWith(Service.class)
                    .should().haveSimpleNameEndingWith("ServiceImpl");

    @ArchTest
    static final ArchRule onion_architecture_is_respected =
            onionArchitecture()
                    .domainModels("edu.uoc.epcsd.showcatalog.domain..")
                    .domainServices("edu.uoc.epcsd.showcatalog.domain.service..")
                    .applicationServices("edu.uoc.epcsd.showcatalog.application..")
                    .ignoreDependency(name("edu.uoc.epcsd.showcatalog.WebConfig"), DescribedPredicate.alwaysTrue())
                    .adapter("infrastructure", "edu.uoc.epcsd.showcatalog.infrastructure..")
                    .adapter("persistence", "edu.uoc.epcsd.showcatalog.infrastructure.repository..")
                    .adapter("kafka", "edu.uoc.epcsd.showcatalog.infrastructure.kafka..")
                    .adapter("rest", "edu.uoc.epcsd.showcatalog.application.rest..");

}
