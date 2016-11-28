package prv.mark.project.soapclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Test configuration class for the StocksSoapClientSpringWs application.
 *
 * @author mlglenn.
 */
@Configuration
//@Import({TestDataConfig.class})
@ComponentScan(basePackages = {"prv.mark.project.soapclient"})
@PropertySources(value = {
        @PropertySource("classpath:/client-TEST.properties")
})
@Profile("test")
public class StocksSpringTestConfig {

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
