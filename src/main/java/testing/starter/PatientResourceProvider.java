package testing.starter;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientResourceProvider implements IResourceProvider {

  private ArrayList<Patient> patients;

  PatientResourceProvider() {
    patients = new ArrayList<>();
  }

  @Override
  public Class<Patient> getResourceType() {
    return Patient.class;
  }

  @Read
  public Patient getResourceById(@IdParam IdType wantedId) {
    for (Patient patient : patients)
      if (patient.getIdElement().getIdPart().compareTo(wantedId.getIdPart()) == 0)
        return patient;
    return new Patient();
  }

  @Create
  public MethodOutcome createPatient(@ResourceParam Patient newPatient) {
    newPatient.setId(((Integer) patients.size()).toString());
    patients.add(newPatient);

    MethodOutcome outcome = new MethodOutcome();
    outcome.setId(new IdType(String.valueOf(newPatient.getClass())));
    return outcome;
  }

  @Search
  public List<Patient> getAllPatients() {
    return new ArrayList<>(patients);
  }

  @Search
  public List<Patient> getPatientByLastName(@RequiredParam(name = Patient.SP_FAMILY) String lastName) {
    List<Patient> matchedPatients = new ArrayList<>();
    for (Patient patient : patients)
      if (lastName.compareTo(patient.getName().get(0).getFamily()) == 0)
        matchedPatients.add(patient);
    return matchedPatients;
  }

  @Search
  public List<Patient> getPatientByFirstName(@RequiredParam(name = Patient.SP_GIVEN) String first_name) {
    List<Patient> matchedPatients = new ArrayList<>();
    for (Patient patient : patients)
      if (patient.getName().get(0).getGivenAsSingleString().contains(first_name))
        matchedPatients.add(patient);
    return matchedPatients;
  }

}
