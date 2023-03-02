import java.util.ArrayList;

public class SetupResponsibilities 
{
    public static ArrayList<Responsibility> resToDelegate = new ArrayList<Responsibility>();
    public static Responsibility setupManagerResponsiblities(int numOfRooms)
    {
        ArrayList<Responsibility> cleanSubRes = new ArrayList<Responsibility>();
        ArrayList<Responsibility> observeSubRes = new ArrayList<Responsibility>();
        ArrayList<Responsibility> safetySubRes = new ArrayList<Responsibility>();
        for (int i = 0; i < numOfRooms; i++)
        {
            char room = (char)('A' + i);
            Task.TaskAction taCleanRoom = new Task.TaskAction("cleanroom" + room);
            Task cleanroom = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoom);}}, new ArrayList<Task.TaskState>());
            Task.TaskAction taObserve = new Task.TaskAction("observe" + room);
            Task observe = new Task(new ArrayList<Task.TaskAction>(){{add(taObserve);}}, new ArrayList<Task.TaskState>());
            Responsibility resClean = new Responsibility("clean" + room,new ArrayList<Responsibility>(),cleanroom,Responsibility.ResType.rt_repeat); 
            Responsibility resCleanBadDirt = new Responsibility("cleanBadDirt" + room,new ArrayList<Responsibility>(),cleanroom,Responsibility.ResType.rt_repeat);
            Responsibility resObserve = new Responsibility("observe" + room,new ArrayList<Responsibility>(),observe,Responsibility.ResType.rt_repeat); 
            cleanSubRes.add(resClean);
            observeSubRes.add(resObserve);
            safetySubRes.add(resCleanBadDirt);
        }

        Task.TaskAction taSendToHuman = new Task.TaskAction("sendToHuman");
        Task.TaskState tsBadDirt = new Task.TaskState("checkBadDirt");
        Task sendToHuman = new Task(new ArrayList<Task.TaskAction>(){{add(taSendToHuman);}}, new ArrayList<Task.TaskState>());
        Task checkDirtLevel = new Task(new ArrayList<Task.TaskAction>(), new ArrayList<Task.TaskState>(){{add(tsBadDirt);}});
        Responsibility resReportToHuman = new Responsibility("reportToHuman",new ArrayList<Responsibility>(),sendToHuman,Responsibility.ResType.rt_oneshot);
  
        Responsibility resClean = new Responsibility("clean",cleanSubRes,new Task(),Responsibility.ResType.rt_repeat); 
        Responsibility resObserve = new Responsibility("observe",observeSubRes,new Task(),Responsibility.ResType.rt_repeat); 
        
        ArrayList<Responsibility> cleanlinessSubRes = new ArrayList<Responsibility>();
        cleanlinessSubRes.add(resObserve);cleanlinessSubRes.add(resClean);
        Responsibility resCleanliness = new Responsibility("cleanliness",cleanlinessSubRes,new Task(),Responsibility.ResType.rt_repeat); 

        ArrayList<Responsibility> safetyFailRes = new ArrayList<Responsibility>();
        safetyFailRes.add(resReportToHuman);
        Responsibility resSafety = new Responsibility("safety",safetySubRes,checkDirtLevel,Responsibility.ResType.rt_repeat,safetyFailRes); 

        ArrayList<Responsibility> janitorialSubRes = new ArrayList<Responsibility>();
        janitorialSubRes.add(resSafety);janitorialSubRes.add(resCleanliness);
        Responsibility resJanitorial = new Responsibility("janitorial",janitorialSubRes,new Task(),Responsibility.ResType.rt_repeat); 

        return resJanitorial;
    }    

    public static Responsibility setupCleanerResponsiblity()
    {
        Task.TaskAction taSendStatus = new Task.TaskAction("sendStatus");
        Task sendStatus = new Task(new ArrayList<Task.TaskAction>(){{add(taSendStatus);}}, new ArrayList<Task.TaskState>());
        Responsibility resJanitorial = new Responsibility("report",new ArrayList<Responsibility>(),sendStatus,Responsibility.ResType.rt_repeat); 
        return resJanitorial;
    }

    public static void generateDelegatedRes(int numOfRooms)
    {
        for (int i = 0; i < numOfRooms; i++)
        {
            char room = (char)('A' + i);
            Task.TaskAction taCleanRoom = new Task.TaskAction("cleanroom" + room);
            Task cleanroom = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoom);}}, new ArrayList<Task.TaskState>());
            Responsibility resClean = new Responsibility("clean" + room,new ArrayList<Responsibility>(),cleanroom,Responsibility.ResType.rt_oneshot); 
            Responsibility resCleanBadDirt = new Responsibility("cleanBadDirt" + room,new ArrayList<Responsibility>(),cleanroom,Responsibility.ResType.rt_oneshot);
            resToDelegate.add(resClean);
            resToDelegate.add(resCleanBadDirt);
        }
    }

    public static Responsibility findDelegateRes(String name)
    {
        for (Responsibility res : resToDelegate)
        {
            if (res.getName().equals(name))
            {
                return res;
            }
        }
        return null;
    }
}
