package org.certeasy.backend;

import java.util.Optional;
import java.util.function.Consumer;

public class Result<OkType, ErrType> {

    private final OkType okValue;
    private final ErrType errValue;

    private Result(OkType ok, ErrType err){
        this.okValue = ok;
        this.errValue = err;
    }

    public Optional<OkType> getOkValue(){
        return Optional.ofNullable(okValue);
    }

    public Optional<ErrType> getError(){
        return Optional.ofNullable(errValue);
    }

    public void ok(Consumer<OkType> consumer){
        if(consumer == null)
            throw new IllegalArgumentException("consumer MUST not be null");
        if(okValue != null)
            consumer.accept(okValue);
    }

    public void error(Consumer<ErrType> consumer){
        if(consumer == null)
            throw new IllegalArgumentException("consumer MUST not be null");
        if(errValue != null)
            consumer.accept(errValue);

    }

    public boolean isError(){
        return errValue != null;
    }

    public static <O,E>  Result<O,E> errorResult(E error){
        return new Result<>(null, error);
    }

    public static <O,E>  Result<O,E> okResult(O ok){
        return new Result<>(ok, null);
    }

}
