package uy.anagonzalez.pokeapi.support;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Browser lifecycle for scenarios tagged with @ui.
 * The browser is launched once (lazily, so API-only runs never start it)
 * and every scenario gets a new, isolated context.
 */
public class UiHooks {

    private static Playwright playwright;
    private static Browser browser;

    private final UiContext ui;

    public UiHooks(UiContext ui) {
        this.ui = ui;
    }

    @Before("@ui")
    public void openBrowser() {
        if (browser == null) {
            PlaywrightAssertions.setDefaultAssertionTimeout(10_000);
            playwright = Playwright.create();
            browser = browserType(playwright).launch(new BrowserType.LaunchOptions()
                    .setHeadless(Config.HEADLESS)
                    .setSlowMo(Config.SLOW_MO));
        }
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setBaseURL(Config.WEB_BASE_URL)
                .setViewportSize(1366, 768));
        ui.setBrowserContext(context);
        ui.setPage(context.newPage());
    }

    @After("@ui")
    public void closeContext(Scenario scenario) {
        if (scenario.isFailed() && ui.hasPage()) {
            byte[] screenshot = ui.getPage().screenshot(new Page.ScreenshotOptions().setFullPage(true));
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        if (ui.getBrowserContext() != null) {
            ui.getBrowserContext().close();
        }
    }

    @AfterAll
    public static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    private static BrowserType browserType(Playwright playwright) {
        switch (Config.BROWSER.toLowerCase()) {
            case "firefox":
                return playwright.firefox();
            case "webkit":
                return playwright.webkit();
            default:
                return playwright.chromium();
        }
    }
}
