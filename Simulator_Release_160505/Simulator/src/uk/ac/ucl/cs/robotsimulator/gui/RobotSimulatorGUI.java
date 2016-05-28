package uk.ac.ucl.cs.robotsimulator.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.ac.ucl.cs.robotsimulator.simulation.JsonScenarioGenerator;
import uk.ac.ucl.cs.robotsimulator.simulation.ScenarioGenerator;
import uk.ac.ucl.cs.robotsimulator.simulation.SimulationEngine;

public class RobotSimulatorGUI extends JFrame implements ActionListener
{
	private final static Logger _logger = Logger.getLogger(RobotSimulatorGUI.class.getName());

	private static int GAP = 5;

	private static boolean running = false;

	/**
	 *
	 */
	private static final long serialVersionUID = -4715674095253936603L;
	private static int TOPPANEL_HEIGHT = 60;
	private Timer _animationTimer;
	private SimulationEngine _simulationEngine;

	private JPanel bottomPanel;
	private JPanel contentPane;
	private JTextArea displayTextField;
	private SimulationEngineViewDrawingPanel dPanel;
	private int DPANEL_HEIGHT = 400;
	private int DPANEL_WIDTH = 800;
	private JTextField MouseXYtextField;
	private JToggleButton MouseXYToggleButton;

	private final JTextArea outputTextArea = new JTextArea();
	
	private final JSlider _zoomSlider = new JSlider();

	private JTextField RobotXYtextField;

	private JToggleButton RobotXYToggleButton;
	
	// The starting directory
	File _lastDirectory = new File(System.getProperty("user.dir"));

	protected double scaleRatio = 0.4;
	
	private File _lastSelectedFile = null;

	public RobotSimulatorGUI() throws HeadlessException
	{
		// TODO Auto-generated constructor stub
	}

	public RobotSimulatorGUI(GraphicsConfiguration gc)
	{
		super(gc);
		// TODO Auto-generated constructor stub
	}

	public RobotSimulatorGUI(String title) throws HeadlessException
	{
		super(title);
		// TODO Auto-generated constructor stub
	}

	public RobotSimulatorGUI(String title, GraphicsConfiguration gc)
	{
		super(title, gc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		repaint();
	}

	public void initialize(String[] args, SimulationEngine simulationEngine)
	{
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowDeactivated(WindowEvent e)
			{
				running = false;
			}
		});
		
		ScenarioGenerator sg = simulationEngine.getScenarioGenerator();
		
