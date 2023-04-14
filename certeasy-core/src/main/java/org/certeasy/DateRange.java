package org.certeasy;

import java.time.LocalDate;

public record DateRange(LocalDate start, LocalDate end) {

    public DateRange(LocalDate end){
        this(LocalDate.now(), end);
    }

    public DateRange(LocalDate start, LocalDate end){
        if(start==null||end==null)
            throw new IllegalArgumentException("neither start nor end date should be null");
        if(end.isBefore(start))
            throw new IllegalArgumentException("end date MUST be after start date");
        this.start = start;
        this.end = end;
    }

    public boolean isWithin(LocalDate date){
        if(date==null)
            throw new IllegalArgumentException("date MUST NOT be null");
        return ( date.isEqual(start) || date.equals(end)) ||  ( date.isAfter(start) && date.isBefore(end));
    }

}
