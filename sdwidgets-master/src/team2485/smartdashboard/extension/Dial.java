package team2485.smartdashboard.extension;

// This Java Application is being used as a testing grounds
// for several issues.  Some of these issues are looking at
// JOptionPane versus JDialog, and checking for default key behavior
// in components like JDialogs, to name a few.....
//
// January - April, 1998
// menovak@facstaff.wisc.edu


import java.awt.*;
import java.awt.event.*;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class Dial extends JPanel {

	public static JFrame jf;
	Dial dial;
	public Dial () {  // Dial constructor
		dial = this;
	setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

// not needed for this demonstration of various dialog boxes.........
// first create a Swing  button
//	JButton a = new JButton("Just Another JButton");
//	a.setMnemonic('J');
//	a.setFont(new Font("SansSerif", Font.PLAIN, 18));

//	JPanel jp = new JPanel();
//	jp.add(a);
//	add(jp);

	jf.setBounds(300,300,350,150);

	// add Window Listner to close properly
	jf.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	jf.getContentPane().setLayout(new BorderLayout());

	// next create a Swing menu
		JMenu c = new JMenu("File ");
		c.setFont(new Font("SansSerif", Font.PLAIN, 18));
		c.setMnemonic('f');
			
		JMenuItem item = new JMenuItem("Large Text Example");
		JMenuItem item1 = new JMenuItem("J Option Pane Example # 1");
		JSeparator item2 = new JSeparator();
		JMenuItem item3 = new JMenuItem("J Dialog Example ");
		item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK, false));
		JMenuItem item4 = new JMenuItem("J Option Pane Example # 2");

	// add action listeners to the menu items...
		item1.addActionListener(new ActionListener() 
		{
			public void actionPerformed (ActionEvent e)
			{
				String result;
				result = JOptionPane.showInputDialog(Dial.this, "Please enter your name: ");
				System.out.println("You just typed the following: " + result);
				System.out.println();
				System.out.println("action event was " + e);
			}
		});

	item4.addActionListener(new ActionListener() 
		{
			public void actionPerformed (ActionEvent e)
			{
				String result;
				JLabel templab = new JLabel("My Own Text Label Here :    ");
				JTextField temptxt = new JTextField(10);
				templab.setLabelFor(temptxt);

				Object[] panel = {templab, temptxt}; 
				JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION );
//				pane.setWantsInput(true);  causes a TextField in the Dialog Box
				JDialog jdogtest = pane.createDialog(Dial.this, "My Own Input Title Here ");
//				jdogtest.setModal(false);
				jdogtest.show();
				
				Object selectedValue = pane.getValue();
				if (selectedValue instanceof Integer)
					{
					if (((Integer) selectedValue).intValue() == JOptionPane.OK_OPTION)
						{
						result = temptxt.getText();
						System.out.println("You just typed the following: " + result);
						}
					}
				System.out.println();
				System.out.println("action event was " + e);
				jdogtest.dispose();
			}
		});

		item3.addActionListener(new ActionListener() 
		{
			public void actionPerformed (ActionEvent e)
			{
				System.out.println("action event was " + e);
				MyDialog mydialog = new MyDialog(jf, "Input using JDialog ");
			}
		});
				

		item.addActionListener(new ActionListener() 
		{
			public void actionPerformed (ActionEvent e)
			{
			System.out.println("action event was " + e);
			AnotherDialog adialog = new AnotherDialog(jf, "JDialog Input with Large Print ");
			}
		});

		c.add(item1);
		c.add(item4);
		c.add(item);
		c.add(item2);
		c.add(item3);

		JMenuBar jmenuBar = new JMenuBar();
		jmenuBar.add(c);
		jf.getContentPane().add("North", jmenuBar);


	}  // end Dial constructor


	public static void main(String args[]) {

		try
			{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		catch (java.lang.Exception exc)
			{
			System.out.println("Could not load system Look&Feel. ");
			}
		jf = new JFrame("JDialog Ex. for CSUN '98");
		Dial tt = new Dial();
		jf.getContentPane().add("South", tt);
		jf.setVisible(true);
	}
} // end Dial class


