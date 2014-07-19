package Applet;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Observable; 
import java.util.Observer;
import java.net.*; 
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import Applet.*;

class Menu extends Frame implements ActionListener,ItemListener,KeyListener {
  
  JavaGame Game;
  Button[] buttonSpieler = new Button[100] ;
  Button[] buttonSchuss = new Button[100] ;
  Button Sound,Restart,Fps,Bot;
  Choice SpielerAuswahl;
  TextField Name,maxFps,perks,Spieler;
  Label L1;
  public Menu (JavaGame Game) 
  {
    this.Game = Game;
    this.setLayout(null);
    this.addKeyListener(this);
    setTitle("Menu");  // Fenstertitel setzen
    setSize(Game.getWidth(),Game.getHeight());                            // Fenstergr��e einstellen  
    addWindowListener(new TestWindowListener());                        // EventListener f�r das Fenster hinzuf�gen
    setLocationRelativeTo(null);
    setVisible(true);                                                   // Fenster (inkl. Inhalt) sichtbar machen
    
    for (int c=0;c<Game.texture.length;c++) {
      if (Game.texture[c] != null) {
        buttonSpieler[c] = new Button("Ausw�hlen");
        buttonSpieler[c].setBounds(120+71*c,250,67,20);
        buttonSpieler[c].addActionListener(this);
        buttonSpieler[c].addKeyListener(this);
        this.add(buttonSpieler[c]);
      } // end of if
    } // end of for
    
    for (int c=0;c<Game.shottexture.length;c++) {
      if (Game.shottexture[c] != null) {
        buttonSchuss[c] = new Button("Ausw�hlen");
        buttonSchuss[c].setBounds(120+71*c,330,67,20);
        buttonSchuss[c].addActionListener(this);
        buttonSchuss[c].addKeyListener(this);
        this.add(buttonSchuss[c]);
      }  
    } // end of for 
    
    SpielerAuswahl = new Choice();
    SpielerAuswahl.addKeyListener(this);
    SpielerAuswahl.setBounds(120,50,300,30);
    
    Name = new TextField(Game.player[1].name);
    Name.setBounds(120,90,300,20);
    Name.addKeyListener(this);
    this.add(Name);
    
    perks = new TextField(Game.gamerunner.auftretenvonperks);
    perks.setBounds(210,460,30,20);
    perks.addKeyListener(this);
    this.add(perks);
    
    L1=new Label("Perkh�ufigkeit:");
    L1.setBounds(120,460,90,20);
    L1.addKeyListener(this);
    this.add(L1);
    
    if (Game.soundan) {
      Sound = new Button("Sound aus");
    } // end of if
    else {
      Sound = new Button("Sound an");
    } // end of if-else
    Sound.setBounds(120,370,100,20);
    Sound.addActionListener(this);
    Sound.addKeyListener(this);
    this.add(Sound);
    
    if (Game.fpsan) {
      Fps = new Button("FPS ausblenden");
    } // end of if
    else {
      Fps = new Button("FPS anzeigen");
    } // end of if-else
    Fps.setBounds(120,400,100,20);
    Fps.addActionListener(this);
    Fps.addKeyListener(this);
    this.add(Fps);
    
    
    Bot = new Button("Bot an");
    Bot.setBounds(490,460,100,20);
    Bot.addActionListener(this);
    Bot.addKeyListener(this);
    this.add(Bot);
    
    int spielerAnzahl=0;
    for (int c=1;c<Game.player.length;c++) {
      if (Game.player[c] != null) {
        spielerAnzahl++;
      } // end of if
    } // end of for
    
    Spieler = new TextField(spielerAnzahl+"");
    Spieler.setBounds(490,400,100,20);
    Spieler.addKeyListener(this);
    this.add(Spieler);
    
    Restart = new Button("Restart");
    Restart.setBounds(120,430,100,20);
    Restart.addActionListener(this);
    Restart.addKeyListener(this);
    this.add(Restart);
    
    maxFps = new TextField(Game.gamerunner.maxFPS+"");
    maxFps.setBounds(230,400,100,20);
    maxFps.addKeyListener(this);
    this.add(maxFps);
    
    for (int c=1;c<Game.player.length;c++) {
      SpielerAuswahl.addItem("Spieler "+c);
    } // end of for
    SpielerAuswahl.addItemListener(this);
    
    this.add(SpielerAuswahl);
  }
  
  
  public void itemStateChanged(ItemEvent ie)
  {
    Name.setText(Game.player[SpielerAuswahl.getSelectedIndex()+1].name);
  }
  
  public void keyReleased(KeyEvent e) 
  {
    
  }
  
  public void keyTyped(KeyEvent e)
  {
    
  }   
  
