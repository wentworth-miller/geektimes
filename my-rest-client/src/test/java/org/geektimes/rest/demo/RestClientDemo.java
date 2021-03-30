package org.geektimes.rest.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class RestClientDemo {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://127.0.0.1:8080/hello/world") // WebTarget
            .request() // Invocation.Builder
            .get(); // Response

        String content = response.readEntity(String.class);

        System.out.println(content);

    }

    private Client getClient() {
        return ClientBuilder.newClient();
    }

    @Test
    public void postTest() {
        Client client = getClient();
        // Entity<String> entity = Entity.json("{\"a\":\"123\"}");
        Entity<String> entity = Entity.entity("{\"a\":\"123\"}", MediaType.APPLICATION_JSON);
        Response response = client.target("http://www.baidu.com").request().post(entity);
        // Response response = client.target("http://127.0.0.1:8080/hello/world").request().post(entity);

        String content = response.readEntity(String.class);

        System.out.println(content);

    }
}
