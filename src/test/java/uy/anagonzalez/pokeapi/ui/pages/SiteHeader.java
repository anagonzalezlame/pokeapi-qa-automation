package uy.anagonzalez.pokeapi.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Navigation bar shared by every page of the site (Home, About, API v2, GraphQL).
 */
public class SiteHeader {

    private final Locator header;

    public SiteHeader(Page page) {
        this.header = page.locator("#site-header");
    }

    public void goTo(String linkText) {
        header.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(linkText).setExact(true)).click();
    }
}
