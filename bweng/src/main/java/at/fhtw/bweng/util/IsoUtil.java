package at.fhtw.bweng.util;

import java.util.Locale;
import java.util.Set;

// Class for validating the country code
public final class IsoUtil {
    private static final Set<String> ISO_LANGUAGES = Set.of(Locale.getISOLanguages());
    private static final Set<String> ISO_COUNTRIES = Set.of(Locale.getISOCountries());

    private IsoUtil() {}

    public static boolean isValidISOLanguage(String s) {
        return ISO_LANGUAGES.contains(s);
    }

    public static boolean isValidISOCountry(String s) {
        return ISO_COUNTRIES.contains(s);
    }
}