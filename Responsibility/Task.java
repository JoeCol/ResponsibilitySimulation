package Responsibility;
import java.util.ArrayList;

public class Task 
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

    private ArrayList<Task.TaskAction> actions = new ArrayList<Task.TaskAction>();
    private ArrayList<Task.TaskState> states = new ArrayList<Task.TaskState>();

    public Task(ArrayList<Task.TaskAction> actions, ArrayList<Task.TaskState> states) {
        this.actions = actions;
        this.states = states;
    }

    public Task()
    {
        
    }

    public ArrayList<Task.TaskAction> getActions() {
        return actions;
    }

    public ArrayList<Task.TaskState> getStates() {
        return states;
    }

}
