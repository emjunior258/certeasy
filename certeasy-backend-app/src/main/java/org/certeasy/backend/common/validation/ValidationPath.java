package org.certeasy.backend.common.validation;

public class ValidationPath {

    private String path;

    private ValidationPath(String path){
        this.path = path;
    }

    public ValidationPath append(String path){
        return new ValidationPath(this.path + "." + path);
    }

    public static ValidationPath of(String path){
        return new ValidationPath(path);
    }


    @Override
    public String toString() {
        return path;
    }
}
