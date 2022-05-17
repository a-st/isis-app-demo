package petclinic.modules.pets.fixture.petowner;

import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.testing.fixtures.applib.personas.PersonaWithBuilderScript;
import org.apache.isis.testing.fixtures.applib.personas.PersonaWithFinder;
import org.apache.isis.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import petclinic.modules.pets.dom.petowner.PetOwner;
import petclinic.modules.pets.dom.petowner.PetOwners;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PetOwner_persona
implements PersonaWithBuilderScript<PetOwner, PetOwnerBuilder>, PersonaWithFinder<PetOwner> {

    JONES("Jones"),
    FARRELL("Farrell"),
    UNDERHILL("Underhill"),
    FORD("Ford"),
    YOUNGS("Youngs"),
    MAY("May"),
    GENGE("Genge"),
    EWELS("Ewels"),
    VUNIPOLA("Vunipola"),
    ITOJE("Itoje");

    @Getter
    private final String name;

    @Override
    public PetOwnerBuilder builder() {
        return new PetOwnerBuilder().setName(name);
    }

    @Override
    public PetOwner findUsing(final ServiceRegistry serviceRegistry) {
        PetOwners petOwners = serviceRegistry.lookupService(PetOwners.class).orElse(null);
        return petOwners.findByLastNameExact(name);
    }

    public static class PersistAll
    extends PersonaEnumPersistAll<PetOwner, PetOwner_persona, PetOwnerBuilder> {

        public PersistAll() {
            super(PetOwner_persona.class);
        }
    }
}
