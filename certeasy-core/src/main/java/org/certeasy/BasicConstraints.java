package org.certeasy;

public record BasicConstraints(boolean ca, int pathLength) {

    public BasicConstraints(boolean ca){
        this(ca, -1);
    }

    public BasicConstraints(int pathLength){
        this(true, pathLength);
    }
    public BasicConstraints{
        if(pathLength<-1)
            throw new IllegalArgumentException("pathLength MUST not be less than -1");
    }

}
