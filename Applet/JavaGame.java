import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.applet.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Observable; 
import java.util.Observer;
import java.net.*; 
import javax.sound.sampled.FloatControl;

//                           Interfaces
  

public class JavaGame extends Applet implements KeyListener {
  
  // Anfang Attribute
  File appletpfad = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
  boolean notrunning = true;
  URL  PlayerTextureUrl;
  File texture = new File("K:\\test.png");
  //File texture = new File(appletpfad, "\texture\test.png");
  File sound = new File("K:\\sound.wav");
  File shottexture = new File("K:\\shot.png");
  Player player[] = new Player[4];
  Image dbImage;
  Graphics dbGraphics;
  damageLogig DamageLogig;
  int[][] ebenen = new int[100][3] ;
  GameRunner gamerunner;
  float vol;
  AudioClip ac;
  //FloatControl volume;
  // Ende Attribute
  
  
  //Erzaehler erz = new Erzaehler(player1, player2);
  
  public void init() {
    try { 
      PlayerTextureUrl = sound.toURI().toURL();
    } catch(MalformedURLException urlexception) {
      
    } 
    
    ac = getAudioClip(PlayerTextureUrl);
    ac.loop();
    
    //volume = (FloatControl) ac.getControl(FloatControl.Type.MASTER_GAIN);
    //vol = volume.getValue();
    
    dbImage = createImage(1920,1080);
    dbGraphics = dbImage.getGraphics();
    
    ebenen[1][0]= 200;  //x1
    ebenen[1][1]= 400;  //x2
    ebenen[1][2]= 480;  //y
    
    ebenen[0][0]= 100-67;
    ebenen[0][1]= 1000;       // Main Ebene: Kann nicht durchschrittenwerden indem down gedr�ckt wird
    ebenen[0][2]= 600;
    
    ebenen[2][0]= 600;
    ebenen[2][1]= 800;
    ebenen[2][2]= 480;
    
    ebenen[3][0]= 400;
    ebenen[3][1]= 600;
    ebenen[3][2]= 380;
    
    
    player[1] = new Player(texture,shottexture,dbImage,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_ENTER,10,10,"Justus");                // I'm in Space! SPACE!
    player[2] = new Player(texture,shottexture,dbImage,KeyEvent.VK_A,KeyEvent.VK_D,KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_Q,120,10,"Christian");
    player[3] = new Player(texture,shottexture,dbImage,KeyEvent.VK_J,KeyEvent.VK_L,KeyEvent.VK_I,KeyEvent.VK_K,KeyEvent.VK_U,230,10,"Bent der Lutscher");
    
    
    
    
    try {
      PlayerTextureUrl = new URL("http://www.mariowiki.com/images/5/57/WaluigiMP8Official.png");
    } catch(Exception e) {
      e.printStackTrace();
    } 
  } // end of init
  
  
  
  public void keyPressed(KeyEvent e) 
  {
    if (e.getKeyCode()==KeyEvent.VK_ESCAPE && gamerunner.running) {
      Graphics gr = this.getGraphics();
      gr.setFont(new Font("TimesRoman", Font.PLAIN, 40)); 
      gr.drawString("PAUSE", (int) this.getWidth()/2, this.getHeight()/2);
      gamerunner.running=false;
      ac.stop();
      //volume.setValue(vol);
    }
    
    else if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {
      gamerunner.running=true;
      ac.loop();
      //volume.setValue(vol-0.5);
    } // end of if-else
  }
  
  public void keyReleased(KeyEvent e) 
  {
    
  }
  
  public void keyTyped(KeyEvent e)
  {
    
  }   
  
  public void paint (Graphics g) {
    
    if (notrunning) {
      player[1].laden(this,dbGraphics,g,100,500);
      player[2].laden(this,dbGraphics,g,200,500);
      player[3].laden(this,dbGraphics,g,300,500);
      this.addKeyListener(player[1]);
      this.addKeyListener(player[2]);
      this.addKeyListener(player[3]);
      this.addKeyListener(this);
      
      
      gamerunner = new GameRunner(player,this);
      DamageLogig = new damageLogig (gamerunner);
      
      notrunning = false;
    } // end of if
    
  }  
  
  public void update(Graphics g) {
    
    paint(g);
    
  }
  // Ende Methoden
} // end of class JavaGame



class Player extends Thread implements KeyListener  {
  
  // Anfang Attribute1
  File playertexture,shottexture;
  BufferedImage textureImage, textureImageb = new BufferedImage(1000,1000,1);
  
