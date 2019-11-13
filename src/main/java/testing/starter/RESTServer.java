package testing.starter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@WebServlet(urlPatterns= {"/fhir/*"}, displayName="FHIR Server")
public class RESTServer extends RestfulServer {

  @Override
  protected void initialize() throws ServletException {
    /* Setting FHIR R4 Context*/
    setFhirContext(FhirContext.forR4());

    /* Assigning Server URL base */
    String serverBase = "http://localhost:8080/fhir";
    setServerAddressStrategy(new HardcodedServerAddressStrategy(serverBase));

    /* Setting Resource Providers*/
    List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
    resourceProviders.add(new PatientResourceProvider());
    setResourceProviders(resourceProviders);

    /* Authomatic narratives for resources that don't have one */
    getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

    /* Default JSON and Pretty Printing */
    setDefaultPrettyPrint(true);

    /* Nice HTML when request seen in browser */
    registerInterceptor(new ResponseHighlighterInterceptor());

    /* Enable CORS */
    CorsConfiguration config = new CorsConfiguration();
    CorsInterceptor corsInterceptor = new CorsInterceptor(config);
    config.addAllowedHeader("Accept");
    config.addAllowedHeader("Content-Type");
    config.addAllowedOrigin("*");
    config.addExposedHeader("Location");
    config.addExposedHeader("Content-Location");
    config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
    registerInterceptor(corsInterceptor);
  }
}
