package uy.anagonzalez.pokeapi.ui;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uy.anagonzalez.pokeapi.support.UiContext;
import uy.anagonzalez.pokeapi.ui.pages.HomePage;
import uy.anagonzalez.pokeapi.ui.pages.SiteHeader;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UiSteps {

    private final UiContext ui;

    public UiSteps(UiContext ui) {
        this.ui = ui;
    }

    private Page page() {
        return ui.getPage();
    }

    private HomePage homePage() {
        return new HomePage(page());
    }

    // ---------- Actions ----------

    @Given("I open the PokeAPI home page")
    public void openHomePage() {
        homePage().open();
    }

    @When("I search the resource {string}")
    public void searchResource(String resource) {
        homePage().searchResource(resource);
    }

    @When("I click the hint {string}")
    public void clickHint(String hint) {
        homePage().clickHint(hint);
    }

    @When("I click {string} in the header")
    public void clickHeaderLink(String linkText) {
        new SiteHeader(page()).goTo(linkText);
    }

    @When("I click the {string} link")
    public void clickLink(String linkText) {
        homePage().clickLink(linkText);
    }

    // ---------- Checks ----------

    @Then("the page title should be {string}")
    public void pageTitleShouldBe(String title) {
        assertThat(page()).hasTitle(title);
    }

    @Then("I should see the {string} heading")
    public void shouldSeeHeading(String text) {
        assertThat(homePage().heading(text)).isVisible();
    }

    @Then("I should see the result message {string}")
    public void shouldSeeResultMessage(String message) {
        assertThat(homePage().resultMessage(message)).isVisible();
    }

    @Then("the direct link should point to {string}")
    public void directLinkShouldPointTo(String url) {
        assertThat(homePage().directLink()).hasAttribute("href", url);
    }

    @Then("the URL should contain {string}")
    public void urlShouldContain(String fragment) {
        // Playwright evaluates the regex in the browser (JavaScript), which does not support
        // Java's Pattern.quote() syntax (\Q...\E), so special characters are escaped one by one.
        String escaped = fragment.replaceAll("[^A-Za-z0-9]", "\\\\$0");
        assertThat(page()).hasURL(Pattern.compile(".*" + escaped + ".*"));
    }
}
