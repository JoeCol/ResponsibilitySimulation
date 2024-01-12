package SREnvironment;
import Responsibility.Responsibilities;
import Responsibility.Responsibility;
import Responsibility.TaskResponsibility;
import Responsibility.Responsibility.Concludes;

import java.util.ArrayList;
import java.util.HashMap;

import Agents.Agent;

public class SRStartingResponsibilities 
{
    public static HashMap<Agent,Responsibilities> getAll(SRWorld env, ArrayList<Agent> ags)
    {
        HashMap<Agent,Responsibilities> toRet = new HashMap<Agent,Responsibilities>();
        for (Agent ag : ags)
        {
            Responsibilities allRes = new Responsibilities();
            //Moral Responsibility
            MoralResponsibility priortiseHumansMoral = new MoralResponsibility();
            Responsibility prioritseHumans = new Responsibility("PrioritiseHumans", new ArrayList<Responsibility>(), priortiseHumansMoral, Concludes.rt_repeat);
            allRes.addActiveRes(prioritseHumans);
            //Confirm areas clear responsibility
            ArrayList<Responsibility> confirmSubResponsibilities = new ArrayList<Responsibility>();
            for (Character c : env.zones)
            {
                if (c != '0')
                {
                    TaskResponsibility confirmClearZone = new TaskResponsibility(null, null);
                    Responsibility confirmClearZoneRes = new Responsibility("ConfirmZone" + c + "Clear", new ArrayList<Responsibility>(), confirmClearZone, Concludes.rt_oneshot);
                    confirmSubResponsibilities.add(confirmClearZoneRes);
                }
            }
            TaskResponsibility confirmClearTask = new TaskResponsibility(null, null);
            Responsibility confirmClearRes = new Responsibility("ConfirmAllZonesClear", confirmSubResponsibilities, confirmClearTask, Concludes.rt_oneshot);
            allRes.addActiveRes(confirmClearRes);
            toRet.put(ag, allRes);
        }
        return toRet;
    }
}
