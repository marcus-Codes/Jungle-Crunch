import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Image;



public class GamePanel extends JPanel implements Runnable {
  private final int WIDTH = 453, HEIGHT = 450;
  private static final int UNIT_SIZE = 25;
  private final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
  private boolean isRunning,isPaused;
  private Thread gameThread;
  Apple apple;
  Bug bug;
  private Image background, flowerImage;
  Dimension dimension = this.getSize();
  Rectangle2D.Double bugRect,appleRect;  // Rectangles acting as boundaries for collisions
  BufferedImage image;
  SoundManager soundEffects;
  int pixel, pixels[], red, blue, green, gray;
  
  
  public GamePanel () {
    apple = null; 
    bug = null;
    gameThread = new Thread(this);
    isRunning = false;
    isPaused = false;
    image =  new BufferedImage (600, 425, BufferedImage.TYPE_INT_RGB);
    soundEffects = SoundManager.getInstance();
  }

  public void createGameEntities() {
    bug = new Bug(this, (int) (WIDTH / 2), (int) (HEIGHT / 2));
    bugRect =  bug.getBoundingRectangle();
    apple = new Apple(this, bug); 
    appleRect = apple.getBoundingRectangle();
    background = ImageManager.loadImage("images/background.jpg");
    flowerImage = ImageManager.loadImage("images/flower.png");
  }

  public void renderGame() {
  
    Graphics2D imageContext = (Graphics2D) this.getGraphics();
    imageContext.drawImage(background, 0, 0, null);
    
    Graphics g = this.getGraphics();
    Graphics2D g2 = (Graphics2D) g;
    
    if(apple == null || bug == null) return;
    apple.draw();
    bug.draw(); 
    g2.setColor(Color.WHITE);
    g2.setFont(new Font("Retro Gaming", Font.BOLD, 20));
    g2.drawString("Score: "+bug.getAte(), (WIDTH-110) ,20);
    g2.dispose(); 
  }

  public void updateGame(int direction) {  // updates game objects and states
    if (bug == null ) return;  
    bug.erase();
    bug.move(direction);
  }

  public void startGame() {
    if(bug == null) return;
    gameThread.start();
    soundEffects.playClip("background", true);
  }

  public void pauseGame() {
    if(isPaused) {
      isPaused = false;
      return;
    }
    isPaused = true;
  }

  public void exitGame() {
    System.exit(0);
  }

  public void restartGame() { 
    isPaused = false;
    if (gameThread == null || !isRunning) {
      soundEffects.playClip("background", true);
      createGameEntities();
      renderGame();
      gameThread = new Thread (this);
      gameThread.start();
      }
  }

  public void endGame() { // end the game thread
    if (isRunning) {
      isRunning = false;
      soundEffects.stopClip ("background");
    }
  }
 
  public void run() {  
    isRunning = true;

    try { 
      while (isRunning) {
        if(!isPaused) {
          renderGame();
          checkCollision();

          if(!bug.isAlive()) {
            Thread.sleep(550);
            endGame();
          }

          if(checkFoodCollision()) {
            if(apple.getAte()) {
              apple.setLocation();
              //apple = new Apple(this, bug); 
            }

          }

          
        }
       
        Thread.sleep(80); // increase to slow down and vice-versa   
      }
     

    }
    catch (InterruptedException e) {
        e.printStackTrace();  // error tracing exception i.e shows what went wrong and where (method)
    }

  }

  public void gameOver() {
    Graphics g = this.getGraphics();
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.RED);
    g2.setFont(new Font("Charcuterie", Font.BOLD, 20));
    g2.drawString("GAME OVER", WIDTH / 2 - 50, HEIGHT / 2);
    g2.dispose();
  }

  public void checkCollision() {
    if(bug.wallCollison()) {
      bug.ripBug();
      if(!bug.isAlive()) 
        gameOver();
    }
    return;
  }

  public boolean checkFoodCollision() {
    if(apple.collidesWithBug() && bug.isAlive()) {  
      if(apple.getAte()) {
        apple = new Apple(this, bug);
      }
      return true;
    }
    return false;
  }

  public void checkApple() {
    if((bug.getX() == apple.getX()) && (bug.getY() == apple.getY())) {
      bug.eat(); 
    }
    return;
  }

  public void getPixels() {
    for(int i = 0; i < GAME_UNITS; i++) {
      for(int j = 0; j < GAME_UNITS; j++) {
        pixel = image.getRGB(i, j);
        red = (pixel >> 16) & 0xFF;
        green = (pixel >> 8) & 0xFF;
        blue = pixel & 0xFF;
        gray = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
        
        if(gray > 0) 
          System.out.println("Pixel at (" + i + ", " + j + ") is not white");
        
      }
    }
  }

  public void getAllPixels() {
    int imWidth = image.getWidth();
    int imHeight = image.getHeight();
    int [] pixels = new int[imWidth * imHeight];
    image.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
  }


     
  // Getters and Setters

  public Apple getApple() {
    return apple;
  }

  public Bug getSnake() {
    return bug;
  }

  public int getWidth() {
    return WIDTH;
  }

  public int getHeight() {
    return HEIGHT;
  }

  public int getUnits() {
    return GAME_UNITS;
  }

  public boolean getGameState() {
    return isRunning;
  }

}