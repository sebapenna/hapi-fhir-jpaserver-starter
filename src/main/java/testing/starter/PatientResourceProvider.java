package testing.starter;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientResourceProvider implements IResourceProvider {

  private ArrayList<Patient> _patients;

  public PatientResourceProvider() {
    _patients = new ArrayList<>();
  }

  @Override
  public Class<Patient> getResourceType() {
    return Patient.class;
  }

  @Read
  public Patient getResourceById(@IdParam IIdType wanted_id) {
    return new Patient();
  }

  @Create
  public MethodOutcome createPatient(@ResourceParam Patient new_patient) {
    new_patient.setId(((Integer) _patients.size()).toString());
    _patients.add(new_patient);
    System.out.println("Patients on DataBase: " +  _patients.size());

    MethodOutcome ret = new MethodOutcome();
    ret.setId(new IdType(String.valueOf(new_patient.getClass())));
    return ret;
  }

  @Search
  public List<Patient> getAllPatients() {
    return new ArrayList<>(_patients);
  }

  @Search
  public List<Patient> getPatientByLastName(@RequiredParam(name = Patient.SP_FAMILY) String last_name) {
    List<Patient> matched_patients = new ArrayList<>();
    for (Patient patient : _patients)
      if (last_name.compareTo(patient.getName().get(0).getFamily()) == 0)
        matched_patients.add(patient);
    return matched_patients;
  }

}
