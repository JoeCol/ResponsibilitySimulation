package Responsibility;

import Responsibility.ResponsibilityModel.Node;

public abstract class ResponsibilityType 
{
    public abstract double evaluation();
    public abstract String explanation();
    public abstract void resolution(Node nodet);
}
