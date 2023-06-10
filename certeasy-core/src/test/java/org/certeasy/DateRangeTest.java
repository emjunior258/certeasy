package org.certeasy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class DateRangeTest {

    @Test
    @DisplayName("constructor must not allow null start date")
    public void constructorMustNotAllowNullStartDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            new DateRange(null, LocalDate.now());
        });
    }


    @Test
    @DisplayName("constructor must not allow null end date")
    public void constructorMustNotAllowNullEndDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            new DateRange(LocalDate.now(), null);
        });
    }

    @Test
    @DisplayName("constructor must not allow end date earlier than start date")
    public void constructorMustNotAllowEndDateEarlierThanStartDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            new DateRange(LocalDate.of(2023, Month.AUGUST, 10),
                    LocalDate.of(2023, Month.JULY, 10));
        });
    }

    @Test
    @DisplayName("constructor must assume start date is today when not provided")
    public void constructorMustAssumeStartDateIsTodayWhenNotProvided() {
        LocalDate today = LocalDate.now();
        DateRange range = new DateRange(LocalDate.of(2023, Month.SEPTEMBER, 28));
        LocalDate start = range.start();
        assertEquals(today, start);
    }

    @Test
    @DisplayName("isWithin() must return false if date is outside range")
    public void isWithinMustReturnFalseIfOutOfRange() {

        DateRange range = new DateRange(LocalDate.of(2023, Month.JANUARY, 1),
                LocalDate.of(2023, Month.SEPTEMBER, 28));
        range.isWithin(LocalDate.of(2023, Month.DECEMBER, 10));

    }

    @Test
    @DisplayName("isWithin() must return false if date is within range")
    public void isWithinMustReturnTrueIfWithinRange() {

        DateRange range = new DateRange(LocalDate.of(2023, Month.JANUARY, 1),
                LocalDate.of(2023, Month.SEPTEMBER, 28));
        range.isWithin(LocalDate.of(2023, Month.MARCH, 10));

    }

    @Test
    @DisplayName("isWithin() must fail if date is null")
    public void isWithinMustFailIfDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateRange range = new DateRange(LocalDate.of(2023, Month.JANUARY, 1),
                    LocalDate.of(2023, Month.SEPTEMBER, 28));
            range.isWithin(null);
        });
    }

}
