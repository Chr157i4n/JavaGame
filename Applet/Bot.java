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

class Bot extends Player {
  
  public Bot(File playertexture, File shottexture, Image dbImage, int left, int right , int jump, int down, int attack, int xHealth, int yHealth, String name) {
    super(playertexture, shottexture, dbImage, left, right, jump, down, attack, xHealth, yHealth, name);
  }  
  
  public void updateKey()
  {
    if (name.equals("boss")) {          //Cheat
      health=200;                       //Cheat
    } // end of if
    name = "Bot";  //ERste �nderung an der update Klasse: hier sollte die KI/KD sachen stehen und die Keysachen nicht...
    
    
    
    if (y + textureImage.getHeight() > Game.ebenen[0][2] + 200) {
      health=0;
    } // end of if
    updateJump(jumpupdate);
    updateBoom(15);
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
    
    if (x+textureImage.getWidth()<0) {                                               
      int xpoints[] = {0,10,10};
      int ypoints[] = {y,y+10,y-10};
      Game.dbImage.getGraphics().drawPolygon(xpoints,ypoints,3);
    } // end of if
    
    if (x>Game.getWidth()) {
      int xpoints[] = {Game.getWidth(),Game.getWidth()-10,Game.getWidth()-10};
      int ypoints[] = {y,y+10,y-10};
      Game.dbImage.getGraphics().drawPolygon(xpoints,ypoints,3);
    } // end of if
    
    if (y+textureImage.getHeight()<0) {
      int xpoints[] = {x,x-10,x+10};
      int ypoints[] = {0,10,10};
      Game.dbImage.getGraphics().drawPolygon(xpoints,ypoints,3); 
    } // end of if
    
    if (health <= 0) {
      freezeControls=true;
    } // end of if
    
    //Perk begrenzen
    //perks anzeigen
    if (jumpheigth==300) {
      Game.dbImage.getGraphics().drawString("Springen "+perkz�hlerjump/10,xHealth,yHealth+50);
      perkz�hlerjump=perkz�hlerjump-1;
    } // end of if
    if (sperrzeit==25) {
      Game.dbImage.getGraphics().drawString("Schie�en "+perkz�hlershoot/10,xHealth,yHealth+65);
      perkz�hlershoot=perkz�hlershoot-1;
    } // end of if
    if (speed==10) {
      Game.dbImage.getGraphics().drawString("Rennen   "+perkz�hlerrun/10,xHealth,yHealth+80);
      perkz�hlerrun=perkz�hlerrun-1;
    } // end of if
    //perks anzeigen Ende
    
    //perks zur�cksetzen
    if (perkz�hlerjump<0) {
      jumpheigth=200;
    } // end of if
    if (perkz�hlerrun<0) {
      speed=5;
    } // end of if
    if (perkz�hlershoot<0) {
      sperrzeit=40;
    } // end of if
    //perks zur�cksetzen Ende
    
    //Perk begrenzen Ende
    drawboom=drawboom+1;
    
    if (drawboom<15) {
      Game.dbImage.getGraphics().drawImage(boomImage,boomx,boomy,Game);
    } // end of if
    
    if (Game.player[1].name.equals("boss")) {          //Cheat
      Game.player[1].health=200;                       //Cheat
      Game.player[1].name="bot";                       //Cheat
    } // end of if
    
    for (int c=1;c<Game.player.length;c++) {
      dif=x-Game.player[c].x;
      if (dif<min) {
        min=dif;
        angriffsziel=c;
      } // end of if
    }
    
    
    
    if (x<100 || x>1000) {      //selbstschutz
      if (x<100)
      {x += speed;
        characterInverted = false;
      }
      if (x>1000)
      {x -= speed;
        characterInverted = true;
      }
    } 
    else {
      
      if (Game.player[angriffsziel].health>0 && Game.player[angriffsziel] != this) {
        int botdif=y-Game.player[angriffsziel].y;      //Kampf gegen Spieler 2
        
        if (x<Game.player[angriffsziel].x) {
          x +=speed/2;
          characterInverted = false;
          
        } // end of if
        if (x>Game.player[angriffsziel].x) {
          x -=speed/2;
          characterInverted = true; 
        } // end of if 
        
        if (x==Game.player[angriffsziel].x) {
          if (schusssperre == 0) {
            Shot bullet = new Shot(shottexture,!characterInverted, 10, Game,this);
            bullet.laden(x,y+50);
            schusssperre = sperrzeit;
          } // end of if
        }
        if (botdif<80 && botdif>-80) {     //selbe H�he
          
          if (schusssperre == 0) {
            Shot bullet = new Shot(shottexture,!characterInverted, 10, Game,this);
            bullet.laden(x,y+50);
            schusssperre = sperrzeit;
          } // end of if
        } // end of if
        
        if (y>Game.player[angriffsziel].y) {
            setJump(jumpheigth);
          } // end of if
        
      } 
      
    }
  }
  
  
  
  
  
  
  
  
  
  
  
}