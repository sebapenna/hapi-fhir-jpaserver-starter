package testing.starter;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Practitioner;

import java.util.ArrayList;
import java.util.List;

public class PractitionerResourceProvider implements IResourceProvider {

  private ArrayList<Practitioner> practitioners;

  PractitionerResourceProvider() {
    practitioners = new ArrayList<>();
  }

  @Override
  public Class<Practitioner> getResourceType() {
    return Practitioner.class;
  }

  @Read
  public Practitioner getResourceById(@IdParam IdType wantedId) {
    for (Practitioner practitioner : practitioners)
      if (practitioner.getIdElement().getIdPart().compareTo(wantedId.getIdPart()) == 0)
        return practitioner;
    return new Practitioner();
  }

  @Create
  public MethodOutcome createPractitioner(@ResourceParam Practitioner newPractitioner) {
    newPractitioner.setId(((Integer) practitioners.size()).toString());
    practitioners.add(newPractitioner);

    MethodOutcome outcome = new MethodOutcome();
    outcome.setId(new IdType(String.valueOf(newPractitioner.getClass())));
    return outcome;
  }

  @Search
  public List<Practitioner> getAllPractitioners() {
    return new ArrayList<>(practitioners);
  }

  @Search
  public List<Practitioner> getPractitionerByLastName(@RequiredParam(name = Practitioner.SP_FAMILY) String lastName) {
    List<Practitioner> matchedPractitioners = new ArrayList<>();
    for (Practitioner practitioner : practitioners)
      if (lastName.compareTo(practitioner.getName().get(0).getFamily()) == 0)
        matchedPractitioners.add(practitioner);
    return matchedPractitioners;
  }

  @Search
  public List<Practitioner> getPractitionerByFirstName(@RequiredParam(name = Practitioner.SP_GIVEN) String first_name) {
    List<Practitioner> matchedPractitioners = new ArrayList<>();
    for (Practitioner practitioner : practitioners)
      if (practitioner.getName().get(0).getGivenAsSingleString().contains(first_name))
        matchedPractitioners.add(practitioner);
    return matchedPractitioners;
  }

}
