package Environment;

import java.awt.Component;

import Agents.Agent;
import Responsibility.ResponsibilityModel.Node;

public abstract class Environment 
{
    protected WorldCell[][] world;
    public enum AgentAction {aa_moveup, aa_movedown, aa_moveright, aa_moveleft, aa_clean, aa_observedirt, aa_moveupleft, aa_moveupright, aa_movedownleft, aa_movedownright, aa_finish, aa_none}
    public abstract void applyAction(Agent ag, AgentAction action);
    public abstract void saveData(String saveLoc);
    public abstract Component getGUIPanel();
    public abstract void updateGUIPanel(Node node);
    public abstract void nodeUpdate(Node node);
    
}
