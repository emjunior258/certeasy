package org.certeasy;

public record BasicConstraints(boolean ca, int pathLength) {

    public BasicConstraints(boolean ca){
        this(ca, -1);
    }
    public BasicConstraints(boolean ca, int pathLength){
        this.ca = ca;
        if(pathLength<-1)
            throw new IllegalArgumentException("pathLength MUST not be less than -1");
        this.pathLength = pathLength;
    }

}
