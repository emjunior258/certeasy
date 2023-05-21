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
        if (from == null)
            violations.add(new Violation(path, "from", ViolationType.REQUIRED, "from date is required"));
        if (until == null)
            violations.add(new Violation(path, "until", ViolationType.REQUIRED, "until date is required"));
        LocalDate fromLocalDate = null;
        if (from != null && !from.isEmpty()){
            try {
                fromLocalDate = LocalDate.parse(from, DATE_FORMATTER);
            } catch (DateTimeParseException ex) {
                violations.add(new Violation(path, "from", ViolationType.FORMAT, "from date is not ISO date"));
            }
        }
        LocalDate untilLocalDate = null;
        if (until != null && !until.isEmpty()){
            try {
                untilLocalDate = LocalDate.parse(until, DATE_FORMATTER);
            } catch (DateTimeParseException ex) {
                violations.add(new Violation(path, "until", ViolationType.FORMAT, "until date is not ISO date"));
            }
        }
        if (fromLocalDate != null && untilLocalDate != null){
            if (fromLocalDate.isAfter(untilLocalDate))
                violations.add(new Violation(path, "from", ViolationType.PRECEDENCE, "from date cannot be after until date"));
        }
        return violations;
    }
}
