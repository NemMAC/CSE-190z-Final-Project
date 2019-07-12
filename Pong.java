// Milan C
// CSE 190z
// Final Project: Pong Alpha v1.0.0
// This project is a small Graphics Panel based pong game.
// 11/15/18

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import java.util.concurrent.TimeUnit;

public class Pong {  
   
   // Initialize global variables, commonly used across most methods
   public static int vPadelOne = 0;
   public static int vPadelTwo = vPadelOne;
   public static int xPadelOne = 1150;    
   public static int yPadelOne = 375;
   public static int yPadelTwo = yPadelOne;
   public static int xPadelTwo = 30;
   public static int padelWidth = 20; 
   public static int padelHeight = 150;
   public static int xComp = 1;
   public static int yComp = xComp;
   public static int xBall = 590;
   public static int yBall = 440;
   public static int wBall = 20;
   public static int hBall = wBall;
   public static int vXBall = 4;
   public static int vYBall = 4;
   public static int playerOneScore = 0;
   public static int playerTwoScore = 0;
   public static int screen = -1;
   public static int game = 1; 
   
   public static DrawingPanel panel = new DrawingPanel(1200, 900);
   public static Graphics g = panel.getGraphics();
   
   public static void main(String[] args) {
         
      // Listen for key presses
      PaddleKeyListener listener = new PaddleKeyListener(panel);
      panel.addKeyListener(listener);
      
      while (game == 1) {
         
         // Game mode selection screen (two player)
         while (screen == -1) {
            
            setInitialConditions();
            panel.setBackground(Color.black);
            g.setColor(Color.black);
            g.fillRect(0, 0, 1200, 900);
            g.setColor(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            g.drawString("Press '2' for two players.", 480, 450);
            panel.sleep(100); 
            
         }
         
         // Game enviornment
         while (screen == 0) {
            
            g.setColor(Color.black);
            g.fillRect(480, 400, 300, 100);
            g.fillRect(525, 10, 200, 20);
            g.fillRect(xPadelOne, yPadelOne, padelWidth, padelHeight);
            g.fillRect(xPadelTwo, yPadelTwo, padelWidth, padelHeight);
            g.fillRect(xBall, yBall, wBall, hBall);
            
            enforceGameLogic();
              
            g.setColor(Color.white); 
            g.drawString("" + playerOneScore, 525, 30);
            g.drawString("" + playerTwoScore, 675, 30);
            g.fillRect(xPadelOne, yPadelOne, padelWidth, padelHeight);
            g.fillRect(xPadelTwo, yPadelTwo, padelWidth, padelHeight);
            g.fillOval(xBall, yBall, wBall, hBall);
            panel.sleep(10); 
            
         }
      }     
   }  
   
   
   /**
   * Sets initial conditions for the ball and paddle.
   */
   public static void setInitialConditions() {
   
      xPadelOne = 1150;    
      yPadelOne = 375;
      yPadelTwo = yPadelOne;
      xPadelTwo = 30;
      padelWidth = 20; 
      padelHeight = 150;
      playerOneScore = 0;
      playerTwoScore = playerOneScore;
      xComp = 1;
      yComp = xComp;
      xBall = 590;
      yBall = 440;
      wBall = 20;
      hBall = wBall;
      vXBall = 4;
      vYBall = vXBall;
   
   }
   
   /**
   * Sets the logic of the ball and paddle.
   */
   public static void enforceGameLogic() {
      
      // Basic movement
      xBall += (vXBall * xComp);
      yBall += (vYBall * yComp);
      yPadelTwo += (vPadelTwo * 5);
      yPadelOne += (vPadelOne * 5);
      
      // Stops paddles on y axis bounds      
      if (yPadelOne >= 750 || yPadelOne <= 0) {     
         yPadelOne -= (vPadelOne * 5);
      } 
            
      if (yPadelTwo >= 750 || yPadelTwo <= 0) {
         yPadelTwo -= (vPadelTwo * 5);
      }  
      
      // Bound ball movement on y axis  
      if (yBall >= 880 || yBall <= 0 ) {
         vYBall = -vYBall;
      } 
      
      // Padel & ball interaction    
      if ((xBall == (xPadelTwo + padelWidth) && yBall >= yPadelTwo && yBall <= (yPadelTwo + padelHeight)) ||
      (xBall == (xPadelOne - padelWidth) && yBall >= yPadelOne && yBall <= (yPadelOne + padelHeight))) {  
         vXBall = -vXBall;
      }
      
      // Reset ball upon scoring     
      if (xBall >= 1180) {
         playerOneScore += 1;
         delayRespawn();
         xBall = 590;
         yBall = 440;
      } else if (xBall <= 0) {
         playerTwoScore += 1;
         delayRespawn();
         xBall = 590;
         yBall = 440;
      }
      
      // End game at a score of 7    
      if (playerOneScore == 7 || playerTwoScore == 7) {
         screen = -1;
      }
   
   }
   
   // Method for delaying respawn found at https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
   /**
   * Delays the code execution.
   */
   public static void delayRespawn() {
   
      try {
         Thread.sleep(3000);
      } catch(InterruptedException ex) {
         Thread.currentThread().interrupt();
      }
      
   }
   
   // Class was provided to me in Office Hours on Thurs Nov 29th
   
   /** 
   * A class for responding to key presses on the drawing panel.
   */
   public static class PaddleKeyListener extends KeyAdapter {
      private DrawingPanel panel;
            
      public PaddleKeyListener(DrawingPanel panel) {
         this.panel = panel;
      }
      
      public void keyPressed(KeyEvent event) {
         int code = event.getKeyCode();
         
         if (code == KeyEvent.VK_DOWN) {
            vPadelOne = 1;
         } else if (code == KeyEvent.VK_UP) {
            vPadelOne = -1; 
         }
           
         if (code == KeyEvent.VK_S) {
            vPadelTwo = 1; 
         } else if (code == KeyEvent.VK_W) {
            vPadelTwo = -1; 
         }
         
         if (code == KeyEvent.VK_2 && screen == -1) {
            screen = 0; 
         }
      }
   }
}
