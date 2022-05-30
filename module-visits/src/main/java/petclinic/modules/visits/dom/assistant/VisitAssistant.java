package petclinic.modules.visits.dom.assistant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Snapshot;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.jaxb.PersistentEntitiesAdapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import petclinic.modules.pets.dom.pet.Pet;
import petclinic.modules.visits.dom.visit.Visit;
import petclinic.modules.visits.dom.visit.VisitRepository;

@XmlRootElement
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@DomainObject(nature = Nature.VIEW_MODEL, logicalTypeName = "assistant.VisitAssistant", editing = Editing.ENABLED)
@DomainObjectLayout(bookmarking = BookmarkPolicy.NEVER)
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class VisitAssistant {

    @Inject
    @XmlTransient
    @Property(snapshot = Snapshot.EXCLUDED)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private VisitRepository visitRepository;

    @XmlJavaTypeAdapter(PersistentEntitiesAdapter.class)
    private List<Visit> visits = new ArrayList<>();

    @Property(editing = Editing.ENABLED, optionality = Optionality.MANDATORY)
    private Pet pet;

    @Property(editing = Editing.ENABLED, optionality = Optionality.MANDATORY)
    private SampleEnum sampleEnum;

    public enum SampleEnum {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
    }

    public List<SampleEnum> choicesSampleEnum() {
        return Arrays.asList(SampleEnum.values());
    }

    public String title() {
        return "Assistant";
    }

    @PropertyLayout(hidden = Where.ANYWHERE)
    private int step = 1;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public VisitAssistant previous() {
        step--;
        return this;
    }

    public String disablePrevious() {
        return step == 1 ? "Kein vorheriger Schritt" : null;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public VisitAssistant next() {
        step++;
        return this;
    }

    public String disableNext() {
        return step == 2 ? "Kein nächster Schritt" : null;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
    public VisitAssistant close() {
        return null;
    }

    public List<Pet> choicesPet() {
        return this.visits.stream().map(Visit::getPet).collect(Collectors.toList());
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(associateWith = "visits")
    public VisitAssistant add(final List<Visit> visits) {
        this.visits.addAll(visits);
        return this;
    }

    public List<Visit> autoComplete0Add(@MinLength(1) final String search) {
        return visitRepository.findAll().stream().filter(visit -> visit.title().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, choicesFrom = "visits")
    public VisitAssistant remove(final List<Visit> removedVisits) {
        removedVisits.forEach(visit -> log.info("Removing {}", visit.title()));
        this.visits.removeAll(removedVisits);
        return this;
    }

    public String disableRemove() {
        return this.visits.isEmpty() ? "No items to remove" : null;
    }

    public List<Visit> choices0Remove() {
        return this.visits;
    }

    public boolean hideVisits() {
        return step != 1;
    }

    public boolean hidePet() {
        return step != 2;
    }

    public boolean hideSampleEnum() {
        return step != 2;
    }

}
