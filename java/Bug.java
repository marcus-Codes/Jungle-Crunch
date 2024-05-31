import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.geom.Rectangle2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class Bug  {
  private JPanel panel;
  private Color backgroundColour;
  private Dimension dimension;
  private final int UNIT_SIZE = 10;
  private final int width, height;
  private static int dx, dy, x, y;
  private int ate;
  private boolean alive;
  private SoundManager soundEffects;
  private Image bugImage;
  BufferedImage image;


  public Bug (JPanel p, int x, int y) {
    panel = p;
    dimension = panel.getSize();
    backgroundColour = panel.getBackground();
    Bug.x = x;
    Bug.y = y;
    Bug.dx = UNIT_SIZE;
    Bug.dy = UNIT_SIZE;
    ate = 0;
    width = 68;
    height = 64;
    alive = true;
    soundEffects = SoundManager.getInstance();
    image =  new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
    bugImage = ImageManager.loadImage("images/bug.png");
  }


  public void draw() { // draws 
    Graphics g = panel.getGraphics();
    Graphics2D g2 = (Graphics2D) g;
    Graphics2D imageContext = (Graphics2D) image.getGraphics();
    imageContext.drawImage(bugImage, 0, 0, null);
    g2.drawImage(bugImage, x, y, width, height, null);
    g2.dispose();
  }

  
  public void erase() { // erases
    Graphics g = panel.getGraphics ();
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(backgroundColour);
    g2.fill(new Rectangle2D.Double (x, y, width, height));
    g2.dispose();
  }


  public void move(int direction) {  // moves 
    if(!panel.isVisible()) return;
    dimension = panel.getSize();

    if (direction == 1) {	// move left
      if(x - dx < 0) 
        x = 0;
      
      else
        x -= dx;
    }

    else if (direction == 2) {  	// move right
      if ((x + width)  + dx > dimension.width)  // wrap around screen from right side
        x = dimension.width - width;
      
      else
      x += dx;
    }

    else if(direction == 3) { // move up
      if(y - dy < 0)
        y = 0;  

      else
        y -= dy;
    }

    else if(direction == 4) { // move down
      if((y + height) + dy > dimension.height) 
        y = dimension.height - height; 
      
      else
        y += dy;
    }
      
    else
      return;
  }
  
  
  public boolean wallCollison() {
    if(x < 0 || x > dimension.width) {  // left and right wall collision
      soundEffects.playClip("wallHit",false);
      return true;
    }

    if(y < 0 || y > dimension.height) {   // top and bottom wall collision
      soundEffects.playClip("wallHit",false);
      return true;
    }
    
    return false;
  }


  public Rectangle2D.Double getBoundingRectangle() {
    return new Rectangle2D.Double (x, y, width, height);
  }

  public void ripBug() {
    if(alive){ 
      alive = false;
      soundEffects.playClip("death", false);
    }
  }

  public boolean isAlive() {
    return alive;
  }

  public void eat() {
    soundEffects.playClip("eat",false);
    ate++;
  }

  public int getAte() {
    return ate;
  }

  public Bug getBug() {
    return this;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
  
}
