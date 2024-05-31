import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.util.Random;
import java.awt.Image;
import java.awt.image.BufferedImage;



public class Apple {

  private JPanel panel;
  private final static int UNIT_SIZE = 25;  // game unit size i.e max size
  private int x, y, w, h;  // default width and height 
  private Image appleImage;
  Ellipse2D.Double apple;	    
  private Color backgroundColour;
  private Dimension dimension;
  private boolean isEaten;     // simulation states
  private Bug bug;    // reference to ship object
  private SoundManager soundEffects;
  BufferedImage image;

  
  public Apple (JPanel p, Bug b) {
    panel = p;
    dimension = panel.getSize();
    backgroundColour = panel.getBackground();
    isEaten = false;
    x = generateRandomInteger(0, dimension.width - w);
    y = generateRandomInteger(0, dimension.height - h);
    w = 14;
    h = 17;
    bug = b;
    image =  new BufferedImage (w, h, BufferedImage.TYPE_INT_RGB);
    appleImage = ImageManager.loadImage("images/apple.png");
    soundEffects = SoundManager.getInstance();
  }

  
  public int generateRandomInteger(int min, int max) {  // Generates a random integer between specified range
    Random random = new Random();
    return random.nextInt(max) + min;
  }

  
  public void draw() { // draws 
    Graphics g = panel.getGraphics();
    Graphics2D g2 = (Graphics2D) g;
    Graphics2D imageContext = (Graphics2D) image.getGraphics();
    imageContext.drawImage(appleImage, 0, 0, null);
    g2.drawImage(appleImage, x, y, w, h, null);
    g2.dispose();
  }

  
  public void erase () { // erases asteroid by drawing over it with the background colour
    Graphics g = panel.getGraphics ();
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(backgroundColour);
    g2.fill(new Rectangle2D.Double(x,y,w,h));
    g2.dispose();
  }


  public void setLocation() { 
    if (!panel.isVisible() || isEaten) 
      return;
    
   
    x = generateRandomInteger(0, dimension.width - UNIT_SIZE)*UNIT_SIZE;
    y = generateRandomInteger(0, dimension.height - UNIT_SIZE)*UNIT_SIZE;
   /* boolean collision = collidesWithBug(); 

    if (collision) 
      setLocation();	// resets object location  */

     setEaten();
   }
  
  public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (x, y, w, h);
   }


  public boolean collidesWithBug() {  // checks collision 
    Rectangle2D.Double myRect = getBoundingRectangle();
    Rectangle2D.Double bugRect = bug.getBoundingRectangle();
    if(myRect.intersects(bugRect) && bug.isAlive()) {
      bug.eat();
      erase();
      setEaten();
      soundEffects.playClip("ate", false);
     
      return true;
    }
    return false;
  }

  
  // Getters & Setters

  public int getX() {
    return x; 
  }

  public int getY() {
    return y; 
  }
  
  public int getWidth() {
    return w; 
  }

  public int getHeight() {
    return h; 
  }

  public int getUnitSize() {
    return UNIT_SIZE; 
  }
  
  public boolean getAte() { 
    return isEaten; 
  }

  public void setEaten() { 
    if(isEaten)
      isEaten = false;
    isEaten = true;
  }

  public Apple getApple() {
    return this;
  }
  
}