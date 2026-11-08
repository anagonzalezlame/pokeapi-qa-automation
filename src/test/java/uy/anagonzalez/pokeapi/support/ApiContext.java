package uy.anagonzalez.pokeapi.support;

import io.restassured.response.Response;

/**
 * Holds the last API response of a scenario so that When and Then steps can share it.
 * PicoContainer creates a fresh instance for every scenario.
 */
public class ApiContext {

    private Response response;

    public Response getResponse() {
        if (response == null) {
            throw new IllegalStateException("No request was sent yet in this scenario");
        }
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
