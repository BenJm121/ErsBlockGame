import java.awt.*;
import java.awt.event.*;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class ErsMenu extends JFrame{
	JMenuBar bar=new JMenuBar();
	private JMenu 
	        mGame=new JMenu("��Ϸ"),
	        mControl=new JMenu("����"),
	        mWindowStyle=new JMenu("���ڷ��"),
	        mInfo=new JMenu("����");
	private JMenuItem
	        miNewGame=new JMenuItem("����Ϸ"),
			miSetBlockColor=new JMenuItem("���÷�����ɫ"),
			miSetBackColor=new JMenuItem("���ñ�����ɫ"),
			miTurnHarder=new JMenuItem("�����Ѷ�"),
			miTurnEasier=new JMenuItem("�����Ѷ�"),
			miExit=new JMenuItem("�˳�"),
			miPlay=new JMenuItem("��ʼ"),
			miPause=new JMenuItem("��ͣ"),
			miResume=new JMenuItem("����"),
			miStop=new JMenuItem("ֹͣ"),
			miAuthor=new JMenuItem("���ߣ��޲�"),
			miSourceInfo=new JMenuItem("�汾��1.0");
	private JCheckBoxMenuItem
	        miAsWidows=new JCheckBoxMenuItem("Windows"),
			miAsMotif=new JCheckBoxMenuItem("Motif"),
			miAsMetal=new JCheckBoxMenuItem("Metal",true);
	ErsMenu(String name)
	{
		super(name);
		createMenu();
	}
	
	void createMenu()
	{
		bar.add(mGame);
		bar.add(mControl);
		bar.add(mWindowStyle);
		bar.add(mInfo);
		mGame.add(miNewGame);
		mGame.addSeparator();
		mGame.add(miSetBlockColor);
		mGame.add(miSetBackColor);
		mGame.addSeparator();
		mGame.add(miTurnHarder);
		mGame.add(miTurnEasier);
		mGame.addSeparator();
		mGame.add(miExit);
		mControl.add(miPlay);
		mControl.add(miPause);
		mControl.add(miResume);
		mControl.add(miStop);
		mWindowStyle.add(miAsWidows);
		mWindowStyle.add(miAsMotif);
		mWindowStyle.add(miAsMetal);
		mInfo.add(miAuthor);
		mInfo.add(miSourceInfo);
		setJMenuBar(bar);
		
		miExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		miPause.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK));
		miResume.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0)
				);
		/*miSetBlockColor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				Color newFrontColor=
						JColorChooser.showDialog(ErsBlocksGame.this, "���÷�����ɫ", canvas.getBlockColor());
				if(newFrontColor!=NULL)
				{
					canvas.setBlockColor(newFrontColor);
				}
			}
		});*/
		
		/*miSetBackColor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				Color newBackColor=
						JColorChooser.showDialog(ErsBlocksGame.this, "���ñ�����ɫ", canvas.getBackgroundColor());
				if(newBackColor!=NULL)
				{
					canvas.setBackgroundColor(newBackColor);
				}
			}
		});*/
		/*miNewGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				stopGame();
				reset();
				setLevel(DEFAULT_LEVEL);
			}
		});*/
	}

	public static void main(String[] args)
	{
		ErsMenu JMenu=new ErsMenu("����˹����Ĳ˵���ʾ");
		JMenu.setSize(500, 300);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		JMenu.setLocation((scrSize.width-JMenu.getSize().width)/2, (scrSize.height-JMenu.getSize().height)/2);
		JMenu.setResizable(false);
		JMenu.setVisible(true);
	
	}
}
