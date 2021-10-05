package petclinic.modules.pets.dom.petowner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.types.PetName;

@Action(
        semantics = SemanticsOf.IDEMPOTENT,
        commandPublishing = Publishing.ENABLED,
        executionPublishing = Publishing.ENABLED,
        choicesFrom = "pets"
)
@ActionLayout(associateWith = "pets", sequence = "2")
@RequiredArgsConstructor
public class PetOwner_removePets {

    private final PetOwner petOwner;

    public PetOwner act(final List<Pet> pets) {
        pets.forEach(repositoryService::remove);
        return petOwner;
    }
    public String disableAct() {
        return petRepository.findByPetOwner(petOwner).isEmpty() ? "No pets" : null;
    }

    @Inject PetRepository petRepository;
    @Inject RepositoryService repositoryService;
}
