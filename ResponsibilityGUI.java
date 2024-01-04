import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;

import Agents.Agent;
import Agents.CleanerAgent;
import Agents.ManagerAgent;
import CleaningEnvironment.CleaningStartingResponsibilities;
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
	private static Environment env;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		int simSpeed = 0;
		boolean gui = true;
		int simSteps = 10000;
		resModel = new ResponsibilityModel();
		ArrayList<Agent> ags = new ArrayList<Agent>();
		for (int i = 0; i < args.length; i++)
		{
			switch (args[i].toLowerCase())
			{
			case "simsteps":
				simSteps = Integer.valueOf(args[++i]);
				break;
			case "cleaning":
				env = new CleaningWorld(Arrays.copyOfRange(args,i + 1,args.length));//We are in cleaning scenario
				ags.add(new CleanerAgent("C1"));
				ags.add(new CleanerAgent("C2"));
				ags.add(new ManagerAgent("M1"));
				resModel.addStartingResponsibilities(CleaningStartingResponsibilities.getAll((CleaningWorld)env));
				i = args.length;//Stop processing arguments
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
		
		resModel.addWorldListeners(new NodeUpdate() {

			@Override
			public void nodeUpdate(Node node) {
				env.nodeUpdate(node);
			}
			
		});
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
		resModel.setup(ags, env);
		((CleaningWorld)env).setupManagerAgentHelper(ags.get(2));//addobservations to manager agent to setup default care values

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
		env.saveData();
		System.exit(0);//Close program down
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
				lblSimStep.setText("Time: " + node.timet);
				lblDirt.setText("Dirt: " + ((CleaningWorld)node.env).getTotalDirt() + " Bad Dirt: " + ((CleaningWorld)node.env).getTotalSCDirt());
				env.updateGUIPanel(node);
			}

		});
		
	}

}
