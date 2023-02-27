import java.util.ArrayList;

public class SetupResponsibilities 
{
    public static Responsibility setupManagerResponsiblities()
    {
        Task.TaskAction taCleanRoomA = new Task.TaskAction("cleanroomA");
        Task.TaskAction taCleanRoomB = new Task.TaskAction("cleanroomB");
        Task.TaskAction taCleanRoomC = new Task.TaskAction("cleanroomC");
        Task.TaskAction taCleanRoomD = new Task.TaskAction("cleanroomD");
        Task.TaskAction taCleanRoomE = new Task.TaskAction("cleanroomE");
        Task.TaskAction taCleanRoomF = new Task.TaskAction("cleanroomF");
        Task.TaskAction taCleanRoomG = new Task.TaskAction("cleanroomG");
        Task.TaskAction taCleanRoomH = new Task.TaskAction("cleanroomH");
        Task.TaskAction taCleanRoomI = new Task.TaskAction("cleanroomI");
        Task.TaskAction taCleanRoomJ = new Task.TaskAction("cleanroomJ");

        Task.TaskAction taObserveA = new Task.TaskAction("observeA");
        Task.TaskAction taObserveB = new Task.TaskAction("observeB");
        Task.TaskAction taObserveC = new Task.TaskAction("observeC");
        Task.TaskAction taObserveD = new Task.TaskAction("observeD");
        Task.TaskAction taObserveE = new Task.TaskAction("observeE");
        Task.TaskAction taObserveF = new Task.TaskAction("observeF");
        Task.TaskAction taObserveG = new Task.TaskAction("observeG");
        Task.TaskAction taObserveH = new Task.TaskAction("observeH");
        Task.TaskAction taObserveI = new Task.TaskAction("observeI");
        Task.TaskAction taObserveJ = new Task.TaskAction("observeJ");

        Task.TaskAction taSendToHuman = new Task.TaskAction("sendToHuman");

        Task.TaskState tsBadDirt = new Task.TaskState("checkBadDirt");

        Task cleanroomA = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomA);}}, new ArrayList<Task.TaskState>());
        Task cleanroomB = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomB);}}, new ArrayList<Task.TaskState>());
        Task cleanroomC = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomC);}}, new ArrayList<Task.TaskState>());
        Task cleanroomD = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomD);}}, new ArrayList<Task.TaskState>());
        Task cleanroomE = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomE);}}, new ArrayList<Task.TaskState>());
        Task cleanroomF = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomF);}}, new ArrayList<Task.TaskState>());
        Task cleanroomG = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomG);}}, new ArrayList<Task.TaskState>());
        Task cleanroomH = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomH);}}, new ArrayList<Task.TaskState>());
        Task cleanroomI = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomI);}}, new ArrayList<Task.TaskState>());
        Task cleanroomJ = new Task(new ArrayList<Task.TaskAction>(){{add(taCleanRoomJ);}}, new ArrayList<Task.TaskState>());

        Task observeA = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveA);}}, new ArrayList<Task.TaskState>());
        Task observeB = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveB);}}, new ArrayList<Task.TaskState>());
        Task observeC = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveC);}}, new ArrayList<Task.TaskState>());
        Task observeD = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveD);}}, new ArrayList<Task.TaskState>());
        Task observeE = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveE);}}, new ArrayList<Task.TaskState>());
        Task observeF = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveF);}}, new ArrayList<Task.TaskState>());
        Task observeG = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveG);}}, new ArrayList<Task.TaskState>());
        Task observeH = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveH);}}, new ArrayList<Task.TaskState>());
        Task observeI = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveI);}}, new ArrayList<Task.TaskState>());
        Task observeJ = new Task(new ArrayList<Task.TaskAction>(){{add(taObserveJ);}}, new ArrayList<Task.TaskState>());

        Task sendToHuman = new Task(new ArrayList<Task.TaskAction>(){{add(taSendToHuman);}}, new ArrayList<Task.TaskState>());
        Task checkDirtLevel = new Task(new ArrayList<Task.TaskAction>(), new ArrayList<Task.TaskState>(){{add(tsBadDirt);}});

        //Manager Responsibilities
        Responsibility resCleanA = new Responsibility("cleanA",new ArrayList<Responsibility>(),cleanroomA,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanB = new Responsibility("cleanB",new ArrayList<Responsibility>(),cleanroomB,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanC = new Responsibility("cleanC",new ArrayList<Responsibility>(),cleanroomC,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanD = new Responsibility("cleanD",new ArrayList<Responsibility>(),cleanroomD,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanE = new Responsibility("cleanE",new ArrayList<Responsibility>(),cleanroomE,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanF = new Responsibility("cleanF",new ArrayList<Responsibility>(),cleanroomF,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanG = new Responsibility("cleanG",new ArrayList<Responsibility>(),cleanroomG,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanH = new Responsibility("cleanH",new ArrayList<Responsibility>(),cleanroomH,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanI = new Responsibility("cleanI",new ArrayList<Responsibility>(),cleanroomI,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanJ = new Responsibility("cleanJ",new ArrayList<Responsibility>(),cleanroomJ,Responsibility.ResType.rt_repeat); 

        Responsibility resObserveA = new Responsibility("observeA",new ArrayList<Responsibility>(),observeA,Responsibility.ResType.rt_repeat); 
        Responsibility resObserveB = new Responsibility("observeB",new ArrayList<Responsibility>(),observeB,Responsibility.ResType.rt_repeat); 
        Responsibility resObserveC = new Responsibility("observeC",new ArrayList<Responsibility>(),observeC,Responsibility.ResType.rt_repeat); 
        Responsibility resObserveD = new Responsibility("observeD",new ArrayList<Responsibility>(),observeD,Responsibility.ResType.rt_repeat); 
        Responsibility resObserveE = new Responsibility("observeE",new ArrayList<Responsibility>(),observeE,Responsibility.ResType.rt_repeat); 
        Responsibility resObserveF = new Responsibility("observeF",new ArrayList<Responsibility>(),observeF,Responsibility.ResType.rt_repeat); 
        Responsibility resObserveG = new Responsibility("observeG",new ArrayList<Responsibility>(),observeG,Responsibility.ResType.rt_repeat); 
        Responsibility resObserveH = new Responsibility("observeH",new ArrayList<Responsibility>(),observeH,Responsibility.ResType.rt_repeat); 
        Responsibility resObserveI = new Responsibility("observeI",new ArrayList<Responsibility>(),observeI,Responsibility.ResType.rt_repeat); 
        Responsibility resObserveJ = new Responsibility("observeJ",new ArrayList<Responsibility>(),observeJ,Responsibility.ResType.rt_repeat); 

        Responsibility resCleanBadDirtA = new Responsibility("cleanBadDirtA",new ArrayList<Responsibility>(),cleanroomA,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanBadDirtB = new Responsibility("cleanBadDirtB",new ArrayList<Responsibility>(),cleanroomB,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanBadDirtC = new Responsibility("cleanBadDirtC",new ArrayList<Responsibility>(),cleanroomC,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanBadDirtD = new Responsibility("cleanBadDirtD",new ArrayList<Responsibility>(),cleanroomD,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanBadDirtE = new Responsibility("cleanBadDirtE",new ArrayList<Responsibility>(),cleanroomE,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanBadDirtF = new Responsibility("cleanBadDirtF",new ArrayList<Responsibility>(),cleanroomF,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanBadDirtG = new Responsibility("cleanBadDirtG",new ArrayList<Responsibility>(),cleanroomG,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanBadDirtH = new Responsibility("cleanBadDirtH",new ArrayList<Responsibility>(),cleanroomH,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanBadDirtI = new Responsibility("cleanBadDirtI",new ArrayList<Responsibility>(),cleanroomI,Responsibility.ResType.rt_repeat); 
        Responsibility resCleanBadDirtJ = new Responsibility("cleanBadDirtJ",new ArrayList<Responsibility>(),cleanroomJ,Responsibility.ResType.rt_repeat); 

        Responsibility resReportToHuman = new Responsibility("reportToHuman",new ArrayList<Responsibility>(),sendToHuman,Responsibility.ResType.rt_oneshot);
        
        ArrayList<Responsibility> cleanSubRes = new ArrayList<Responsibility>();
        cleanSubRes.add(resCleanA);cleanSubRes.add(resCleanB);cleanSubRes.add(resCleanC);cleanSubRes.add(resCleanD);cleanSubRes.add(resCleanE);cleanSubRes.add(resCleanF);cleanSubRes.add(resCleanG);cleanSubRes.add(resCleanH);cleanSubRes.add(resCleanI);cleanSubRes.add(resCleanJ);
        Responsibility resClean = new Responsibility("clean",cleanSubRes,new Task(),Responsibility.ResType.rt_repeat); 
        
        ArrayList<Responsibility> observeSubRes = new ArrayList<Responsibility>();
        observeSubRes.add(resObserveA);observeSubRes.add(resObserveB);observeSubRes.add(resObserveC);observeSubRes.add(resObserveD);observeSubRes.add(resObserveE);observeSubRes.add(resObserveF);observeSubRes.add(resObserveG);observeSubRes.add(resObserveH);observeSubRes.add(resObserveI);observeSubRes.add(resObserveJ);
        Responsibility resObserve = new Responsibility("observe",observeSubRes,new Task(),Responsibility.ResType.rt_repeat); 
        
        ArrayList<Responsibility> cleanlinessSubRes = new ArrayList<Responsibility>();
        cleanlinessSubRes.add(resObserve);cleanlinessSubRes.add(resClean);
        Responsibility resCleanliness = new Responsibility("cleanliness",cleanlinessSubRes,new Task(),Responsibility.ResType.rt_repeat); 

        ArrayList<Responsibility> safetySubRes = new ArrayList<Responsibility>();
        safetySubRes.add(resCleanBadDirtA);safetySubRes.add(resCleanBadDirtB);safetySubRes.add(resCleanBadDirtC);safetySubRes.add(resCleanBadDirtD);safetySubRes.add(resCleanBadDirtE);safetySubRes.add(resCleanBadDirtF);safetySubRes.add(resCleanBadDirtG);safetySubRes.add(resCleanBadDirtH);safetySubRes.add(resCleanBadDirtI);safetySubRes.add(resCleanBadDirtJ);
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
}
