package drivers;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.Assert;
import responses.GetHotelBookingResponse;
import responses.PostHotelBookingResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpDrivers {

    public GetHotelBookingResponse get(final String url, final int expectedStatusCode, String expectedErrorMessage) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");

        HttpResponse response = client.execute(request);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), expectedStatusCode, "Unexpected status code\n");

        System.out.println("\nSending 'GET' request to URL : " + url);

        StringBuffer result = readResponse(response);

        if (response.getStatusLine().getStatusCode() == 200) {
            Gson gson = new Gson();
            GetHotelBookingResponse getHotelBookingResponse = gson.fromJson(result.toString(), GetHotelBookingResponse.class);
            return getHotelBookingResponse;
        }
        else {
            Assert.assertEquals(result.toString(), expectedErrorMessage, "Unexpected error message\n");
        }

        return null;
    }

    public PostHotelBookingResponse post(final String url, final String postBody) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/json; charset=UTF-8");

        final StringEntity urlParameters = new StringEntity(postBody);
        request.setEntity(urlParameters);

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'POST' request to URL : " + url);

        Gson gson = new Gson();
        StringBuffer result = readResponse(response);
        PostHotelBookingResponse postHotelBookingResponse = gson.fromJson(result.toString(), PostHotelBookingResponse.class);

        return postHotelBookingResponse;
    }

    public HttpResponse delete(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(url);

        request.addHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=");

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'DELETE' request to URL : " + url);

        return response;
    }

    private StringBuffer readResponse(HttpResponse response) throws IOException {

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result;
    }
}
