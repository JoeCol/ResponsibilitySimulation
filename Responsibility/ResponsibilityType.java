package Responsibility;

import Responsibility.ResponsibilityModel.Node;

public abstract class ResponsibilityType 
{
    public String typeName;
    public String getTypeName() {
        return typeName;
    }
    public ResponsibilityType(String typeName) {
        this.typeName = typeName;
    }
    public abstract double evaluation();
    public abstract String explanation();
    public abstract void resolution(Node nodet);

    
}
