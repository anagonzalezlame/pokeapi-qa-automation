package uy.anagonzalez.pokeapi.support;

/**
 * Central place for environment settings. Every value can be overridden
 * from the command line, e.g. {@code mvn test -Dheadless=false -Dbrowser=firefox}.
 */
public final class Config {

    public static final String API_BASE_URL = System.getProperty("api.baseUrl", "https://pokeapi.co/api/v2");
    public static final String WEB_BASE_URL = System.getProperty("web.baseUrl", "https://pokeapi.co");

    /** chromium, firefox or webkit */
    public static final String BROWSER = System.getProperty("browser", "chromium");
    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "true"));
    /** Milliseconds to wait between browser actions; useful to watch a run with -Dheadless=false. */
    public static final double SLOW_MO = Double.parseDouble(System.getProperty("slowMo", "0"));

    private Config() {
    }
}