// JDilaog class with lots of stuff, accessibility and keyboard added
class MyDialog extends JDialog //implements KeyListener 
{
	JDialog jd;
	JRootPane jrp;

	public MyDialog (JFrame frame, String title)
	{ // MyDialog Constructor
		super(frame, title, false);
		jd = this;

		jrp = jd.getRootPane();

		JPanel holdlabel = new JPanel();
		holdlabel.setLayout(new BoxLayout(holdlabel, BoxLayout.Y_AXIS));
		JLabel label3 = new JLabel("Please enter your name:                             ");
///		JButton label3 = new JButton("Please enter your name:");
		label3.setDisplayedMnemonic('P');
///		label3.setMnemonic('P');
///		label3.setBorderPainted(false);
		JTextField jtxt3 = new JTextField();
		label3.setLabelFor(jtxt3);

// still need a listener on the JTextField, to handle the Enter
// key (default to OK button), as the registerKeyboardAction code
// below didn't solve the problem for Swing 0.7....maybe by Swing 1.0?
//		jtxt3.addKeyListener(new MyKeyListener (jd, jtxt3));
// thought this might tie the textfield to the label via Focus, but 
// it doesn't really do that....
//		jtxt3.setFocusAccelerator('P');
//		jtxt3.setRequestFocusEnabled(true);

		Icon dukeIcon = new ImageIcon("dukeWave.gif");
		JLabel label4 = new JLabel(dukeIcon);
//		JLabel label4 = new JLabel("Duke Waving", dukeIcon, JLabel.CENTER);
//		label4.setVerticalTextPosition(JLabel.BOTTOM);
//		label4.setHorizontalTextPosition(JLabel.CENTER);
		label4.setLabelFor(jtxt3);
		label4.getAccessibleContext().setAccessibleDescription("Duke waving and asking for your name");
		label4.getAccessibleContext().setAccessibleName("Icon image of Duke waving ");
		label4.setToolTipText("Icon image of Duke waving ");

		JPanel iconPanel = new JPanel();
		iconPanel.add(label4);

		holdlabel.add(Box.createVerticalStrut(10));
		holdlabel.add(label3);
		holdlabel.add(Box.createVerticalStrut(3));
		holdlabel.add(jtxt3);
		holdlabel.add(Box.createVerticalGlue());
		holdlabel.add(Box.createVerticalStrut(6));

		JPanel holdbut = new JPanel();
		holdbut.setLayout(new FlowLayout());
		JButton a3 = new JButton ("OK    ");
		a3.getAccessibleContext().setAccessibleDescription("Select OK after typing your name");
		a3.setToolTipText("Select OK after typing your name ");
		JButton b3 = new JButton ("Cancel");
		b3.getAccessibleContext().setAccessibleDescription("Select Cancel to ignore this request");
		b3.setToolTipText("Select Cancel to ignore this request ");

// no difference in end result when registering on the button, or
// the RootPane, as to what happens to the OK (enter key) when the
// JTextField has focus.  Problem:  When the JTextField has focus, 
// pressing the Enter key, which should globally default to the OK
// button, doesn't work. The Escape key however, which maps to the
// Cancel button, does work.  Also, when either of the buttons has
// focus, either can be accessed using the Enter or Escape keys....

/*		a3.registerKeyboardAction(
			new DefaultAction((AbstractButton) a3),
			KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
			JComponent.WHEN_IN_FOCUSED_WINDOW);

		b3.registerKeyboardAction(
			new DefaultAction((AbstractButton) b3),
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false),
			JComponent.WHEN_IN_FOCUSED_WINDOW);    
*/
		jrp.registerKeyboardAction(
			new DefaultAction((AbstractButton) a3),
			KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
			JComponent.WHEN_IN_FOCUSED_WINDOW);

		jrp.registerKeyboardAction(
			new DefaultAction((AbstractButton) b3),
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false),
			JComponent.WHEN_IN_FOCUSED_WINDOW);

//	supposedly this will be in 1.0 release of Swing?
//		jrp.setDefaultButton(a3);

