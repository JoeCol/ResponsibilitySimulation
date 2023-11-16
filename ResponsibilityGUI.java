import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;

import Agents.Agent;
import CleaningEnvironment.CleaningWorld;
import Environment.Environment;
import Responsibility.NodeUpdate;
import Responsibility.ResponsibilityModel;
import Responsibility.ResponsibilityModel.Node;

import javax.swing.JLabel;
import java.util.*;

public class ResponsibilityGUI {

	private JFrame frmResponsibilityGwen;
	private static ResponsibilityModel resModel;
	private static Environment env = new CleaningWorld();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		String saveLoc = "output/";
		int simSpeed = 0;
		boolean gui = true;
		
		String agentFile = "";
		int simSteps = 10000;
		/*int dirtInt = 15; 
		int badDirtInt = 5;
		String worldLoc = "14Rooms.world";*/
		for (int i = 0; i < args.length; i++)
		{
			switch (args[i].toLowerCase())
			{
			case "simsteps":
				simSteps = Integer.valueOf(args[++i]);
				break;
			case "saveloc":
				saveLoc = args[++i];
				break;
			/*case "dirtinterval":
				dirtInt = Integer.valueOf(args[++i]);
				badDirtInt = Integer.valueOf(args[++i]);
				break;
			case "worldlocation":
				worldLoc = args[++i];
				break;*/
			case "agents":
				agentFile = args[++i];
				break;
			case "cleaning":
				env = new CleaningWorld(Arrays.copyOfRange(args,i,args.length));
				break;
			case "speed":
				simSpeed = Integer.valueOf(args[++i]);
				break;
			case "nogui":
				gui = false;
				break;
			default:
				System.out.println("Unrecognised argument: " + args[i]);
			}
		}
		resModel = new ResponsibilityModel();
		resModel.setup(loadAgentsFromFile(agentFile), env);
		
		if (gui)
		{
			EventQueue.invokeLater(new Runnable() 
			{
				public void run() {
					try 
					{
						ResponsibilityGUI window = new ResponsibilityGUI();
						window.frmResponsibilityGwen.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		for (int i = 0; i < simSteps; i++)
		{
			resModel.doStep();
			if (simSpeed > 0)
			{
				try
				{
					Thread.sleep(simSpeed);
				}
				catch (Exception e)
				{

				}
			}
		}
		env.saveData(saveLoc);
	}

	private static ArrayList<Agent> loadAgentsFromFile(String agentFile) {
		return null;
	}

	/**
	 * Create the application.
	 */
	public ResponsibilityGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmResponsibilityGwen = new JFrame();
		frmResponsibilityGwen.setTitle("Responsibility Gwen");
		frmResponsibilityGwen.setBounds(100, 100, 701, 521);
		frmResponsibilityGwen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		frmResponsibilityGwen.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JLabel lblSimStep = new JLabel();
		panel.add(lblSimStep);
		JLabel lblDirt = new JLabel("Dirt: 0 Bad Dirt: 0");
		panel.add(lblDirt);
		
		frmResponsibilityGwen.getContentPane().add(env.getGUIPanel(), BorderLayout.CENTER);
		
		//visual.setLayout(new GridLayout(env.getHeight(), env.getWidth(), 0, 0));
		resModel.addWorldListeners(new NodeUpdate()
		{

			@Override
			public void nodeUpdate(Node node) 
			{
				/*lblSimStep.setText("Steps Remaining: " + time);
				lblDirt.setText("Dirt: " + dirt + " Bad Dirt: " + badDirt);
						visual.setWorld(world, agentLocations, agentColours);*/
				env.updateGUIPanel(node);
			}

		});
		
	}

}
