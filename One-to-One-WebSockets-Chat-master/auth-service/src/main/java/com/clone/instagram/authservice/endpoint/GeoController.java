package com.clone.instagram.authservice.endpoint;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class GeoController {

    private static final String endpointUrl = "https://nominatim.openstreetmap.org/";



    @GetMapping(value = "/city/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("name") String name) throws Exception {


        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        // Set max total connection
        connectionManager.setMaxTotal(20);
        // Set max route connection
        connectionManager.setDefaultMaxPerRoute(20);


      HttpClient httpClient =   HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();

        JsonNominatimClient  nominatimClient = new JsonNominatimClient(endpointUrl, httpClient,"aa@aa.com");




        final List<Address> addresses = nominatimClient.search("Santos,Brazil");


        final Address address = nominatimClient.getAddress(addresses.get(0).getLongitude(), addresses.get(0).getLatitude());

        return ResponseEntity.ok(addresses);
    }



}
