import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 17.06.2014
  * @author 
  */
//                           Interfaces

//interface Observer
//{ 
//  public void update(KeyEvent keyEvent);
//}
//
//interface Observable
//{
//  public void NotifyObservers(KeyEvent keyEvent);
//}
  
  

public class JavaGame extends Applet implements KeyListener {
  
  String appletpfad = (System.getProperty("user.dir")+"\texture\test.png");
  //private ArrayList<Observer> obsList;
  //File texture = new File(appletpfad); 
  File texture = new File("Z:\\test.png");
  Player player1 = new Player(texture,texture);
  
  //  public JavaGame()
  //  {
  //    obsList = new ArrayList();
  //  }
  
  public void keyPressed(KeyEvent e) 
  {
    
  }
  
  public void keyReleased(KeyEvent e) 
  {
    
  }
  
  public void keyTyped(KeyEvent e) 
  {
    
  }
  
  //  public void NotifyObservers(KeyEvent keyEvent)
  //  {
  //    for(Observer obs : obsList)
  //    {
  //      obs.update(keyEvent);
  //    }
  //  }
  
  //  public void AddObserver(Observer obs)
  //  {
  //    if (obs != null)
  //    obsList.add(obs);
  //  }
  //  
  //  public void DelObserver(Observer obs)
  //  {
  //    if (obs != null)
  //    obsList.remove(obs);
  //  }
  
  public void init() {
    
    
    
  } // end of init
  
  public void paint (Graphics g) {
    player1.laden(this,g);
    this.addKeyListener(player1);
    
  }  
  
} // end of class JavaGame

class Player extends Thread implements KeyListener  {
  
  File playertexture,shottexture;
  BufferedImage textureImage = new BufferedImage(1000,1000,1);
  Graphics g;
  
  public Player(File playertexture, File shottexture) {
    this.playertexture = playertexture;
    this.shottexture = shottexture;
    //this.g = g;
    JavaGame observer = new JavaGame();
    //observer.AddObserver(this);
  }  
  
  public void laden(JavaGame Game,Graphics g) {
    
    try { 
      textureImage = ImageIO.read(playertexture);
    } catch(IOException exeption) {
      
    }
    
  }
  
  //  public void update(KeyEvent keyEvent)
  //  {
  //    switch (keyEvent.getAction()) {
  //      case KEY_PRESSED: keyPressed(keyEvent.getCharacter()); break;
  //      
  //      case KEY_RELEASED: keyReleased(keyEvent.getCharacter()); break;
  //      
  //      case KEY_TYPED: keyTyped(keyEvent.getCharacter()); break;
  //      
  //    }
  //  }
  
  public void keyPressed(KeyEvent e) 
  {
    
  }
  
  public void keyReleased(KeyEvent e) 
  {
    
  }
  
  public void keyTyped(KeyEvent e) 
  {
    
  }
  
}
