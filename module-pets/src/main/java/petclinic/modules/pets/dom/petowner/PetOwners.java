package petclinic.modules.pets.dom.petowner;

import java.util.List;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.PriorityPrecedence;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.Query;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.persistence.jpa.applib.services.JpaSupportService;

import lombok.RequiredArgsConstructor;

import petclinic.modules.pets.types.FirstName;
import petclinic.modules.pets.types.LastName;

@DomainService(
        nature = NatureOfService.VIEW,
        logicalTypeName = "pets.PetOwners"
)
@Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class PetOwners {

    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final PetOwnerRepository petOwnerRepository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public PetOwner create(
            @LastName final String lastName,
            @FirstName final String firstName) {
        return repositoryService.persist(PetOwner.withName(lastName, firstName));
    }


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public List<PetOwner> findByLastNameLike(
            @LastName final String lastName) {
        return repositoryService.allMatches(
                Query.named(PetOwner.class, PetOwner.NAMED_QUERY__FIND_BY_LAST_NAME_LIKE)
                     .withParameter("lastName", "%" + lastName + "%"));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<PetOwner> findByLastName(
            @LastName final String lastName
            ) {
        return petOwnerRepository.findByLastNameContaining(lastName);
    }


    @Programmatic
    public PetOwner findByLastNameExact(final String lastName) {
        return petOwnerRepository.findByLastName(lastName);
    }



    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<PetOwner> listAll() {
        return petOwnerRepository.findAll();
    }




    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(PetOwner.class)
            .mapSuccess(entityManager -> {
                final TypedQuery<PetOwner> q = entityManager.createQuery(
                        "SELECT p FROM PetOwner p ORDER BY p.lastName",
                        PetOwner.class)
                    .setMaxResults(1);
                q.getResultList();
                return q;
            })
            .ifFailureFail();
    }


}
