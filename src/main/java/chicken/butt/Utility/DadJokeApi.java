package chicken.butt.Utility;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DadJokeApi {
    public static String getDadJoke() throws URISyntaxException, ClientProtocolException, IOException {
        HttpClient httpClient = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build())
            .build();

        URIBuilder uriBuilder = new URIBuilder("https://icanhazdadjoke.com/");

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Accept:", "text/plain");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        String dadJoke = EntityUtils.toString(entity, "UTF-8");

        return dadJoke;
    }
}
