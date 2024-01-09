package CleaningEnvironment;
import Responsibility.DefaultResponsibility;
import Responsibility.Responsibility;
import Responsibility.TaskResponsibility;
import Responsibility.Responsibility.Concludes;
import Responsibility.TaskResponsibility.TaskAction;
import Responsibility.TaskResponsibility.TaskState;

import java.util.ArrayList;
import java.util.Iterator;

import Helper.Pair;

public class CleaningStartingResponsibilities 
{
    public static ArrayList<Responsibility> getAll(CleaningWorld env)
    {
        ArrayList<Responsibility> toRet = new ArrayList<Responsibility>();

        ArrayList<Responsibility> safetySubRes = new ArrayList<Responsibility>();
        ArrayList<Responsibility> cleanlinessSubRes = new ArrayList<Responsibility>();

        ArrayList<Responsibility> cleanSubRes = new ArrayList<Responsibility>();
        for (Pair<Integer, Integer> x : env.possibleDirtLocations)
        {
            //Clean Safety Critial Dirt all locations
            ArrayList<TaskAction> clean = new ArrayList<TaskAction>();
            clean.add(new TaskAction("clean(" + x.getFirst() + "," + x.getSecond() + ")"));
            TaskResponsibility cleantile = new TaskResponsibility(clean, new ArrayList<TaskState>());

            Responsibility sr = new Responsibility("cleanSCDirt(" + x.getFirst() + "," + x.getSecond() + ")", new ArrayList<Responsibility>(), cleantile, Concludes.rt_repeat);
            safetySubRes.add(sr);

            Responsibility cr = new Responsibility("clean(" + x.getFirst() + "," + x.getSecond() + ")", new ArrayList<Responsibility>(), cleantile, Concludes.rt_repeat);
            cleanSubRes.add(cr);
        }
        cleanlinessSubRes.add(new Responsibility("clean", cleanSubRes, new DefaultResponsibility(), Concludes.rt_repeat));

        Iterator<Character> a = env.getZones().iterator();
        ArrayList<Responsibility> observeSubRes = new ArrayList<Responsibility>();
        while (a.hasNext())
        {
            ArrayList<TaskAction> observeActions = new ArrayList<TaskAction>();
            Character zone = a.next();
            observeActions.add(new TaskAction("observe(" + zone + ")"));
            Responsibility observeRoomRes = new Responsibility("observe(" + zone + ")", new ArrayList<Responsibility>(), new TaskResponsibility(observeActions, new ArrayList<TaskState>()), Concludes.rt_repeat);   
            observeSubRes.add(observeRoomRes);
        }
        cleanlinessSubRes.add(new Responsibility("observe",  observeSubRes, new DefaultResponsibility(), Concludes.rt_repeat));

        ArrayList<Responsibility> janSubRes = new ArrayList<Responsibility>();
        ArrayList<TaskState> safTask = new ArrayList<TaskState>();
        safTask.add(new TaskState("cdc<5"));

        //Fail Responsibility
        ArrayList<Responsibility> safFailRes = new ArrayList<Responsibility>();
        ArrayList<TaskAction> sendToHumanAction = new ArrayList<TaskAction>();
        sendToHumanAction.add(new TaskAction("sendReport"));
        TaskResponsibility sendToHuman = new TaskResponsibility(sendToHumanAction, new ArrayList<TaskState>());
        safFailRes.add(new Responsibility("reportHuman", new ArrayList<Responsibility>(), sendToHuman, Concludes.rt_repeat));

        //Main Responsibilities
        janSubRes.add(new Responsibility("safety", safetySubRes, new TaskResponsibility(new ArrayList<TaskAction>(),safTask), Concludes.rt_repeat, safFailRes));
        janSubRes.add(new Responsibility("cleanliness", cleanlinessSubRes, new DefaultResponsibility(), Concludes.rt_repeat));
        toRet.add(new Responsibility("janitorial", janSubRes, new DefaultResponsibility(), Concludes.rt_repeat));

        ArrayList<TaskAction> statusActions = new ArrayList<TaskAction>();
        statusActions.add(new TaskAction("sendStatus"));
        TaskResponsibility statusTask = new TaskResponsibility(statusActions, new ArrayList<TaskState>());
        toRet.add(new Responsibility("report", new ArrayList<Responsibility>(), statusTask, Concludes.rt_repeat));
        return toRet;
    }
}
