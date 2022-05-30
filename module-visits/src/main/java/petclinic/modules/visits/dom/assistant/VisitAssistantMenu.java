package petclinic.modules.visits.dom.assistant;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.factory.FactoryService;

import lombok.RequiredArgsConstructor;

@DomainService(nature = NatureOfService.VIEW, logicalTypeName = "assistant.VisitAssistantMenu")
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class VisitAssistantMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public VisitAssistant open() {
        return factoryService.viewModel(VisitAssistant.class);
    }

    @Inject
    private FactoryService factoryService;
}