  public void keyPressed(KeyEvent e)
  {
    Game.player[SpielerAuswahl.getSelectedIndex()+1].name=Name.getText();
    
    
    if (isNumeric(perks.getText())) {
      if (Integer.parseInt(perks.getText())<=1000 && Integer.parseInt(perks.getText())>0) {
        Game.gamerunner.auftretenvonperks = Integer.parseInt(perks.getText());
      } // end of if
      
    } // end of if
    
    if (isNumeric(Spieler.getText())) {
      int anzahl = Integer.parseInt(Spieler.getText());
      int textureAnzahl=0;
      int shottextureAnzahl=0;
      for (int cou=0;cou<Game.texture.length;cou++) {
        if (Game.texture[cou] != null) {
          textureAnzahl++;
        } // end of if
      } // end of for
      for (int cou=0;cou<Game.shottexture.length;cou++) {
        if (Game.shottexture[cou] != null) {
          shottextureAnzahl++;
        } // end of if
      } // end of for
      
      for (int c=1;c<Game.player.length;c++) {
        if (c>anzahl) {
          
          Game.player[c] = null;
          
        } // end of if
        else {
          if (Game.player[c] == null) {
            Game.player[c] = new Bot(Game.texture[(int) (Math.random()*textureAnzahl)],Game.shottexture[(int) (Math.random()*shottextureAnzahl)],Game.dbImage,KeyEvent.VK_A,KeyEvent.VK_D,KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_Q,c,35,"Spieler");
            Game.player[c].laden(Game,(int) (Math.random()*(Game.ebenen[0][1]-Game.ebenen[0][0])+Game.ebenen[0][0]),0);
          } // end of if
        } // end of if-else
      } // end of for
      Game.restartGame();
    } // end of if
    
    if (isNumeric(maxFps.getText())) {
      if (Integer.parseInt(maxFps.getText())<=1000 && Integer.parseInt(maxFps.getText())>0) {
        Game.gamerunner.maxFPS=Integer.parseInt(maxFps.getText());
      } // end of if
    } // end of if
    
    if (e.getKeyCode()==KeyEvent.VK_ESCAPE) {              //Zur�ckkehren zum Spiel
      Game.gamerunner.running=true;
      if (Game.soundan) {
        Game.ac.loop(10);
      } // end of if
      this.dispose(); 
    }   
  }  
  
  public static boolean isNumeric(String str)  
  {  
    try  
    {  
      double d = Double.parseDouble(str);  
    }  
    catch(NumberFormatException nfe)  
    {  
      return false;  
    }  
    return true;  
  }
  
  class TestWindowListener extends WindowAdapter
  {
    public void windowClosing(WindowEvent e)
    {
      Game.gamerunner.running=true;
      if (Game.soundan) {
        Game.ac.loop(10);
      } // end of if
      e.getWindow().dispose();                   // Fenster "killen"
    }           
  }
  
  public void paint(Graphics g)  
  {  
    super.paint(g);  
    for (int c=0;c<Game.texture.length;c++) {
      if (Game.texture[c] != null) {
        try {
          BufferedImage Image = ImageIO.read(Game.texture[c]);
          this.getGraphics().drawImage(Image,120+71*c,140,this);
        } catch(IOException exeption) {
          
        }
      }  
    } // end of for
    
    for (int c=0;c<Game.shottexture.length;c++) {
      if (Game.shottexture[c] != null) {
        try {
          BufferedImage Image = ImageIO.read(Game.shottexture[c]);
          this.getGraphics().drawImage(Image,120+71*c,280,this);
        } catch(IOException exeption) {
          
        }
      }  
    } // end of for  
  }  
  
  
  public void actionPerformed(ActionEvent e) {
    for (int c=0;c<buttonSpieler.length;c++ ) {
      if (e.getSource()==buttonSpieler[c]) {
        int spieler = SpielerAuswahl.getSelectedIndex()+1;
        try {
          BufferedImage Image = ImageIO.read(Game.texture[c]);
          Game.player[spieler].textureImage  = Image;
          Game.player[spieler].textureImageb = Game.player[spieler].verticalflip(Image);
        } 
        catch(IOException exeption) {
          
        }
      } // end of if
    }
    
    for (int c=0;c<buttonSchuss.length;c++ ) {
      if (e.getSource()==buttonSchuss[c]) {
        int spieler = SpielerAuswahl.getSelectedIndex()+1;
        Game.player[spieler].shottexture = Game.shottexture[c];
      } // end of if
    } // end of for
    
    if (e.getSource()==Sound) {
      if (Game.soundan) {
        Game.soundan=false;
        Sound.setLabel("Sound an");
      } // end of if
      else {
        Game.soundan=true;
        Sound.setLabel("Sound aus");
      } // end of if-else
    } // end of if
    
    if (e.getSource()==Fps) {
      if (Game.fpsan) {
        Game.fpsan=false;
        Fps.setLabel("FPS anzeigen");
      } // end of if
      else {
        Game.fpsan=true;
        Fps.setLabel("FPS ausblenden");
      } // end of if-else
    } // end of if
    
    if (e.getSource()==Restart) {
      Game.restartGame();
      if (Game.soundan) {
        Game.ac.loop(10);
      } // end of if
      this.dispose();
      Game.gamerunner.running=true;
    } // end of if  
    
    if (e.getSource()==Bot) {
      int spieler = SpielerAuswahl.getSelectedIndex()+1;
      Game.player[spieler] = new Bot(Game.player[spieler].playertexture,Game.player[spieler].shottexture,Game.player[spieler].dbImage,0,0,0,0,0,spieler,Game.player[spieler].yHealth,Game.player[spieler].name);
      Game.player[spieler].laden(Game,Game.player[spieler].x,Game.player[spieler].y);
    } // end of if
    
    
  }
  
  
  public void update(Graphics g) {
    
  }  
  
}