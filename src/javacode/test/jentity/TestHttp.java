package javacode.test.jentity;


import javacode.framework.jentity.HttpMethod;
import javacode.framework.jentity.HttpsClientFactoryWithoutSSL;
import javacode.framework.jentity.SSLClient;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;

public class TestHttp {


    public static void main(String[] args) throws GeneralSecurityException, IOException {

//        CloseableHttpClient hc = (CloseableHttpClient) HttpClientFactoryWithKeystore.getHttpsClientWithoutAuth();
        HttpsClientFactoryWithoutSSL httpsClients = new HttpsClientFactoryWithoutSSL("new");
        CloseableHttpClient hc1 = httpsClients.getHttpClient();

        CloseableHttpClient hc = new SSLClient();
        HashMap<String, String> map = new HashMap<>();
//        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
//        map.put("x-ms-principal-id", "user02");

        String result = HttpMethod.httpsGet(hc1, "https://106.14.146.79/", map,
                "106.14.146.79", 443);
        System.out.println(result);


    }
}
