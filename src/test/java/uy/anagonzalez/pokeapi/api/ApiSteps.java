package uy.anagonzalez.pokeapi.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import uy.anagonzalez.pokeapi.support.ApiContext;
import uy.anagonzalez.pokeapi.support.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class ApiSteps {

    private final ApiContext api;

    public ApiSteps(ApiContext api) {
        this.api = api;
    }

    private RequestSpecification request() {
        return RestAssured.given()
                .baseUri(Config.API_BASE_URL)
                .accept(ContentType.JSON)
                .log().ifValidationFails();
    }

    // ---------- Requests ----------

    @When("I request the pokemon {string}")
    public void requestPokemon(String nameOrId) {
        api.setResponse(request().get("/pokemon/{nameOrId}", nameOrId));
    }

    @When("I request the pokemon list with limit {int} and offset {int}")
    public void requestPokemonList(int limit, int offset) {
        api.setResponse(request()
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .get("/pokemon"));
    }

    @When("I request the type {string}")
    public void requestType(String type) {
        api.setResponse(request().get("/type/{type}", type));
    }

    @Given("I request the species {string}")
    public void requestSpecies(String species) {
        api.setResponse(request().get("/pokemon-species/{species}", species));
    }

    @When("I follow the species evolution chain")
    public void followEvolutionChain() {
        String chainUrl = api.getResponse().jsonPath().getString("evolution_chain.url");
        assertThat("The species response should link to an evolution chain", chainUrl, notNullValue());
        // The URL is absolute, so REST Assured ignores the base URI here
        api.setResponse(request().get(chainUrl));
    }

    // ---------- Generic checks ----------

    @Then("the response status code should be {int}")
    public void statusCodeShouldBe(int expected) {
        api.getResponse().then().statusCode(expected);
    }

    @Then("the response should be in JSON format")
    public void responseShouldBeJson() {
        api.getResponse().then().contentType(ContentType.JSON);
    }

    @Then("the response time should be under {long} milliseconds")
    public void responseTimeShouldBeUnder(long maxMillis) {
        api.getResponse().then().time(lessThan(maxMillis));
    }

    @Then("the response field {string} should be {string}")
    public void fieldShouldBeText(String field, String expected) {
        api.getResponse().then().body(field, equalTo(expected));
    }

    @Then("the response field {string} should be the number {int}")
    public void fieldShouldBeNumber(String field, int expected) {
        api.getResponse().then().body(field, equalTo(expected));
    }

    @Then("the response body should match the {string} schema")
    public void bodyShouldMatchSchema(String schemaName) {
        api.getResponse().then().body(matchesJsonSchemaInClasspath("schemas/" + schemaName + ".json"));
    }

    // ---------- Pokemon ----------

    @Then("the pokemon should have the type {string}")
    public void pokemonShouldHaveType(String type) {
        api.getResponse().then().body("types.type.name", hasItem(type));
    }

    @Then("the pokemon should have exactly these abilities:")
    public void pokemonShouldHaveAbilities(List<String> expected) {
        List<String> actual = api.getResponse().jsonPath().getList("abilities.ability.name", String.class);
        assertThat(actual, containsInAnyOrder(expected.toArray(new String[0])));
    }

    // ---------- Pagination ----------

    @Then("the list should contain {int} results")
    public void listShouldContain(int size) {
        api.getResponse().then().body("results", hasSize(size));
    }

    @Then("the total count should be greater than {int}")
    public void totalCountShouldBeGreaterThan(int minimum) {
        api.getResponse().then().body("count", greaterThan(minimum));
    }

    @Then("the first result should be {string}")
    public void firstResultShouldBe(String name) {
        api.getResponse().then().body("results[0].name", equalTo(name));
    }

    @Then("the {string} page link should be present")
    public void pageLinkShouldBePresent(String link) {
        api.getResponse().then().body(link, notNullValue());
    }

    @Then("the {string} page link should be empty")
    public void pageLinkShouldBeEmpty(String link) {
        api.getResponse().then().body(link, nullValue());
    }

    // ---------- Types ----------

    @Then("the type should deal double damage to:")
    public void typeShouldDealDoubleDamageTo(List<String> types) {
        api.getResponse().then()
                .body("damage_relations.double_damage_to.name", hasItems(types.toArray(new String[0])));
    }

    @Then("the type should take double damage from:")
    public void typeShouldTakeDoubleDamageFrom(List<String> types) {
        api.getResponse().then()
                .body("damage_relations.double_damage_from.name", hasItems(types.toArray(new String[0])));
    }

    // ---------- Evolution ----------

    /**
     * Walks the evolution chain following the first branch at each stage
     * and compares it with a readable text such as "charmander > charmeleon > charizard".
     */
    @Then("the evolution chain should be {string}")
    @SuppressWarnings("unchecked")
    public void evolutionChainShouldBe(String expected) {
        List<String> stages = new ArrayList<>();
        Map<String, Object> node = api.getResponse().jsonPath().getMap("chain");
        while (node != null) {
            Map<String, Object> species = (Map<String, Object>) node.get("species");
            stages.add((String) species.get("name"));
            List<Map<String, Object>> next = (List<Map<String, Object>>) node.get("evolves_to");
            node = (next == null || next.isEmpty()) ? null : next.get(0);
        }
        assertThat(String.join(" > ", stages), equalTo(expected));
    }
}
