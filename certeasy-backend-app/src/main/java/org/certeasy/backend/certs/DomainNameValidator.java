package org.certeasy.backend.certs;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class DomainNameValidator {

    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[!@#$%^&*()_+\\[\\]{};':\"\\\\|,.<>/?]+");
    private static final Pattern CAPITAL_CASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT_START_PATTERN = Pattern.compile("^\\d.*");

    public static boolean isValidDomain(String domain) {

        if (StringUtils.isBlank(domain)) {
            return false;
        }

        // add empty space to tackle split issue on domains ending with dot
        if (StringUtils.endsWith(domain, ".")){
            domain = domain + StringUtils.SPACE;
        }

        String[] segments = domain.split("\\.");
        if ( segments.length > 127) {
            return false;
        }

        for (String segment : segments) {
            if (StringUtils.isBlank(segment) || segment.length() > 63 || segment.length() < 2) {
                return false;
            }

            if(SPECIAL_CHARS_PATTERN.matcher(segment).find() || CAPITAL_CASE_PATTERN.matcher(segment).find()){
                return false;
            }

            if (segment.startsWith("-") || segment.endsWith("-")) {
                return false;
            }

            if(DIGIT_START_PATTERN.matcher(segment).matches()){
                return false;
            }
        }

        return true;
    }

}
