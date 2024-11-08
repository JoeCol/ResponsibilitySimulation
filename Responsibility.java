import java.util.ArrayList;

public class Responsibility {
    public enum ResType {rt_repeat, rt_oneshot};
    private ResType type;
    private String name;
    private ArrayList<Responsibility> subRes;
    private Task task;
    private ArrayList<Responsibility> failRes;
    
    public Responsibility(String name, ArrayList<Responsibility> subRes, Task task, Responsibility.ResType type) {
        this.type = type;
        this.name = name;
        this.subRes = subRes;
        this.task = task;
        this.failRes = null;
    }

    public Responsibility(String name, ArrayList<Responsibility> subRes, Task task, Responsibility.ResType type, ArrayList<Responsibility> failRes) {
        this.type = type;
        this.name = name;
        this.subRes = subRes;
        this.task = task;
        this.failRes = failRes;
    }

    public ResType getType() {
        return type;
    }
    public void setType(ResType type) {
        this.type = type;
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
    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }

    public ArrayList<Responsibility> getFailRes() {
        return failRes;
    }

    public void setFailRes(ArrayList<Responsibility> failRes) {
        this.failRes = failRes;
    }


}