  Graphics g,db;
  KeyEvent taste;
  JavaGame Game;
  Image dbImage;
  boolean justupdated,characterInverted,firsttimepressed,freezeControls=false;
  int x,y=0;
  int stehenzahl = -1;                                                                                                    
  int schusssperre,left,right,jump,down,attack,xHealth,yHealth;                                                                                             
  int jumpup, jumpdown = 0;                                                                                                   
  boolean[] keys = new boolean[1000];
  int health = 100;
  String name;
  int jumpheigth = 200;
  
  // Ende Attribute1
  
  public Player(File playertexture, File shottexture, Image dbImage, int left, int right , int jump, int down, int attack, int xHealth, int yHealth, String name) {
    this.playertexture = playertexture;
    this.shottexture = shottexture;
    this.dbImage = dbImage;
    this.left = left;
    this.right = right;
    this.jump = jump;
    this.attack = attack;
    this.xHealth = xHealth;
    this.yHealth = yHealth;
    this.down = down;
    this.name = name;
  }  
  // Anfang Komponenten1
  // Ende Komponenten1
  // Anfang Methoden1
  
  public void laden(JavaGame Game, Graphics db, Graphics g, int x, int y) {
    
    this.Game = Game;
    this.g = g;
    this.db = db;
    this.x = x;
    this.y = y;
    
    for (int c=0;c<keys.length;c++ ) {
      keys[c]=false;
    } // end of for
    
    try { 
      textureImage = ImageIO.read(playertexture);
    } catch(IOException exeption) {
      
    }
    
    textureImageb = verticalflip(textureImage);
  }
  
  public BufferedImage verticalflip(BufferedImage img) {
    int w = img.getWidth();
    int h = img.getHeight();
    BufferedImage dimg = new BufferedImage(w, h, img.getType());
    Graphics2D g = dimg.createGraphics();
    /**
    * img - the specified image to be drawn. This method does nothing if
    * img is null. dx1 - the x coordinate of the first corner of the
    * destination rectangle. dy1 - the y coordinate of the first corner of
    * the destination rectangle. dx2 - the x coordinate of the second
    * corner of the destination rectangle. dy2 - the y coordinate of the
    * second corner of the destination rectangle. sx1 - the x coordinate of
    * the first corner of the source rectangle. sy1 - the y coordinate of
    * the first corner of the source rectangle. sx2 - the x coordinate of
    * the second corner of the source rectangle. sy2 - the y coordinate of
    * the second corner of the source rectangle. observer - object to be
    * notified as more of the image is scaled and converted.
    *
    */
    g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
    g.dispose();
    return dimg;
  }
  
  
  
  public void updateKey()
  {
    if (firsttimepressed && !freezeControls) {
      
      if (keys[right] ) {                              
        x +=5;
        characterInverted = false;
      } // end of if
      
      if (keys[left] ) {                                 
        x -=5;
        characterInverted = true; 
      } // end of if
      
      if (keys[jump] ) {                                 
        setJump(jumpheigth);
      } // end of if
      
      if (keys[down]  && stehenzahl != 0 ) {                  // Stehenzahl 0: Player steht auf der Mainebene               
        y -=1;
        justupdated = true;
      } // end of if
      
      if (keys[attack] && y<900 && schusssperre == 0) {                             
        Shot bullet = new Shot(shottexture,!characterInverted, 10, Game, this);
        bullet.laden(x,y+50);
        schusssperre = 40;
      } // end of if
    } // end of if  
    
    
    if (y + textureImage.getHeight() > Game.ebenen[0][2] + 200) {
      health=0;
    } // end of if
    updateJump(10);
    drawHealth();
    if (schusssperre > 0) {
      schusssperre--;
    } // end of if
    
    
    if (!characterInverted && health>0) {
      Game.dbImage.getGraphics().drawString(name,x+33,y-10);
      Game.dbImage.getGraphics().drawImage(textureImage,x,y,Game);
    } // end of if
    else if (characterInverted && health>0){
      Game.dbImage.getGraphics().drawString(name,x+33,y-10);
      Game.dbImage.getGraphics().drawImage(textureImageb,x,y,Game);
    } // end of if-else
    
    
  }
  
  public void move(boolean rechts, int amount)  {
    
    
  }  
  
  public void drawHealth() {
    Graphics gra = Game.dbImage.getGraphics();
    gra.drawLine(xHealth,yHealth,xHealth+100,yHealth);
    gra.drawLine(xHealth,yHealth+10,xHealth+100,yHealth+10);
    gra.setColor(Color.red);
    gra.fillRect(xHealth+1,yHealth,health,10);
    gra.setColor(Color.black);
    gra.drawString(name,xHealth,yHealth+30);
  }  
  
  public void run() {
  }
  
  public void setDamage(int damage) {
    health -= damage;
    if (health < 0) {
      freezeControls = true;
    } // end of if
  }  
  
  public void keyPressed(KeyEvent e) 
  {
    firsttimepressed = true;
    this.taste = e;
    keys[taste.getKeyCode()]=true;   
  }
  
