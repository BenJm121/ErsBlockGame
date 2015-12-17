import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ErsBlocksGame extends JFrame{
	public final static int PER_LINE_SCORE = 100;
	public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 10;
	public final static int MAX_LEVEL = 10;
	public final static int DEFAULT_LEVEL = 1;
	
	private GameCanvas canvas;
	private ErsBlock block;
	private ControlPanel ctrlPanel;
	private JMenuBar bar=new JMenuBar();
	private boolean playing=false;
	
	private JMenu 
    mGame=new JMenu("游戏"),
    mControl=new JMenu("控制"),
    mWindowStyle=new JMenu("窗口风格"),
    mInfo=new JMenu("帮助");
    private JMenuItem
    miNewGame=new JMenuItem("新游戏"),
	miSetBlockColor=new JMenuItem("设置方块颜色"),
	miSetBackColor=new JMenuItem("设置背景颜色"),
	miTurnHarder=new JMenuItem("增加难度"),
	miTurnEasier=new JMenuItem("降低难度"),
	miExit=new JMenuItem("退出"),
	miPlay=new JMenuItem("开始"),
	miPause=new JMenuItem("暂停"),
	miResume=new JMenuItem("继续"),
	miStop=new JMenuItem("停止"),
	miAuthor=new JMenuItem("作者：罗波"),
	miSourceInfo=new JMenuItem("版本：1.0");
    private JCheckBoxMenuItem
    miAsWindows=new JCheckBoxMenuItem("Windows"),
	miAsMotif=new JCheckBoxMenuItem("Motif"),
	miAsMetal=new JCheckBoxMenuItem("Metal",true);
    
	public ErsBlocksGame(String title)
	{
		super(title);
		setSize(315,392);
		Dimension srcSize=Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((srcSize.width-getSize().width)/2,(srcSize.height-getSize().height)/2);
		createMenu();
		Container container=getContentPane();
		container.setLayout(new BorderLayout(6,0));
		canvas=new GameCanvas(20,12);
		ctrlPanel=new ControlPanel(this);
		container.add(canvas, BorderLayout.CENTER);
		container.add(ctrlPanel, BorderLayout.EAST);
		addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent we) 
		    {
		    	stopGame();
		    	System.exit(0);
			}
		});
		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent ce)
			{
				canvas.fanning();
			}
		});
		setVisible(true);
		canvas.fanning();
		
	} 
	
	public void reset() {
		ctrlPanel.reset();    
		canvas.reset();        

	}

	
	public boolean isPlaying() {
		return playing;
	}

	
	public ErsBlock getCurBlock() {
		return block;
	}

	
	public GameCanvas getCanvas() {
		return canvas;
	}

	
	public void playGame() {
		play();
		ctrlPanel.setPlayButtonEnable(false);
		miPlay.setEnabled(false);
		ctrlPanel.requestFocus();
	}

	
	public void pauseGame() {
		if (block != null) block.pauseMove();
		ctrlPanel.setPauseButtonLabel(false);
		miPause.setEnabled(false);
		miResume.setEnabled(true);
	}

	
	public void resumeGame() {
		if (block != null) block.resumeMove();
		ctrlPanel.setPauseButtonLabel(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.requestFocus();
	}

	
	public void stopGame() {
		playing = false;
		if (block != null) block.stopMove();
		miPlay.setEnabled(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.setPlayButtonEnable(true);
		ctrlPanel.setPauseButtonLabel(true);
	}

	
	public int getLevel() {
		return ctrlPanel.getLevel();
	}

	
	public void setLevel(int level) {
		if (level < 11 && level > 0) ctrlPanel.setLevel(level);
	}

	
	public int getScore() {
		if (canvas != null) return canvas.getScore();
		return 0;
	}

	
	public int getScoreForLevelUpdate() {
		if (canvas != null) return canvas.getScoreForLevelUpdate();
		return 0;
	}

	
	public boolean levelUpdate() {
		int curLevel = getLevel();
		if (curLevel < MAX_LEVEL) {
			setLevel(curLevel + 1);
			canvas.resetScoreForLevelUpdate();
			return true;
		}
		return false;
	}

	
	private void play() {
		reset();
		playing = true;
		Thread thread = new Thread(new Game());
		thread.start();
	}

	
	private void reportGameOver() {
		JOptionPane.showMessageDialog(this, "游戏结束!");
	}

	private void createMenu() {
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

		mWindowStyle.add(miAsWindows);
		mWindowStyle.add(miAsMotif);
		mWindowStyle.add(miAsMetal);

		mInfo.add(miAuthor);
		mInfo.add(miSourceInfo);

		setJMenuBar(bar);

		miPause.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK));
		miResume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));

		miNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stopGame();
				reset();
				setLevel(DEFAULT_LEVEL);
			}
		});
        
		miSetBlockColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Color newFrontColor =
				        JColorChooser.showDialog(ErsBlocksGame.this,
				                "设置方块颜色", canvas.getBlockColor());
				if (newFrontColor != null)
					canvas.setBlockColor(newFrontColor);
			}
		}); 
	
	   
		miSetBackColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Color newBackColor =
				        JColorChooser.showDialog(ErsBlocksGame.this,
				                "设置背景颜色", canvas.getBackgroundColor());
				if (newBackColor != null)
					canvas.setBackgroundColor(newBackColor);
			}
		});

		
		miTurnHarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int curLevel = getLevel();
				if (curLevel < MAX_LEVEL) setLevel(curLevel + 1);
			}
		});
		
		miTurnEasier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int curLevel = getLevel();
				if (curLevel > 1) setLevel(curLevel - 1);
			}
		});
      
		miExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
       
		miPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				playGame();
			}
		});
		
		
		miPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pauseGame();
			}
		});
		
		
		miResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				resumeGame();
			}
		});
		
		miStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stopGame();
			}
		});
        
		
		miAsWindows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				ctrlPanel.fanning();
				miAsWindows.setState(true);
				miAsMetal.setState(false);
				miAsMotif.setState(false);
			}
		});
		miAsMotif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				ctrlPanel.fanning();
				miAsWindows.setState(false);
				miAsMetal.setState(false);
				miAsMotif.setState(true);
			}
		});
		miAsMetal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String plaf = "javax.swing.plaf.metal.MetalLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				ctrlPanel.fanning();
				miAsWindows.setState(false);
				miAsMetal.setState(true);
				miAsMotif.setState(false);
			}
		});

	}
	private void setWindowStyle(String plaf) {
		try {
			UIManager.setLookAndFeel(plaf);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
	}
	
	public class Game implements Runnable
	{
		public void run()
		{
			int col=(int) (Math.random()*(canvas.getCols()-3)),
					style=ErsBlock.STYLES[(int)(Math.random()*7)]
							[(int)(Math.random()*4)];
			while(playing)
			{
				if(block!=null)
				{
					if(block.isAlive())
					{
						try{
							Thread.currentThread().sleep(100);
						}catch(InterruptedException ie)
						{
							ie.printStackTrace();
						}
						continue;
					}
				}
				checkFullLine();
				if(isGameOver())
				{
					miPlay.setEnabled(true);
					miPause.setEnabled(true);
					miResume.setEnabled(false);
					ctrlPanel.setPlayButtonEnable(true);
					ctrlPanel.setPauseButtonLabel(true);
					reportGameOver();
					return;
				}
				block=new ErsBlock(style,-1,col,getLevel(),canvas);
				block.start();
				col=(int) (Math.random()*(canvas.getCols()-3));
				style=ErsBlock.STYLES[(int)(Math.random()*7)]
						[(int)(Math.random()*4)];
				ctrlPanel.setTipStyle(style);
			}
		}
	}
	public void checkFullLine() 
	{
		for (int i = 0; i < canvas.getRows(); i++) 
		{
			int row = -1;
			boolean fullLineColorBox = true;
			for (int j = 0; j < canvas.getCols(); j++) 
			{   
				if (!canvas.getBox(i, j).isColorBox())
				{
					fullLineColorBox = false;
					break;
				}
			}
			if (fullLineColorBox) 
			{
				row = i--;
				canvas.removeLine(row);
				
			}
		}
	}
		
	
	private boolean isGameOver() {
		for (int i = 0; i < canvas.getCols(); i++) {
			ErsBox box = canvas.getBox(0, i);
			if (box.isColorBox()) return true;
		}
		return false;
	}

	public static void main(String[] args)
	{
		new ErsBlocksGame("俄罗斯方块游戏");
	}
}


