package Environment;

import java.awt.Component;

public abstract class Environment 
{
    public enum AgentAction {aa_moveup, aa_movedown, aa_moveright, aa_moveleft, aa_clean, aa_observedirt, aa_moveupleft, aa_moveupright, aa_movedownleft, aa_movedownright, aa_finish, aa_none}
    public abstract void applyAction(AgentAction action);
    public abstract void saveData(String saveLoc);
    public abstract Component getGUIPanel();
    public abstract void updateGUIPanel();
    
}