  public void keyReleased(KeyEvent e) 
  {
    
    this.taste = e;
    keys[taste.getKeyCode()]=false;   
  }
  
  public void keyTyped(KeyEvent e) 
  {
    
  }
  
  // PlayerLogig
  
  public void setJump(int heigth) {
    if (jumpup == 0 && jumpdown == 0) {
      jumpup += heigth;
      jumpdown += heigth;
      justupdated = true;
    } // end of if
    
  } 
  
  public void updateJump(int speed) {
    boolean ebene = false;
    boolean kannstehen = false;
    int zahl = -1;
    stehenzahl = -1;
    int height = textureImage.getHeight();
    for (int counter=0;counter < Game.ebenen.length;counter++ ) {
      if (Game.ebenen[counter] != null ) {
        if ( (y-speed+height)<=Game.ebenen[counter][2] && y+height>=Game.ebenen[counter][2]) {  
          if (x >= Game.ebenen[counter][0] && x <= Game.ebenen[counter][1]) {
            ebene = true;
            zahl = counter;
          }
        }
        
        if (y+height==Game.ebenen[counter][2]) {  
          if (x >= Game.ebenen[counter][0] && x <= Game.ebenen[counter][1]) {
            kannstehen = true;
            stehenzahl = counter;
          }
        }
      }
    } // end of for
    
    if (ebene && !justupdated) {
      jumpdown = 0; 
    } // end of if
    
    if (ebene && !justupdated && jumpup<=0) {          // Beim losspringen soll nichts erkannt werden
      y = Game.ebenen[zahl][2]-height;
      jumpup = 0;
      
    }
    
    else if (jumpup>0) {
      y -= speed;
      jumpup -= speed;
    } // end of if
    
    else if (!kannstehen) {
      y += speed;
    } // end of if-else
    
    justupdated = false; //Setjump ist mindestens ein update her
  }   
  // Ende Methoden1
}

class Shot {
  
  // Anfang Attribute2
  int x,y,speed;
  JavaGame Game;
  File shottexture;
  Player owner;
  boolean rechts;
  BufferedImage textureImage = new BufferedImage(1000,1000,1);
  // Ende Attribute2
  
  
  public Shot (File shottexture, boolean rechts, int speed, JavaGame Game, Player owner) {
    this.shottexture = shottexture;
    this.Game = Game;
    this.speed = speed;
    this.rechts = rechts;
    this.owner = owner;
  }  
  
  public void laden(int x, int y) {
    
    this.x = x;
    this.y = y;
    
    try { 
      textureImage = ImageIO.read(shottexture);
    } catch(IOException exeption) {
      
    }
    
    if (rechts) {
      textureImage = verticalflip(textureImage);
    } // end of if
    Game.DamageLogig.registerShot(this);            //Registrierung
  }
  
  public void updateShot() {
    if (rechts) {
      x += speed;
      
    } // end of if
    else {
      x -= speed;
      
    } // end of if-else
    
    Game.dbImage.getGraphics().drawImage(textureImage,x,y,Game);
    
  }
  
  public BufferedImage verticalflip(BufferedImage img) {
    int w = img.getWidth();
    int h = img.getHeight();
    BufferedImage dimg = new BufferedImage(w, h, img.getType());
    Graphics2D g = dimg.createGraphics();
    g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
    g.dispose();
    return dimg;
  }
  
  // Ende Methoden2
}

class damageLogig {
  // Anfang Attribute3
  Shot shot[] = new Shot[1000];
  GameRunner runner;
  int counter = 0;
  int xDistance,yDistance;
  
  public damageLogig(GameRunner runner) {
    this.runner = runner;
    
  }  
  
  public void registerShot(Shot bullet) {
    shot[counter] = bullet;
    runner.shot[counter] = bullet;
    
    runner.isthereshot = true;
    counter++;
  }
  
  public void updateDamage() {  
    
    for (int counter=1;counter < runner.player.length;counter++ ) {
      for (int counterb = 0;counterb < runner.shot.length;counterb++) {
        if (runner.shot[counterb] != null && runner.shot[counterb].owner != runner.player[counter] && !runner.player[counter].freezeControls) {
          
          xDistance = runner.shot[counterb].x - runner.player[counter].x;
          yDistance = runner.shot[counterb].y - runner.player[counter].y ;
          
          if ( (xDistance > -50 && xDistance <67) && (yDistance > -50 && yDistance < 100) ) {
            runner.player[counter].setDamage(10);
            runner.shot[counterb]=null;
          } // end of if
          
        } // end of if
      } // end of for
    } // end of for
    
  }  
  // Ende Methoden3
}  
  
/////////////////////////////////  Observable Classe f�r die �bergabe       

