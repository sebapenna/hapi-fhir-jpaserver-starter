package testing.starter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;

import java.util.Scanner;

class Main {
  private static final String OPTIONS_MESSAGE = "Ingrese:\n\t" +
    "0 - Crear Cliente;\n\t" +
    "1 - Buscar todos los Clientes;\n\t" +
    "2 - Buscar por Apellido\n\t" +
    "3 - Buscar por Nombre\n\t" +
    "4 - Imprimir ejemplo Patient\n\t" +
    "5 - Imprimir ejemplo Practitioner\n\t" +
    "6 - Salir";

  private static final String CREATE_MESSAGE = "Ingrese nombre, segundo nombre y apellido:";
  private static final String SEARCH_BY_LASTNAME_MESSAGE = "Ingrese apellido: ";
  private static final String SEARCH_BY_GIVEN_MESSAGE = "Ingrese nombre: ";

  private static final int CREATE = 0;
  private static final int SEARCH_ALL = 1;
  private static final int SEARCH_LAST_NAME = 2;
  private static final int SEARCH_GIVEN_NAME = 3;
  private static final int PRINT_EXAMPLE_PATIENT = 4;
  private static final int PRINT_EXAMPLE_PRACTITIONER = 5;
  private static final int END = 6;

  public static void main(String[] args) {
    FhirContext ctx = FhirContext.forR4();
    Integer nextId = 0;
    String serverBase = "http://localhost:8080/hapi-fhir-jpaserver/fhir/";
//    String serverBase = "http://localhost:8080/";

    IGenericClient client = ctx.newRestfulGenericClient(serverBase);

    Scanner sc = new Scanner(System.in);
    System.out.println(OPTIONS_MESSAGE);

    int option = sc.nextInt();
    sc.nextLine();  // Clean scanner after next int

    while (option != END) {
      switch (option) {
        case CREATE: {
          System.out.println(CREATE_MESSAGE);

          // Client must be inputted as: LASTNAME FIRSTNAME SECONDNAME
          Patient newPatient = PatientFactory.create(sc.nextLine(), nextId++);

          printPatient(ctx, client, newPatient);

          break;
        }
        case SEARCH_ALL: {
          Bundle response = client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
          if (response.getEntry().isEmpty()) {
            System.out.println("No patients in data base");
          } else {
            System.out.println("Information about registered patients:\n");
            printResponseInformation(response);
          }
          break;
        }
        case SEARCH_LAST_NAME: {
          System.out.println(SEARCH_BY_LASTNAME_MESSAGE);
          String to_match = sc.nextLine();
          Bundle response = client.search().forResource(Patient.class).where(Patient.FAMILY.matches().value(to_match)).returnBundle(Bundle.class).execute();
          if (response.getEntry().isEmpty()) {
            System.out.println("No patients matched " + to_match);
          } else {
            System.out.println("Information about patients whose Last Name is: " + to_match);
            printResponseInformation(response);
          }
          break;
        }
        case SEARCH_GIVEN_NAME: {
          System.out.println(SEARCH_BY_GIVEN_MESSAGE);
          String to_match = sc.nextLine();
          Bundle response = client.search().forResource(Patient.class).where(Patient.GIVEN.matches().value(to_match)).returnBundle(Bundle.class).execute();
          if (response.getEntry().isEmpty()) {
            System.out.println("No patients matched " + to_match);
          } else {
            System.out.println("Information about patients whose Name (First or Second) is: " + to_match);
            printResponseInformation(response);
          }
          break;
        }
        case PRINT_EXAMPLE_PATIENT: {
          Patient newPatient = PatientFactory.createExample();
          printPatient(ctx, client, newPatient);
          break;
        }
        case PRINT_EXAMPLE_PRACTITIONER: {
          Practitioner newPractitioner = PractitionerFactory.createExample();
          printPractitioner(ctx, client, newPractitioner);
          break;
        }
      }
      System.out.println(OPTIONS_MESSAGE);
      option = sc.nextInt();
      sc.nextLine();  // Clean scanner after next int
    }
  }

  private static void printPatient(FhirContext ctx, IGenericClient client, Patient newPatient) {
    IParser jsonParser = ctx.newJsonParser();
    jsonParser.setPrettyPrint(true);
    String patientAsJSON = jsonParser.encodeResourceToString(newPatient);
    System.out.println(patientAsJSON);

    MethodOutcome outcome = client.create().resource(newPatient).prettyPrint().encodedJson().execute();
    System.out.println("Got ID: " + outcome.getId().toString());
  }

  private static void printPractitioner(FhirContext ctx, IGenericClient client, Practitioner newPractitioner) {
    IParser jsonParser = ctx.newJsonParser();
    jsonParser.setPrettyPrint(true);
    String PractitionerAsJSON = jsonParser.encodeResourceToString(newPractitioner);
    System.out.println(PractitionerAsJSON);

    MethodOutcome outcome = client.create().resource(newPractitioner).prettyPrint().encodedJson().execute();
    System.out.println("Got ID: " + outcome.getId().toString());
  }

  private static void printResponseInformation(Bundle response) {
    for (Bundle.BundleEntryComponent next : response.getEntry()) {
      Patient patient = (Patient) next.getResource();
      System.out.println("ID: " + patient.getId());
      System.out.println("Nombre: " + patient.getName().get(0).getGiven().get(0) + " " + patient.getName().get(0).getGiven().get(1) + " " + patient.getName().get(0).getFamily());
    }
  }

}