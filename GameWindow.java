import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class GameWindow extends JFrame implements ActionListener, KeyListener, MouseListener {

  private final static int WINDOW_W = 580, WINDOW_H = 575;
  private static int score;
  private int highScore,lives;
  private JLabel scoreL,highScoreL,statusBarL;

  private JTextField scoreTF,highScoreTF,statusBarTF;

  private JButton startB,pauseB,exitB,restartB;

  private Container c;

  private JPanel mainPanel,infoPanel,buttonPanel;
  GamePanel gamePanel;

  @SuppressWarnings({"unchecked"})
  public GameWindow() {

    setTitle("JUNGLE CRUNCH");
    setSize(WINDOW_W, WINDOW_H);

    scoreL = new JLabel("S C O R E :");
    highScoreL = new JLabel("H I G H S C O R E :");
    statusBarL = new JLabel("S T A T U S :");

    scoreTF = new JTextField (25);
    highScoreTF = new JTextField (25);
    statusBarTF = new JTextField (25);

    scoreTF.setEditable(false);
    highScoreTF.setEditable(false);
    statusBarTF.setEditable(false);

    scoreTF.setBackground(Color.WHITE);
    highScoreTF.setBackground(Color.WHITE);
    statusBarTF.setBackground(Color.WHITE);

    mainPanel = new JPanel();
    FlowLayout flowLayout = new FlowLayout();
    mainPanel.setLayout(flowLayout);

    infoPanel = new JPanel();
    GridLayout gridLayout = new GridLayout(3, 2);
    infoPanel.setLayout(gridLayout);
    infoPanel.setBackground(Color.CYAN);

    infoPanel.add(scoreL);
    infoPanel.add(scoreTF);


    infoPanel.add(highScoreL);
    infoPanel.add(highScoreTF);

    infoPanel.add(statusBarL);
    infoPanel.add(statusBarTF);


    startB = new JButton("S T A R T");
    pauseB = new JButton("P A U S E");
    exitB = new JButton("E X I T");
    restartB = new JButton("R E S T A R T");

    startB.addActionListener(this);
    pauseB.addActionListener(this);
    exitB.addActionListener(this);
    restartB.addActionListener(this);

    buttonPanel = new JPanel();
    GridLayout gridLayout2 = new GridLayout(1, 4);
    buttonPanel.setLayout(gridLayout2);

    buttonPanel.add(startB);
    buttonPanel.add(pauseB);
    buttonPanel.add(exitB);
    buttonPanel.add(restartB);

    gamePanel = new GamePanel();
    gamePanel.setPreferredSize(new Dimension(gamePanel.getWidth(), gamePanel.getHeight()));
    gamePanel.setFocusable(true);
    

    mainPanel.add(infoPanel);
    mainPanel.add(gamePanel);
    mainPanel.add(buttonPanel);

    mainPanel.setBackground(Color.PINK);

    gamePanel.addMouseListener(this);
    mainPanel.addKeyListener(this);
    
   

    c = getContentPane();
    c.add(mainPanel);

    setResizable(true);
   
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    gamePanel.createGameEntities();
    GameWindow.score = 0;
    lives = 1;
    
    if(score > highScore) highScore = score;

    

    scoreTF.setText(Integer.toString(getScore()));   // shows playe score
    highScoreTF.setText(Integer.toString(getHighScore()));  // shows high score
    statusBarTF.setText("GAME LOADED");
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == startB) {
      statusBarTF.setText("G A M E  S T A R T E D");
      gamePanel.renderGame();
      gamePanel.startGame();

      if(gamePanel.getGameState()) {
        startB.setEnabled(false);
        pauseB.setEnabled(true);
        restartB.setEnabled(true);
        if(gamePanel.getApple().getAte())
          score++;
      }
      
      if(!gamePanel.getSnake().isAlive()) {
        statusBarTF.setBackground (Color.RED);
        statusBarTF.setText ("G A M E   O V E R !!!");
        scoreTF.setText(Integer.toString(getScore()));
        statusBarTF.setEnabled(false);
        scoreTF.setEnabled(false);
      }
    }

    if(e.getSource() == pauseB) {
      gamePanel.pauseGame();
    }
    
    if(e.getSource() == exitB) {
      gamePanel.exitGame();
    }
    
    if(e.getSource() == restartB) {
      gamePanel.restartGame();
    }

    mainPanel.requestFocus();   
  }

  
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
      

    if(keyCode == KeyEvent.VK_LEFT) {
      gamePanel.updateGame(1);
      gamePanel.renderGame();
    }

    if(keyCode == KeyEvent.VK_RIGHT) {
      gamePanel.updateGame(2);
      gamePanel.renderGame();
    }

    if(keyCode == KeyEvent.VK_UP) {
      gamePanel.updateGame(3);
      gamePanel.renderGame();
    }

    if(keyCode == KeyEvent.VK_DOWN)  {
      gamePanel.updateGame(4);
      gamePanel.renderGame();
    }
  
  }
  
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}



  public void mouseClicked(MouseEvent e) {

    int x = e.getX();
    int y = e.getY();
  }


  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }

  public void mousePressed(MouseEvent e) {

  }

  public void mouseReleased(MouseEvent e) {

  }


 
  public void resetLives() {
    this.lives =0;
  }

  public void resetScore() {
    GameWindow.score = 0;
  }

  public int getScore() {
    return GameWindow.score; 
  }

  public int getHighScore() {
    return this.highScore;
  }

  public int getLives() {
    return this.lives;
  }

}