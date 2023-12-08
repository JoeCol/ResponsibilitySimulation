package Responsibility;
import java.util.ArrayList;

import Responsibility.ResponsibilityModel.Node;

public class Responsibility {
    
    public enum Concludes {rt_repeat, rt_oneshot};
    public enum ResEvaluation {re_nonstarted, re_started, re_failed, re_success};

    private Concludes conclude;
    private String name;
    private ArrayList<Responsibility> subRes;
    private ResponsibilityType restype;
    private ArrayList<Responsibility> failRes;
    
    public Responsibility(String name, ArrayList<Responsibility> subRes, ResponsibilityType restype, Responsibility.Concludes conclude) {
        this.conclude = conclude;
        this.name = name;
        this.subRes = subRes;
        this.restype = restype;
        this.failRes = null;
    }

    public Responsibility(String name, ArrayList<Responsibility> subRes, ResponsibilityType restype, Responsibility.Concludes conclude, ArrayList<Responsibility> failRes) {
        this.conclude = conclude;
        this.name = name;
        this.subRes = subRes;
        this.restype = restype;
        this.failRes = failRes;
    }

    public ResponsibilityType getResponsibilityType()
    {
        return restype;
    }

    public Concludes getConcludes() {
        return conclude;
    }
    public void setType(Concludes conclude) {
        this.conclude = conclude;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Responsibility> getSubRes() {
        return subRes;
    }
    public void setSubRes(ArrayList<Responsibility> subRes) {
        this.subRes = subRes;
    }

    public ArrayList<Responsibility> getFailRes() {
        return failRes;
    }

    public void setFailRes(ArrayList<Responsibility> failRes) {
        this.failRes = failRes;
    }

    public void doResolution(Node t)
    {
        restype.resolution(t);
    }

    public boolean failed() 
    {
        return false;//Need to implement this
    }

    public boolean fulfilled() 
    {
        if (restype.evaluation() >= 1.0)
        {
            for(Responsibility r : subRes)
            {
                if (!r.fulfilled())
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


}
