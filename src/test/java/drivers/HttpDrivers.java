package drivers;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class HttpDrivers {

    public HttpResponse get(final String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        request.addHeader("Accept", "application/json");

        System.out.println("\nSending 'GET' request to URL : " + url);

        return client.execute(request);
    }

    public HttpResponse post(final String url, final String postBody) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/json; charset=UTF-8");

        final StringEntity urlParameters = new StringEntity(postBody);
        request.setEntity(urlParameters);

        System.out.println("\nSending 'POST' request to URL : " + url);

        return client.execute(request);
    }

    public HttpResponse delete(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(url);

        request.addHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=");

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'DELETE' request to URL : " + url);

        return response;
    }
}
