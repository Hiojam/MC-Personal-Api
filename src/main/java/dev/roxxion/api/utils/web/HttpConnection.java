package dev.roxxion.api.utils.web;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpConnection {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final String uri;

    public HttpConnection(String uri){
        this.uri = uri;
    }

    public String sendPost(HashMap<String, String> values) {
        HttpPost post = new HttpPost(this.uri);

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        if(values == null) return null; //DO NOT SEND THE POST EMPTY

        for(String val : values.keySet()) {
            urlParameters.add(new BasicNameValuePair(val, values.get(val)));
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            return EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendGet(HashMap<String, String> values) {

        HttpGet request = new HttpGet(this.uri);

        // add request headers
        if(values == null) return null;

        for(String value : values.keySet()){
            request.addHeader(value, values.get(value));
        }

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                return EntityUtils.toString(entity);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<Boolean> downloadFromUrlToDir(String urlString, String directory){
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(urlString);
                ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                fileOutputStream.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}
