package uy.anagonzalez.pokeapi.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Page Object for https://pokeapi.co/ and its "Try it now!" API explorer.
 * Locators rely on roles, visible text and ids instead of generated CSS classes.
 */
public class HomePage {

    private final Page page;
    private final Locator resourceInput;
    private final Locator submitButton;
    private final Locator directLink;

    public HomePage(Page page) {
        this.page = page;
        this.resourceInput = page.locator("#url-input");
        this.submitButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit").setExact(true));
        this.directLink = page.locator("p", new Page.LocatorOptions().setHasText("Direct link to results:"))
                .getByRole(AriaRole.LINK);
    }

    /**
     * Opens the home page and waits for the explorer's first request (pokemon/ditto)
     * to finish. The pre-rendered HTML already says "Resource for ditto", so waiting
     * for the text alone is not enough: if a test searched while that first request
     * was still in flight, its late answer could overwrite the new result.
     */
    public void open() {
        page.waitForResponse(
                response -> response.url().endsWith("/api/v2/pokemon/ditto"),
                () -> page.navigate("/"));
        assertThat(resultMessage("Resource for ditto")).isVisible();
    }

    public void searchResource(String resource) {
        resourceInput.fill(resource);
        submitButton.click();
    }

    public void clickHint(String hint) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(hint).setExact(true)).click();
    }

    public Locator heading(String text) {
        return page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(text).setExact(true));
    }

    public Locator resultMessage(String message) {
        return heading(message);
    }

    public Locator directLink() {
        return directLink;
    }

    public void clickLink(String text) {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text).setExact(true)).click();
    }
}
