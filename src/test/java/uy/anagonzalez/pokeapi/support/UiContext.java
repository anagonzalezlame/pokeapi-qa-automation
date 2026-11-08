package uy.anagonzalez.pokeapi.support;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

/**
 * Holds the browser context and page of the current UI scenario.
 * Each scenario gets its own isolated context (cookies, storage, cache).
 */
public class UiContext {

    private BrowserContext browserContext;
    private Page page;

    public BrowserContext getBrowserContext() {
        return browserContext;
    }

    public void setBrowserContext(BrowserContext browserContext) {
        this.browserContext = browserContext;
    }

    public Page getPage() {
        if (page == null) {
            throw new IllegalStateException("The browser was not started. Is the scenario tagged with @ui?");
        }
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public boolean hasPage() {
        return page != null;
    }
}
