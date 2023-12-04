package Responsibility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import CleaningEnvironment.CleaningWorld;
import Responsibility.ResponsibilityModel.Node;

public class TaskResponsibility extends ResponsibilityType
{
    public static class TaskState {
        public String stateToCheck;

        public TaskState(String stateToCheck) {
            this.stateToCheck = stateToCheck;
        }
    }

    public static class TaskAction {
        public String actionToDo;

        public TaskAction(String actionToDo) {
            this.actionToDo = actionToDo;
        }
    }

    private HashMap<TaskResponsibility.TaskAction,Boolean> actions = new HashMap<TaskResponsibility.TaskAction,Boolean>();
    private HashMap<TaskResponsibility.TaskState,Boolean> states = new HashMap<TaskResponsibility.TaskState,Boolean>();

    public TaskResponsibility(ArrayList<TaskResponsibility.TaskAction> _actions, ArrayList<TaskResponsibility.TaskState> _states) 
    {
        super("Task");
        for (TaskAction act : _actions)
        {
            actions.put(act, false);
        }
        for (TaskState st : _states)
        {
            states.put(st, false);
        }
    }

    public ArrayList<TaskResponsibility.TaskAction> getActions() {
        return new ArrayList<TaskAction>(actions.keySet());
    }

    public ArrayList<TaskResponsibility.TaskState> getStates() {
        return new ArrayList<TaskState>(states.keySet());
    }

    @Override
    public double evaluation() {
        int total = actions.size() + states.size();
        int done = Collections.frequency(actions.values(), true) + Collections.frequency(states.values(),true);
        return (double)done/total;
    }

    @Override
    public String explanation() {
        StringBuilder b = new StringBuilder();
        if (actions.size() > 0)
        {
            b.append("The current actions are:" + System.lineSeparator());
            for (Map.Entry<TaskAction,Boolean> act : actions.entrySet())
            {
                b.append("Action '" + act.getKey().actionToDo + "' is ");
                if (!act.getValue())
                {
                    b.append("not ");
                }
                b.append("completed" + System.lineSeparator());
            }
        }
        if (states.size() > 0)
        {
            b.append("The current states are:" + System.lineSeparator());
            for (Map.Entry<TaskState,Boolean> act : states.entrySet())
            {
                b.append("state '" + act.getKey().stateToCheck + "' is ");
                if (!act.getValue())
                {
                    b.append("not ");
                }
                b.append("valid" + System.lineSeparator());
            }
        }
        return b.toString();
    }

    @Override
    public void resolution(Node nodet) {
        
        // See if actions done
        for (TaskAction ta : getActions())
        {
            actions.put(ta, ((CleaningWorld)nodet.env).actionWasTaken(ta.actionToDo));
        }
        // Evaluate states
        for (TaskState ts : getStates())
        {
            states.put(ts, ((CleaningWorld)nodet.env).stateIsValid(ts.stateToCheck));
        }
        
    }

}
