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

  ////// Standart Thread f�r das aktualisieren aller Komponenten
  
class GameRunner extends Thread {
  // Anfang Attribute5
  Player player[] = null;
  Shot shot[] = new Shot[1000];
  JavaGame Game ;
  boolean isthereshot = false;
  boolean running = true;
  File perktexture ;
  perks perk[] = new perks[100000];
  int count=0;
  int time =(int) System.currentTimeMillis()*1000;
  int maxFPS = 30;
  int winner,neustart=-1;
  boolean neu=false,schonneu=false;
  int auftretenvonperks=9;
  int totencounter=0;
  
  // Ende Attribute5
  
  public GameRunner (Player[] player, JavaGame Game) {
    this.player = player;
    this.Game = Game;
    perktexture = new File(Game.basePath,"/perk.png");
    this.start();
  }  
  // Anfang Komponenten5
  // Ende Komponenten5
  // Anfang Methoden5
  
  public void run() {
    while (true) {
      synchronized(getClass()) { 
        try {
          sleep(1000/maxFPS);
        }
        catch(InterruptedException e) {
        }
        if (running) {
          Game.repaint();
          Game.dbImage.getGraphics().clearRect(0,0, (int)Game.getWidth(), (int)Game.getHeight());
          
          Game.dbImage.getGraphics().drawImage(Game.backgroundImage,100,200-67,Game);
          
          int perkjn= (int) (Math.random()*3000+1);
          if (perkjn<auftretenvonperks) {
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
          // Game.dbImage.getGraphics().fillRect(100,600,900,10);
          Game.dbImage.getGraphics().drawString("Music: Early Riser Kevin MacLeod (incompetech.com)", Game.getWidth()-320, Game.getHeight()-20);
          
          int timediff = ((int) System.currentTimeMillis()-time);
          if (Game.fpsan) {
            //System.out.println("FPS: " + 1000/timediff);
            Game.dbImage.getGraphics().drawString("FPS: "+ 1000/timediff, Game.getWidth()-380, Game.getHeight()-20);
          } // end of if
          time =(int) System.currentTimeMillis();
          
          for (int c = 1;c<Game.ebenen.length;c++) {
            if (Game.ebenen[c] != null) {
              Game.dbImage.getGraphics().drawLine(Game.ebenen[c][0],Game.ebenen[c][2],Game.ebenen[c][1],Game.ebenen[c][2]);
            } // end of if
          } // end of for
          
          
          /*for (int c = 1;c<Game.player.length;c++) {
          if (Game.player[counter].health=0) {
          if (platz1==100) {
          platz1=counter;
          } // end of if                                             ///Platzierungen
          if (platz1!=100 && platz2==100) {
          platz2=counter;
          } // end of if
          if (platz2!=100 && platz3==100) {
          platz3=counter;
          } // end of 
          
          } // end of if
          } */
          
          for (int c=1;c<Game.player.length;c++) {
            if (Game.player[c].health<=0) {
              totencounter++;
            } // end of if
          } // end of for
          System.out.println(totencounter);
          if (totencounter==Game.player.length-2) {
            for (int c=1;c<Game.player.length;c++) {
              if (Game.player[c].health>0) {
                Game.dbImage.getGraphics().drawString(Game.player[c].name+" hat gewonnen",500,120);    
                if (!neu) {
                  neustart=300;
                  neu=true;
                  schonneu=false;
                } // end of if
                
                Game.dbImage.getGraphics().drawString("Neustart in "+neustart/10,500,135);
              } // end of if
            } // end of for
          } // end of if
          totencounter=0;
          
          
          neustart-=1;
          if (neustart==0 && !schonneu) {
            for (int c=1;c<Game.player.length;c++) {
              Game.player[c].x=(int) (Math.random()*(Game.ebenen[0][1]-Game.ebenen[0][0])+Game.ebenen[0][0]);
              Game.player[c].y=0;
              Game.player[c].health=100;
              Game.player[c].jumpheigth=200;
              Game.player[c].speed=5;
              Game.player[c].sperrzeit=40;
              Game.player[c].freezeControls=false;
              neu=false;
            } // end of for
          } // end of if
          
          
          Game.getGraphics().drawImage(Game.dbImage,0,0,Game);
        }
        
        
        
      } // end of if
      
    } // end of while
    
  }  
  // Ende Methoden5
}  