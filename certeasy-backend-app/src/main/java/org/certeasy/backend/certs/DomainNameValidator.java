package org.certeasy.backend.certs;

import java.util.regex.Pattern;

public class DomainNameValidator {

    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[!@#$%^&*()_+\\[\\]{};':\"\\\\|,.<>/?]+");
    private static final Pattern CAPITAL_CASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT_START_PATTERN = Pattern.compile("^[0-9].*");

    public static boolean isValidDomain(String domain) {

        if (domain == null || domain.isEmpty()) {
            return false;
        }

        String[] segments = domain.split("\\.");
        if (segments.length < 1 || segments.length > 127) {
            return false;
        }

        for (String segment : segments) {
            if (segment.isEmpty() || segment.length() > 63 || segment.contains(" ") ) {
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

            for (char c : segment.toCharArray()) {
                if (!(Character.isLetterOrDigit(c) || c == '-')) {
                    return false;
                }
            }
        }

        return true;
    }

}
