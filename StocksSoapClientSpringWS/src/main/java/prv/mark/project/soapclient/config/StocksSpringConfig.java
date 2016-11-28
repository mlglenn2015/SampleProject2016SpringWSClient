package prv.mark.project.soapclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Spring configuration for the stock ticker client.
 *
 * @author mlglenn on 11/28/2016.
 */
@Configuration
@Profile({"local","dev","test","staging","production"})
public class StocksSpringConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("prv.mark.project.soapclient.schemas");
        return marshaller;
    }

    @Bean(name = "webServiceTemplate")
    public WebServiceTemplate webServiceTemplate() {
        return new WebServiceTemplate(marshaller(), marshaller());
    }

}
