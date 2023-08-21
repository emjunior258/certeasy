package org.certeasy.backend.common.cert;

import org.certeasy.DateRange;
import org.certeasy.backend.common.validation.Validable;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

public record CertValidity(String from, String until) implements Validable {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private static final String ISO_DATE_MESSAGE = " date is not in ISO date format";
    private static final String UNTIL_FIELD = "until";
    private static final String FROM_FIELD = "from";


    public CertValidity(String from, String until) {
        this.from = from;
        this.until = until;
    }

    public CertValidity(DateRange dateRange){
        this(DATE_FORMATTER.format(dateRange.start()), DATE_FORMATTER.format(dateRange.end()));
    }

    public DateRange toDateRange(){
        return new DateRange(LocalDate.parse(from, DATE_FORMATTER), LocalDate.parse(until, DATE_FORMATTER));
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>();

        Set<Violation> violationsUntil = new HashSet<>(checkFieldViolations(path, until, UNTIL_FIELD));
        Set<Violation> violationsFrom = new HashSet<>(checkFieldViolations(path, from, FROM_FIELD));

        LocalDate fromLocalDate = parseLocalDate(path, from, FROM_FIELD, violationsFrom);
        LocalDate untilLocalDate = parseLocalDate(path, until, UNTIL_FIELD, violationsUntil);

        if ((fromLocalDate != null && untilLocalDate != null) && fromLocalDate.isAfter(untilLocalDate))
            violations.add(new Violation(path, "from", ViolationType.PRECEDENCE, "from date cannot be after until date"));

        violations.addAll(violationsFrom);
        violations.addAll(violationsUntil);
        return violations;
    }

    private Set<Violation> checkFieldViolations(ValidationPath path, String dateField, String dateFieldName){
        Set<Violation> violations = new HashSet<>();
        if (dateField == null) {
            violations.add(new Violation(path, dateFieldName, ViolationType.REQUIRED, dateFieldName + " date is required"));
        }
        else if (dateField.trim().isEmpty()) {
            violations.add(new Violation(path, dateFieldName, ViolationType.FORMAT, dateFieldName + ISO_DATE_MESSAGE));
        }
        return violations;
    }

    private LocalDate parseLocalDate(ValidationPath path, String dateField, String dateFieldName, Set<Violation> violations) {
        LocalDate localDate = null;
        if (violations.isEmpty()) {
            try {
                localDate = LocalDate.parse(dateField.trim(), DATE_FORMATTER);
            } catch (DateTimeParseException ex) {
                violations.add(new Violation(path, dateFieldName, ViolationType.FORMAT, dateFieldName + ISO_DATE_MESSAGE));
            }

        }
        return localDate;
    }
}
