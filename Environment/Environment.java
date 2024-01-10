package Environment;

import java.awt.Component;
import java.util.ArrayList;
import Agents.Agent;
import Helper.Pair;
import Responsibility.ResponsibilityModel.Node;

public abstract class Environment 
{
    protected WorldCell[][] world;
    protected ArrayList<Pair<Agent,String>> previousActions = new ArrayList<Pair<Agent,String>>();
    public enum AgentAction {aa_moveup, aa_movedown, aa_moveright, aa_moveleft, aa_clean, aa_observedirt, aa_moveupleft, aa_moveupright, aa_movedownleft, aa_movedownright, aa_finish, aa_none, aa_pickup, aa_putdown, aa_putoutfire}
    public abstract void applyAction(Agent ag, AgentAction action);
    public abstract void saveData();
    public abstract Component getGUIPanel();
    public abstract void updateGUIPanel(Node node);
    public abstract void nodeUpdate(Node node);
    public WorldCell[][] getWorldCells() {
        return world;
    }

    public ArrayList<Pair<Agent,String>> getPreviousActions()
    {
        return previousActions;
    }

    public void recordAction(Agent ag, String action)
    {
        previousActions.add(new Pair<Agent,String>(ag, action));
    }

    public void newStep()
    {
        previousActions.clear();
    }
    
}
