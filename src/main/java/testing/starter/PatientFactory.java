package testing.starter;

import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import org.hl7.fhir.r4.model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PatientFactory {

  private static final int LAST_NAME_POS = 2;
  private static final int FIRST_NAME_POS = 0;
  private static final int SECOND_NAME_POS = 1;

  public static Patient create(String wholeName, Integer id) {
    Patient newPatient = new Patient();

    String[] splittedName = wholeName.split(" ");
    newPatient.addName().setFamily(splittedName[LAST_NAME_POS])
      .addGiven(splittedName[FIRST_NAME_POS]).addGiven(splittedName[SECOND_NAME_POS]);

    Identifier newIdentifier = new Identifier();
    newIdentifier.getType().addCoding().setSystem("http://hl7.org/fhir/v2/0203/").setCode("PPN");
    newIdentifier.setValue("example-identifier");
    newPatient.addIdentifier(newIdentifier);

    newPatient.setId(id.toString());

    return newPatient;
  }

  private static Identifier createDNI() {
    Identifier dni = new Identifier();
    dni.setUse(Identifier.IdentifierUse.OFFICIAL).setSystem("http://www.renaper.gob.ar/dni").setValue("38125032");

    // DNI: Archivo
    Extension dniFile = new Extension();
    dniFile.setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/identifier-image");
    Attachment dniFileAttachment = new Attachment();
    dniFileAttachment.setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg");
    dniFile.setValue(dniFileAttachment);
    dni.getExtension().add(dniFile);

    return dni;
  }


  public static Patient createExample() {
    Patient newPatient = new Patient();

    // Name
    newPatient.addName().setFamily("Abal Medina").addGiven("Juan").addGiven("Mariano");

    // Birthdate
    Date birthdate = new GregorianCalendar(1994, Calendar.MARCH, 22).getTime();
    newPatient.setBirthDate(birthdate);

    // Gender
    newPatient.setGender(Enumerations.AdministrativeGender.MALE);

    // Documento Identidad
    Identifier dni = createDNI();
    newPatient.addIdentifier(dni);

    // Mothers Name
    Extension mothersFamily = new Extension();
    mothersFamily.setUrl("http://hl7.org/fhir/StructureDefinition/humanname-mothers-family")
      .setValue(new StringType("Amura"));
    newPatient.getName().get(0).getFamilyElement().addExtension(mothersFamily);

    // Marital Status
    Coding statusCoding = newPatient.getMaritalStatus().addCoding();
    statusCoding.setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/practitioner-marital-status")
      .setCode(MaritalStatusCodesEnum.S.getCode())
      .setDisplay("Single");

    // Nationality
    CodeableConcept nationality = new CodeableConcept();
    nationality.addCoding().setCode("Argentinian");
    newPatient.addExtension()
      .setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-citizenship")
      .setValue(nationality);

    // Contact phone
    ContactPoint telecom = newPatient.addTelecom();
    telecom.setSystem(ContactPoint.ContactPointSystem.PHONE)
      .setUse(ContactPoint.ContactPointUse.HOME)
      .setValueElement(new StringType("12341234"));

    // Address
    Address address = newPatient.addAddress();
    address.setCity("Beccar").setState("Buenos Aires").setCountry("AR").setPostalCode("1643");
    List<StringType> addressLine = address.getLine();
    addressLine.add(new StringType("D"));
    addressLine.add(new StringType("2"));
    addressLine.add(new StringType("A"));
    addressLine.add(new StringType("C"));
    addressLine.add(new StringType("Padre Acevedo"));
    addressLine.add(new StringType("1777"));

    // Mail
    ContactPoint mail = newPatient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL);
    mail.setUse(ContactPoint.ContactPointUse.HOME);
    mail.setValue("divitoivan@gmail.com");

    Date deathDate = new GregorianCalendar(2018, Calendar.MARCH, 30).getTime();
    newPatient.setDeceased(new DateTimeType(deathDate));

    /***************************************** INICIO CONTACTO ***********************************************/
    Patient.ContactComponent newContact = newPatient.addContact();
    newContact.getName().setUse(HumanName.NameUse.OFFICIAL).setFamily("pepe").addGiven("pepe");
    newContact.addRelationship().setText("padre");

    Extension contactBirthdate = new Extension();
    contactBirthdate.setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-birthdate")
      .setValue(new DateType(birthdate));
    newContact.addExtension(contactBirthdate);

    Extension contactDNI = new Extension();
    contactDNI.setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-identifier");
    contactDNI.setValue(new Identifier().setValue("38125032"));

    ContactPoint contactTelecom = newContact.addTelecom();
    contactTelecom.setSystem(ContactPoint.ContactPointSystem.PHONE)
      .setUse(ContactPoint.ContactPointUse.HOME)
      .setValueElement(new StringType("12341234"));

    ContactPoint contactMail = newContact.addTelecom();
    contactMail.setUse(ContactPoint.ContactPointUse.HOME).setValue("dasd@das.com");

    Address contactAddress = new Address();
    contactAddress.setCity("ciudad de buenos aires").setPostalCode("1234");
    List<StringType> contactAddressLine = contactAddress.getLine();
    contactAddressLine.add(new StringType("calle falsa 123"));
    newContact.setAddress(contactAddress);

    Extension contactProfession = newContact.addExtension();
    contactProfession.setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-profession")
      .setValue(new CodeableConcept().setText("Contador"));
    Extension contactCurrentWork = newContact.addExtension();
    contactCurrentWork.setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-current-work");
    Extension contactEmployer = contactCurrentWork.addExtension();
    contactEmployer.setUrl("employer")
      .setValue(new Reference().setDisplay("Banco Nacion"));
    Extension jobPosition = contactCurrentWork.addExtension();
    jobPosition.setUrl("job-position")
      .setValue(new CodeableConcept().setText("Jefe de Area"));

    Address jobAddress = new Address();
    jobAddress.setCity("calle falsa 123").setPostalCode("6789");
    List<StringType> jobAddressLine = jobAddress.getLine();
    jobAddressLine.add(new StringType("calle falsa 123"));
    contactCurrentWork.addExtension().setUrl("address").setValue(jobAddress);

    newContact.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE)
      .setUse(ContactPoint.ContactPointUse.WORK).setValue("12344321");

    /***************************************** FIN CONTACTO ***********************************************/

    // Healthcare
    Extension healthcare = newPatient.addExtension();
    healthcare.setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-healthcare-payer")
      .addExtension().setUrl("provider")
      .setValue(new Reference().setReference("OSDE"));
    Extension healthcareIdentifier = healthcare.addExtension();
    Identifier afiliado = new Identifier().setValue("1234123-asdc");
    afiliado.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/identifier-image")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));
    healthcareIdentifier.setUrl("identifier").setValue(afiliado);
    // Indications
    newPatient.addIdentifier().setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/system-sc-indications")
      .setValue("123asd");
    // Lista espera
    newPatient.addIdentifier().setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/system-sc-ingress-list")
      .setValue("321asd");

    newPatient.addIdentifier().setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/system-sc-datatech")
      .setAssigner(new Reference().setDisplay("AMENABAR")).setValue("312zsd");

    newPatient.addIdentifier().setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/system-sc-datatech")
      .setAssigner(new Reference().setDisplay("RSC")).setValue("423sda");

    // Id federacion laba
    newPatient.addIdentifier().setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-laba-identifier")
      .setValue("abc123");

    return newPatient;
  }
}
