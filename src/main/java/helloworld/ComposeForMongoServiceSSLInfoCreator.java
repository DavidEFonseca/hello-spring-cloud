package helloworld;

import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import org.springframework.cloud.service.common.MongoServiceInfo;

import com.orange.clara.cloud.boot.ssl.CertificateFactory;
import com.orange.clara.cloud.boot.ssl.DefaultTrustStoreAppender;
import com.orange.clara.cloud.boot.ssl.TrustStoreInfo;

public class ComposeForMongoServiceSSLInfoCreator extends CloudFoundryServiceInfoCreator<MongoServiceInfo> {
	
    public static final String SSL_TRUST_STORE_SYSTEM_PROPERTY = "javax.net.ssl.trustStore";
    public static final String SSL_TRUST_STORE_PASSWORD_SYSTEM_PROPERTY = "javax.net.ssl.trustStorePassword";

    public ComposeForMongoServiceSSLInfoCreator() {
        super(new Tags(), MongoServiceInfo.MONGODB_SCHEME);
    }

    @Override
    public boolean accept(Map<String, Object> serviceData) {
        boolean result = false;
        // Don't really like using the label as the determining factor but that is the only
        // unique attribute to identify the service with.
        Object obj = serviceData.get("label");
        if (obj instanceof String) {
            String label = (String) obj;
            result = "compose-for-mongodb".equals(label);
        }
        return result;
    }

    @Override
    public MongoServiceInfo createServiceInfo(final Map<String, Object> serviceData) {
        String id = null;
        String uri = null;
        Object credObject = serviceData.get("credentials");
        Object idObj = serviceData.get("name");
        if (idObj instanceof String) {
            id = (String) idObj;
        }
        if (credObject instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked")
			Map<String, Object> credentials = (Map<String, Object>) credObject;
            Object uriObj = credentials.get("uri");
            if (uriObj instanceof String) {
                uri = (String) uriObj;
                System.out.println((String)uri);
            }
            Object base64cert = credentials.get("ca_certificate_base64");
            if (base64cert instanceof String) {
            	trustCertificate((String)base64cert);
            }
        }
        return new MongoServiceInfo(id, uri+"?ssl=true&sslInvalidHostNameAllowed=false");
    }
    
    private void trustCertificate(String base64cert) {
    	String certificate = new String(DatatypeConverter.parseBase64Binary(base64cert));
    	DefaultTrustStoreAppender trustStoreAppender = new DefaultTrustStoreAppender();
        final TrustStoreInfo trustStoreInfo = trustStoreAppender.append(CertificateFactory.newInstance(certificate));
        System.setProperty(SSL_TRUST_STORE_SYSTEM_PROPERTY, trustStoreInfo.getTrustStorefFile().getAbsolutePath());
        System.setProperty(SSL_TRUST_STORE_PASSWORD_SYSTEM_PROPERTY, trustStoreInfo.getPassword());
    }
}