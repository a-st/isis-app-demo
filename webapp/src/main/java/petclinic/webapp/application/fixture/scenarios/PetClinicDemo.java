package petclinic.webapp.application.fixture.scenarios;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.apache.isis.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.isis.testing.fixtures.applib.modules.ModuleWithFixturesService;

import petclinic.modules.pets.dom.pet.PetRepository;
import petclinic.modules.pets.fixture.pet.Pet_persona;
import petclinic.modules.pets.fixture.petowner.PetOwner_persona;
import petclinic.modules.visits.dom.visit.Visit;
import petclinic.modules.visits.dom.visit.VisitRepository;

public class PetClinicDemo extends FixtureScript {

    @Override
    protected void execute(final ExecutionContext ec) {
        ec.executeChildren(this, moduleWithFixturesService.getTeardownFixture());
        ec.executeChild(this, new Pet_persona.PersistAll());
        ec.executeChild(this, new PetOwner_persona.PersistAll());
        ec.executeChild(this, new FixtureScript() {
            @Override
            protected void execute(final ExecutionContext executionContext) {
                petRepository.findAll().forEach(pet -> {
                    final Visit visit = new Visit(pet, LocalDateTime.now(), "reason");
                    visitRepository.saveAndFlush(visit);
                });
            }
        });
    }

    @Inject ModuleWithFixturesService moduleWithFixturesService;
    @Inject PetRepository petRepository;
    @Inject VisitRepository visitRepository;

}
