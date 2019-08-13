package javacode.framework.jentity;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

//import org.apache.http.ssl.SSLContexts;


public class HttpsClientFactoryWithoutSSL {

    public HttpsClientFactoryWithoutSSL(String method) throws GeneralSecurityException {
        super();
        if (method.equals("new")) {
            this.client = getAcceptSelfSignedCertificateClient();
        } else {
            this.client = getHttpsClientWithoutX509();
        }
    }

    public CloseableHttpClient getHttpClient() {
        return client;
    }

    private CloseableHttpClient client;

    private CloseableHttpClient getAcceptSelfSignedCertificateClient()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        /*
        We configure a custom HttpClient. We begin by setting up an SSLContext using the
        SSLContextBuilder and use the TrustSelfSignedStrategy class to allow self signed certificates.
        Using the NoopHostnameVerifier essentially turns hostname verification off. Create
        the SSLConnectionSocketFactory and pass in the SSLContext and the HostNameVerifier
        and build the HttpClient using the factory methods.
        * */

        // use the TrustSelfSignedStrategy to allow Self Signed Certificates
        System.out.println("Method 01 ssl");
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();

        // we can optionally disable hostname verification.
        // if you don't want to further weaken the security, you don't have to include this.
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

        // create an SSL Socket Factory to use the SSLContext with the trust self signed certificate strategy
        // and allow all hosts verifier.
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

        // finally create the HttpClient using HttpClient factory methods and assign the ssl socket factory
        return HttpClients
                .custom()
                .setSSLSocketFactory(connectionFactory)
                .build();
    }


    public final CloseableHttpClient getHttpsClientWithoutX509()
            throws GeneralSecurityException {
        System.out.println("Method 02 ssl");

        if (client != null) {
            return client;
        }
        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();

        BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);
        client = HttpClients.custom().setSSLSocketFactory(sslsf)
                .setConnectionManager(connectionManager).build();

        return client;
//        HttpComponentsClientHttpRequestFactory requestFactory =
//                new HttpComponentsClientHttpRequestFactory(httpClient);

    }
}
