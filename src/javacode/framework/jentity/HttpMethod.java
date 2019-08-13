package javacode.framework.jentity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpMethod {
    public static void main(String[] args) {


    }

    public static String httpsGet(CloseableHttpClient httpClient, String url, HashMap<String,
            String> hashMap, String hostname, int port) throws ClientProtocolException, IOException {


        String result = null;
        HttpGet httpGet = new HttpGet(url);

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }


        HttpClientContext context = HttpClientContext.create();
        HttpHost target = new HttpHost(hostname, port, "https");
        CloseableHttpResponse response = httpClient.execute(target, httpGet, context);


        try {
            HttpEntity entity = response.getEntity();

            InputStream inSm = entity.getContent();
//            System.out.println(inSm);
//            FileInputStream fis = (FileInputStream)entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(inSm,
                    "UTF-8"));
            String data = "";
            StringBuffer sb = new StringBuffer();
            while ((data = br.readLine()) != null) {
                sb.append(data);
                sb.append("\r\n");
            }
            result = sb.toString();

        }
        catch (IOException e){
            throw new IOException("InputStreame Error");
        }
        finally {

            response.close();
        }

        return result;
    }

    public static HttpEntity httpsGetNTLM(CloseableHttpClient httpClient, String url, HashMap<String,
            String> hashMap, String hostname, int port, String userName, String password)
            throws ClientProtocolException, IOException {

        HttpEntity entity = null;
        HttpGet httpGet = new HttpGet(url);

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY,
                new NTCredentials(userName, password, "", ""));

// Make sure the same context is used to execute logically related requests
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);

        HttpHost target = new HttpHost(hostname, port, "https");
        CloseableHttpResponse response = httpClient.execute(target, httpGet, context);

        try {
            entity = response.getEntity();
        } finally {
            response.close();
        }

        return entity;
    }


    public static HttpEntity httpGet(CloseableHttpClient httpClient, String url, HashMap<String,
            String> hashMap) throws ClientProtocolException, IOException {

        HttpEntity entity = null;
        HttpGet httpGet = new HttpGet(url);

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            entity = response.getEntity();
        } finally {
            response.close();
        }

        return entity;

    }
}