		if (sg != null)
		{		
			setTitle(sg.getGUIWindowTitle());
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DPANEL_HEIGHT = (int) simulationEngine.getArena().getExtent().y;
		DPANEL_WIDTH = (int) simulationEngine.getArena().getExtent().x;

		setBounds(0, 0, DPANEL_WIDTH + (2 * GAP), DPANEL_HEIGHT + TOPPANEL_HEIGHT + (3 * GAP) + 200);

		// Create the simulation engine
		_simulationEngine = simulationEngine;

		// create menu bar
		final JMenuBar menubar = new JMenuBar();

		final ImageIcon iconLoad = new ImageIcon("load.png");
		final ImageIcon iconExit = new ImageIcon("exit.png");

		final JMenu fileMenu = new JMenu("File");
		//final JMenu controlsMenu = new JMenu("Controls");
		//final JMenu helpMenu = new JMenu("Help");

		// Handle the load map menu item
		final JMenuItem loadMi = new JMenuItem("Load Map", iconLoad);
		loadMi.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e)
				{
					// open dialog to load scenery file
					final JFileChooser c = new JFileChooser();
					final FileFilter filter = new FileNameExtensionFilter("Map", "json");
					c.setFileFilter(filter);
					c.setCurrentDirectory(_lastDirectory);
		
					// Load the scenario file
					final int rVal = c.showOpenDialog(RobotSimulatorGUI.this);
					if (rVal == JFileChooser.APPROVE_OPTION)
					{
						final File selectedFile = c.getSelectedFile();
						_logger.info("Selected file: " + selectedFile.getAbsolutePath());
						JsonScenarioGenerator sg = new JsonScenarioGenerator();
						sg.loadSceneryFile(selectedFile);
						setTitle(sg.getGUIWindowTitle());
						_lastDirectory = selectedFile.getParentFile();
						_lastSelectedFile = selectedFile;
						_simulationEngine.setScenarioGenerator(sg);
						_simulationEngine.reset();
					}
					if (rVal == JFileChooser.CANCEL_OPTION)
					{
						_logger.info("Operation cancelled");
					}
				}
			}
		);
		
		final JMenuItem refreshMap = new JMenuItem("Refresh Map", iconLoad);
		refreshMap.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e)
				{
					if (_lastSelectedFile != null)
					{
						JsonScenarioGenerator sg = new JsonScenarioGenerator();
						sg.loadSceneryFile(_lastSelectedFile);
						setTitle(sg.getGUIWindowTitle());
						_simulationEngine.setScenarioGenerator(sg);
						_simulationEngine.reset();
					}
				}
			}
		);
		
		final JMenuItem exitMi = new JMenuItem("Exit", iconExit);
		exitMi.setToolTipText("Exit application");

		/*
		final JMenuItem prefMi = new JMenuItem("Simulation preferences", iconLoad);
		final JMenuItem resetMi = new JMenuItem("Reset robot position", iconExit);
		resetMi.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// reset robot position
				resetRobot();
				repaint();
			}

			private void resetRobot()
			{
				_simulationEngine.reset();
			}
		});
*/
		
		exitMi.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						System.exit(0);
					}
				});

		fileMenu.add(loadMi);
		fileMenu.add(refreshMap);
		fileMenu.addSeparator();
		fileMenu.add(exitMi);

		//controlsMenu.add(prefMi);
		//controlsMenu.add(resetMi);

		menubar.add(fileMenu);
		//menubar.add(controlsMenu);
		menubar.add(Box.createHorizontalGlue());
		//menubar.add(helpMenu);

		setJMenuBar(menubar);
		//
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 200)));
		contentPane.setBounds(GAP, GAP, DPANEL_WIDTH + (2 * GAP), DPANEL_HEIGHT + (2 * GAP));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		topPanel.setBounds(5, 5, DPANEL_WIDTH, TOPPANEL_HEIGHT);
		contentPane.add(topPanel);
		topPanel.setLayout(null);

		/*
		displayTextField = new JTextArea();
		displayTextField.setFont(new Font("Times", Font.PLAIN, 17));
		displayTextField.setForeground(SystemColor.activeCaption);
		displayTextField.setText("localhost ready\n127.0.0.1");
		displayTextField.setEditable(false);
		displayTextField.setBackground(new Color(0, 0, 255));
		displayTextField.setBounds(1361, 6, 133, 48);
		topPanel.add(displayTextField);

		MouseXYtextField = new JTextField();
		// XYtextField.setText("x= , y=");
		MouseXYtextField.setForeground(Color.ORANGE);
		MouseXYtextField.setEditable(false);
		MouseXYtextField.setBounds(6, 4, 134, 20);
		topPanel.add(MouseXYtextField);
		MouseXYtextField.setColumns(10);

		RobotXYtextField = new JTextField();
		RobotXYtextField.setForeground(Color.ORANGE);
		RobotXYtextField.setEditable(false);
		RobotXYtextField.setBounds(6, 34, 134, 20);
		topPanel.add(RobotXYtextField);
		RobotXYtextField.setColumns(10);
		*/

		//MouseXYToggleButton = new JToggleButton("ON|OFF");
		//MouseXYToggleButton.addActionListener(e -> MouseXYtextField.setText(""));
		//MouseXYToggleButton.setSelected(true);
		//MouseXYToggleButton.setBounds(143, 1, 80, 29);
		//topPanel.add(MouseXYToggleButton);

		/*
		RobotXYToggleButton = new JToggleButton("ON|OFF");
		RobotXYToggleButton.addActionListener(e -> RobotXYtextField.setText(""));
		RobotXYToggleButton.setSelected(true);
		RobotXYToggleButton.setBounds(143, 31, 80, 29);
		topPanel.add(RobotXYToggleButton);
		*/

		_zoomSlider.setValue((int) (100 * scaleRatio));
		_zoomSlider.setPaintLabels(true);
		_zoomSlider.setMaximum(200);
		_zoomSlider.setMajorTickSpacing(50);
		_zoomSlider.setMinorTickSpacing(10);
		_zoomSlider.setPaintTicks(true);
		_zoomSlider.setBounds(245, 6, 252, 48);
		topPanel.add(_zoomSlider);

		dPanel = new SimulationEngineViewDrawingPanel(_simulationEngine);
		dPanel.setBackground(Color.WHITE);
		dPanel.setBounds(GAP, TOPPANEL_HEIGHT + (2 * GAP), DPANEL_WIDTH, DPANEL_HEIGHT);

		contentPane.add(dPanel);
		dPanel.initialize(_simulationEngine);
		dPanel.setViewScale(scaleRatio);

		// Register the callback; do it down here because it guarantees that dPanel has been set up
		_zoomSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent event)
			{
				dPanel.setViewScale(_zoomSlider.getValue() / 100.0);
			}
		});

		bottomPanel = new JPanel();
		bottomPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		bottomPanel.setForeground(Color.BLACK);
		bottomPanel.setBackground(Color.WHITE);

		bottomPanel.setBounds(10, 875, 1480, 96);

		contentPane.add(bottomPanel);
		bottomPanel.setLayout(null);
		outputTextArea.setLineWrap(true);
		outputTextArea.setFont(new Font("Lucida Grande", Font.ITALIC, 11));
		outputTextArea.setBounds(1150, 6, 324, 117);
		outputTextArea.setEditable(false);
		// outputTextArea.setColumns(27);
		outputTextArea.setBackground(Color.WHITE);
		outputTextArea.setForeground(Color.BLACK);
		// JScrollPane scroll = new JScrollPane (dPanel);
		// scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// contentPane.add(scroll);

		// bottomPanel.add(outputTextArea);

		// repaint();

		addMouseWheelListener(new ScaleHandler());
		
		// Set the timer - this specifies the rate at which the GUI is repainted
		_animationTimer = new Timer(20, this);
		_animationTimer.start();
	}
	
	class ScaleHandler implements MouseWheelListener
	{

		@Override
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			doScale(e);
		}

		// not sure still needed
		private void doScale(MouseWheelEvent e)
		{
			final int x = e.getX();
			final int y = e.getY();

			if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
			{
				int scaleValue = _zoomSlider.getValue();
				
				scaleValue = Math.min(200, Math.max(0, scaleValue + e.getWheelRotation() * 5));
				
				_zoomSlider.setValue(scaleValue);
				
				dPanel.setViewScale(scaleValue / 100.0);
				
				System.out.println(e.getWheelRotation());
				//zoomSlider.setValue((int) (100 * scaleRatio));

				//_viewScale = e.getWheelRotation() * 5f;
			}
			//dPanel.setViewScale(zoomSlider.getValue() / 100.0);

		}
	}
}
