package Responsibility;

import Responsibility.ResponsibilityModel.Node;

public class DefaultResponsibility extends ResponsibilityType
{

    public DefaultResponsibility() {
        super("Default");
        //TODO Auto-generated constructor stub
    }

    @Override
    public double evaluation() {
        return 1.0;//Always assumed to be fulfilled
    }

    @Override
    public String explanation() {
        return "This responsibilitiy has no specifics";
    }

    @Override
    public void resolution(Node nodet) {
        //Nothing to resolve
    }
    
}
