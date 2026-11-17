package pe.utec.biblioteca.arquitectura;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Verifica automáticamente las reglas de la arquitectura hexagonal.
 * Si alguien importa algo prohibido, esta prueba falla.
 */
@AnalyzeClasses(packages = "pe.utec.biblioteca", importOptions = ImportOption.DoNotIncludeTests.class)
class ArquitecturaHexagonalTest {

    @ArchTest
    static final ArchRule el_dominio_no_depende_de_nada_externo = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "..application..", "..infrastructure..", "org.springframework..", "jakarta..")
            .because("el dominio debe ser Java puro");

    @ArchTest
    static final ArchRule la_aplicacion_no_conoce_la_infraestructura = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "..infrastructure..", "org.springframework..", "jakarta..")
            .because("los casos de uso solo hablan con puertos");

    @ArchTest
    static final ArchRule los_adaptadores_de_entrada_usan_puertos = noClasses()
            .that().resideInAPackage("..infrastructure.in..")
            .should().dependOnClassesThat().resideInAnyPackage("..application..", "..infrastructure.out..")
            .because("los adaptadores de entrada solo conocen los puertos de entrada");
}
