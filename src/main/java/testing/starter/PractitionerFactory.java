package testing.starter;

import org.hl7.fhir.r4.model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PractitionerFactory {

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

  public static Practitioner createExample() {
    Practitioner newPractitioner = new Practitioner();

    // Name
    newPractitioner.addName().setFamily("Abal Medina").addGiven("Juan").addGiven("Mariano");

    // Birthdate
    Date birthdate = new GregorianCalendar(1994, Calendar.MARCH, 22).getTime();
    newPractitioner.setBirthDate(birthdate);

    // Gender
    newPractitioner.setGender(Enumerations.AdministrativeGender.MALE);

    // Documento Identidad
    Identifier dni = createDNI();
    newPractitioner.addIdentifier(dni);

    // Mothers Name
    Extension mothersFamily = new Extension();
    mothersFamily.setUrl("http://hl7.org/fhir/StructureDefinition/humanname-mothers-family")
      .setValue(new StringType("Amura"));
    newPractitioner.getName().get(0).getFamilyElement().addExtension(mothersFamily);

    // Marital Status
    CodeableConcept marital = new CodeableConcept();
    Coding statusCoding = marital.addCoding();
    statusCoding.setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/practitioner-marital-status")
      .setCode("S")
      .setDisplay("Single");
    newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/practitioner-marital-status")
      .setValue(marital);

    // Nationality
    CodeableConcept nationality = new CodeableConcept();
    nationality.addCoding().setCode("Argentinian");
    newPractitioner.addExtension()
      .setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-citizenship")
      .setValue(nationality);

    // Contact phone
    ContactPoint telecom = newPractitioner.addTelecom();
    telecom.setSystem(ContactPoint.ContactPointSystem.PHONE)
      .setUse(ContactPoint.ContactPointUse.HOME)
      .setValueElement(new StringType("12341234"));

    // Address
    Address address = newPractitioner.addAddress();
    address.setCity("Beccar").setState("Buenos Aires").setCountry("AR").setPostalCode("1643");
    List<StringType> addressLine = address.getLine();
    addressLine.add(new StringType("D"));
    addressLine.add(new StringType("2"));
    addressLine.add(new StringType("A"));
    addressLine.add(new StringType("C"));
    addressLine.add(new StringType("Padre Acevedo"));
    addressLine.add(new StringType("1777"));

    // Mail
    ContactPoint mail = newPractitioner.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL);
    mail.setUse(ContactPoint.ContactPointUse.HOME);
    mail.setValue("divitoivan@gmail.com");

    // Profession
    newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-profession")
      .setValue(new CodeableConcept().setText("Kinesiologo"));

    // Titulo
    Practitioner.PractitionerQualificationComponent titulo = newPractitioner.addQualification();
    titulo.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/qualification-type")
      .setValue(new Coding().setCode("degree"));
    titulo.getCode().addCoding().setCode("TO");
    titulo.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/qualification-name")
      .setValue(new StringType("Terapista Ocupacional"));
    titulo.getIssuer().setDisplay("Colegio de terapistas ocupacionales");
    titulo.addIdentifier().addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/identifier-image")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));

    // Especialidad
    Practitioner.PractitionerQualificationComponent especialidad = newPractitioner.addQualification();
    especialidad.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/qualification-type")
      .setValue(new Coding().setCode("speciality"));
    especialidad.getCode().addCoding().setCode("Rehab");
    especialidad.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/qualification-name")
      .setValue(new StringType("Especialista en rehabilitacion"));
    especialidad.getIssuer().setDisplay("Colegio de terapistas ocupacionales");
    especialidad.addIdentifier().addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/identifier-image")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));

    // Matricula
    Practitioner.PractitionerQualificationComponent matricula = newPractitioner.addQualification();
    matricula.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/qualification-type")
      .setValue(new Coding().setCode("registration"));
    matricula.getCode().addCoding().setCode("TO");
    Identifier matriculaIdentifier = matricula.addIdentifier();
    matriculaIdentifier.setValue("123456").addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/identifier-place")
      .setValue(new Reference().setDisplay("Ciudad de Buenos Aires"));
    matriculaIdentifier.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/identifier-image")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));
    Date beginDate = new GregorianCalendar(2010, Calendar.MARCH, 20).getTime();
    Date endDate = new GregorianCalendar(2023, Calendar.JUNE, 30).getTime();
    matricula.getPeriod().setStart(beginDate).setEnd(endDate);

    // Idiomas
    newPractitioner.addCommunication().setText("Español").addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/communication-level")
      .setValue(new CodeableConcept().setText("nativo"));
    newPractitioner.addIdentifier().setUse(Identifier.IdentifierUse.OFFICIAL).setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/system-cuit")
      .setValue("15-12344123-8")
      .addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/identifier-image")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));

    // Empleos Anteriores
    Extension empleosAnteriores = newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-previus-work");
    beginDate = new GregorianCalendar(1990, Calendar.FEBRUARY, 10).getTime();
    endDate = new GregorianCalendar(1995, Calendar.SEPTEMBER, 22).getTime();
    empleosAnteriores.addExtension().setUrl("period").setValue(new Period().setStart(beginDate).setEnd(endDate));
    empleosAnteriores.addExtension().setUrl("organization").setValue(new Reference().setDisplay("Banco Nacion"));
    empleosAnteriores.addExtension().setUrl("department").setValue(new Reference().setDisplay("Cuentas"));
    empleosAnteriores.addExtension().setUrl("job-position").setValue(new CodeableConcept().setText("Jefe de Area"));
    empleosAnteriores.addExtension().setUrl("job-description").setValue(new StringType("Esta es la descripcion de la posicion"));

    // Razones sociales
    Extension razonesSociales = newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-current-work")
      .addExtension().setUrl("employer-relation");
    razonesSociales.addExtension().setUrl("employer-organization").setValue(new Reference().setReference("RSC"));
    razonesSociales.addExtension().setUrl("worker-identifier").setValue(
      new Identifier().setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/system-sc-bejerman").setValue("asd123"));
    razonesSociales.addExtension().setUrl("worker-identifier").setValue(
      new Identifier().setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/system-sc-tango").setValue("dsa312"));
    razonesSociales.addExtension().setUrl("worker-identifier").setValue(
      new Identifier().setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/system-sc-employee-management").setValue("qwe123"));

    // Lugar trabajo
    Extension currentWork = newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-current-work");
    currentWork.addExtension().setUrl("work-location").setValue(new Reference("Catamarca"));
    currentWork.addExtension().setUrl("legal-relation").setValue(new CodeableConcept().addCoding().setCode("Monotributista"));

    // Egreso/Ingreso
    beginDate = new GregorianCalendar(2016, Calendar.APRIL, 5).getTime();
    endDate = new GregorianCalendar(2018, Calendar.FEBRUARY, 8).getTime();
    currentWork.addExtension().setUrl("period").setValue(new Period().setStart(beginDate).setEnd(endDate));
    currentWork.addExtension().setUrl("ingress-position").setValue(new CodeableConcept().setText("Analista de cuentas"));
    currentWork.addExtension().setUrl("replaced-person").setValue(new Reference().setDisplay("pepe"));
    currentWork.addExtension().setUrl("direct-boss").setValue(new Reference().setDisplay("pepe"));
    currentWork.addExtension().setUrl("discharge-reason").setValue(new CodeableConcept().setText("Cambio de trabajo"));

    Identifier vaccination = new Identifier();
    vaccination.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/identifier-image")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));
    newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-vaccination")
      .setValue(vaccination);

    newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-curriculum-vitae")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));

    newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-professional-responsibility-policy")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));

    newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-professional-enrollment-certificate")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));

    newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-professional-application-form")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));

    newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-tax-position-certificate")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));

    newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-tax-exception-certificate")
      .setValue(new Attachment().setUrl("https://cdn.lavoz.com.ar/sites/default/files/styles/width_1072/public/nota_periodistica/dni1_0.jpg"));

    // Ultima entrevista
    Extension interview = newPractitioner.addExtension().setUrl("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-work-interview");
    Date date = new GregorianCalendar(2008, Calendar.JULY, 22).getTime();
    interview.addExtension().setUrl("date").setValue(new DateType(date));
    interview.addExtension().setUrl("result").setValue(new CodeableConcept().setText("no aceptado"));
    interview.addExtension().setUrl("reason").setValue(new CodeableConcept().setText("no cumplia los requisitos"));

    // Id federacion laba
    newPractitioner.addIdentifier().setSystem("http://platform.lab-a.com.ar/fhir/StructureDefinition/person-laba-identifier")
      .setValue("abc123");

    return newPractitioner;
  }
}