		holdbut.add(a3);
		holdbut.add(b3);
	
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(iconPanel, BorderLayout.WEST);
		getContentPane().add(holdlabel, BorderLayout.EAST);
		getContentPane().add(holdbut, BorderLayout.SOUTH);
		getContentPane().add(Box.createVerticalStrut(6));

		a3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("action event from a3 was: " + e);
				setVisible(false);
				dispose();
			}
		});

		b3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("action event from b3 was: " + e);
				setVisible(false);
				dispose();
			}
		});

		jd.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		jd.setBounds(350,350,400,100);
		setModal(false);
		setVisible(true);
		pack();
		show();

// start with focus on the text field.....after painted, something wrong with this ??
//		jtxt3.requestFocus();

	}  // end MyDialog Constructor
} // end MyDialog Class

class MyKeyListener implements KeyListener {
	JTextField jtxt;
	JDialog jdial;

// add key listeners for keypressed, keytyped, keyreleased...
	MyKeyListener (JDialog jdialog, JTextField jtext) {
	  // constructor
		jtxt = jtext;
		jdial = jdialog;
	} // end MyKeyListener constructor

	public void keyReleased(KeyEvent key)
		{
		}
	public void keyTyped(KeyEvent key)
		{
		}
	public void keyPressed(KeyEvent key)
		{
		System.out.println("key event was: " + key);
		if (key.getKeyCode() == KeyEvent.VK_ENTER)
			{
			System.out.println("You just typed : " + jtxt.getText());
			jdial.setVisible(false);
			jdial.dispose();
			}
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
			jdial.setVisible(false);
			jdial.dispose();
			}
	}
} // end MyKeyListener Class
//}   // end MyDialog Class, was here, before I added the JTextField
// MyKeyListener stuff...........

// this class "intended" to handle the default button to key binding
// action for Windows dialog boxes, where Enter = OK, and Escape = Cancel
// buttons by default, from within anywhere when dialog has focus....

class DefaultAction extends AbstractAction 
{
	AbstractButton owner;
	DefaultAction (AbstractButton owner) 
	{		// constructor
		super("DefaultAction");
		this.owner = owner;
	} // end DefaultAction constructor

	public void actionPerformed (ActionEvent e) 
	{
		owner.doClick();
		System.out.println("action event from JDialog was :" + e);
	}
}

// just the start of another dialog box, with large print, not
// much else.......
// listeners not implemented, would be similar to MyDialog Class...
// note, this Dialog is not disposed of properly either at this time...
// to fix these things, look at MyDialog Code above....

class AnotherDialog extends JDialog // implements KeyListener 
{
	JDialog ad;
	public AnotherDialog (JFrame frame, String title)
	{ // AnotherDialog Constructor
		super(frame, title, false);
		ad = this;
		
		JPanel panel0 = new JPanel();
		panel0.setLayout(new BoxLayout(panel0, BoxLayout.Y_AXIS));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

		JLabel label1 = new JLabel("Please enter your name: ");
		label1.setFont(new Font("SanSerif", Font.PLAIN, 24));

		panel1.add(label1);
		label1.setHorizontalTextPosition(label1.LEFT);
		label1.setVerticalTextPosition(label1.TOP);
		JTextField jtxt = new JTextField();
		jtxt.setFont(new Font("SanSerif", Font.PLAIN, 24));
		label1.setLabelFor(jtxt);
		panel1.add(jtxt);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		JButton a1 = new JButton ("OK    ");
		a1.setFont(new Font("SanSerif", Font.PLAIN, 24));
		JButton b1 = new JButton ("Cancel");
		b1.setFont(new Font("SanSerif", Font.PLAIN, 24));
		panel2.add(a1);
		panel2.add(b1);

		panel0.add(panel1);
		panel0.add(panel2);

		getContentPane().add(panel0);
		setVisible(true);
		pack();
		show();
	} // end AnotherDialog constructor

} // end AnotherDialog class