class Erzaehler extends Observable { 
  // Anfang Attribute4
  // Ende Attribute4
  
  public Erzaehler(Player p1, Player p2){ 
    //this.addObserver(p1);
    //this.addObserver(p2); 
    
    
  } 
  // Anfang Komponenten4
  // Ende Komponenten4
  // Anfang Methoden4
  
  
  public void tell(KeyEvent info){ 
    if(countObservers()>0){ 
      setChanged(); 
      notifyObservers(info); 
    } 
  } 
  // Ende Methoden4
  
  
} 

class perks extends Thread 
{
  int x,y,wert,perkx,perky;
  File perktexture;
  BufferedImage textureImage;
  boolean rechts;
  boolean active=true;
  int speed;
  JavaGame Game;
  
  public perks (File perktexture, boolean rechts, int speed, JavaGame Game, int perkx, int perky) {
    this.perktexture = perktexture;
    this.Game = Game;
    this.speed = speed;
    this.rechts = rechts;
    this.x = x;
    this.y = y;
    this.perkx=perkx;
    this.perky=perky;
    
    laden();
  } 
  public void laden() {
    try { 
      textureImage = ImageIO.read(perktexture);
    } catch(IOException exeption) {
      
    }
  }
  
  public void update() {
    
    for (int counter = 0;counter<Game.player.length;counter++) {
      if (Game.player[counter] != null) {
        
        int xDistance = perkx - Game.player[counter].x;
        int yDistance = perky - Game.player[counter].y;
        
        
        if ( (xDistance > -50 && xDistance <67) && (yDistance > -50 && yDistance < 100)) {
          active=false;
          int perkw = (int) (Math.random()*3+1);
          switch (perkw) {
            case  1:
            Game.player[counter].jumpheigth=300;
            break;
            case  2:
            Game.player[counter].jumpheigth=200;
            break;
            case 3: 
            Game.player[counter].jumpheigth=150;
            break;
            
          }
          
        } 
        Game.dbImage.getGraphics().drawImage(textureImage,perkx,perky,Game);
      } // end of if
    } // end of if
  } // end of for
}



////// Standart Thread f�r das aktualisieren aller Komponenten

class GameRunner extends Thread {
  // Anfang Attribute5
  Player player[] = null;
  Shot shot[] = new Shot[1000];
  JavaGame Game ;
  boolean isthereshot = false;
  boolean running = true;
  File perktexture = new File("K:\\perk.png");
  perks perk[] = new perks[100000];
  int count=0;
  // Ende Attribute5
  
  public GameRunner (Player[] player, JavaGame Game) {
    this.player = player;
    this.Game = Game;
    this.start();
  }  
  // Anfang Komponenten5
  // Ende Komponenten5
  // Anfang Methoden5
  
  public void run() {
    while (true) {
      synchronized(getClass()) { 
        try {
          sleep(33);
        }
        catch(InterruptedException e) {
        }
        if (running) {
          Game.repaint();
          Game.dbImage.getGraphics().clearRect(0,0, (int)Game.getWidth(), (int)Game.getHeight());
          
          int perkjn= (int) (Math.random()*3000+1);
          if (perkjn<20) {
            int perkx= (int) (Math.random()*1000+1);
            int perky= (int) (Math.random()*400+100);
            perk[count] = new perks(perktexture, false, 10, Game, perkx, perky); 
            count++;
            
          } // end of if
          
          for (int counter=0;counter<perk.length;counter++)
          { 
            if (perk[counter] != null) {
              perk[counter].update();
              if (!perk[counter].active) {
                perk[counter]=null;  
              } // end of if
              
            } // end of if
          }
          
          for (int counter=1;counter<player.length;counter++)
          { 
            
            player[counter].updateKey(); 
            
          }
          if (isthereshot) {
            for (int counter=0;counter<shot.length;counter++)
            { 
              if (shot[counter] != null) {
                shot[counter].updateShot();  
              } // end of if
            }
            Game.DamageLogig.updateDamage();
          } // end of if
          Game.dbImage.getGraphics().fillRect(100,600,900,10);
          Game.dbImage.getGraphics().drawString("Music: Early Riser Kevin MacLeod (incompetech.com)", Game.getWidth()-320, Game.getHeight()-20);
          
          for (int c = 1;c<Game.ebenen.length;c++) {
            if (Game.ebenen[c] != null) {
              Game.dbImage.getGraphics().drawLine(Game.ebenen[c][0],Game.ebenen[c][2],Game.ebenen[c][1],Game.ebenen[c][2]);
            } // end of if
          } // end of for
          
          Game.getGraphics().drawImage(Game.dbImage,0,0,Game);
        }
        
        
        
      } // end of if
      
    } // end of while
    
  }  
  // Ende Methoden5
}  
  
  
