package com.technical.test.prices;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.technical.test.prices", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule onionLayersAreRespected = onionArchitecture()
            .domainModels("..domain.model..", "..domain.exception..")
            .domainServices("..domain.service..", "..domain.repository..")
            .applicationServices("..application..")
            .adapter("rest", "..infrastructure.rest..")
            .adapter("persistence", "..infrastructure.persistence..")
            .adapter("config", "..infrastructure.config..");

    @ArchTest
    static final ArchRule coreIsFrameworkFree = noClasses()
            .that().resideInAnyPackage("..domain..", "..application..")
            .should().dependOnClassesThat().resideInAnyPackage("org.springframework..", "jakarta..")
            .because("domain and application are the core of the onion and must not depend on any framework");
}
