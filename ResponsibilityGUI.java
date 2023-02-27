import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;

import java.util.HashMap;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class ResponsibilityGUI {

	private JFrame frmResponsibilityGwen;
	private CleaningPanel visual = new CleaningPanel();
	private static CleaningWorld world;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		String saveLoc = "output/";
		int simSteps = 10000;
		int dirtInt = 15; 
		int badDirtInt = 5;
		String worldLoc = "10Rooms.world";
		boolean naive = false;
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
			case "dirtinterval":
				dirtInt = Integer.valueOf(args[++i]);
				badDirtInt = Integer.valueOf(args[++i]);
				break;
			case "worldlocation":
				worldLoc = args[++i];
				break;
			case "naive":
				naive = true;
				break;
			default:
				System.out.println("Unrecognised argument: " + args[i]);
			}
		}
		world = new CleaningWorld(simSteps, dirtInt, badDirtInt, worldLoc, saveLoc);
		world.setup_agents(naive);
		
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
		
		world.start();
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
		
		frmResponsibilityGwen.getContentPane().add(visual, BorderLayout.CENTER);
		
		visual.setLayout(new GridLayout(world.getHeight(), world.getWidth(), 0, 0));
		world.addWorldListeners(new UpdateToWorld()
		{

			@Override
			public void worldUpdate(int time, int dirt, int badDirt, WorldCell[][] world,
					HashMap<Agent, Pair<Integer, Integer>> agentLocations, HashMap<Agent, Color> agentColours) {
						lblSimStep.setText("Steps Remaining: " + time);
						lblDirt.setText("Dirt: " + dirt + " Bad Dirt: " + badDirt);
						visual.setWorld(world, agentLocations, agentColours);
			}
		});
		
	}

}
