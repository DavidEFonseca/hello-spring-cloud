package helloworld;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.jayway.jsonpath.JsonPath;
import com.orange.clara.cloud.boot.ssl.DefaultTrustStoreAppender;
import com.orange.clara.cloud.boot.ssl.TrustStoreInfo;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static final String SSL_TRUST_STORE_SYSTEM_PROPERTY = "javax.net.ssl.trustStore";
    public static final String SSL_TRUST_STORE_PASSWORD_SYSTEM_PROPERTY = "javax.net.ssl.trustStorePassword";

    public static void main(String[] args) {
    	
    	// add the compose certificates
    	addCertificates();
        SpringApplication.run(Application.class, args);
    }

	private static void addCertificates() {
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
    	
    	List<String> base64certs = JsonPath.read(VCAP_SERVICES, "$..ca_certificate_base64");
    	
    	List<String> certs = new ArrayList<String>();
    	for (String base64cert : base64certs) {
    		// FIXME this doesn't work on Java 7 - use apache commons instead
    		certs.add(new String(DatatypeConverter.parseBase64Binary(base64cert)));
    	}
    	
    	DefaultTrustStoreAppender trustStoreAppender = new DefaultTrustStoreAppender();
	    final TrustStoreInfo trustStoreInfo = trustStoreAppender.append(certs);
	    System.out.println(trustStoreInfo.getTrustStorefFile().getAbsolutePath());
    	System.setProperty(SSL_TRUST_STORE_SYSTEM_PROPERTY, trustStoreInfo.getTrustStorefFile().getAbsolutePath());
	    System.setProperty(SSL_TRUST_STORE_PASSWORD_SYSTEM_PROPERTY, trustStoreInfo.getPassword());
	}
    
}
