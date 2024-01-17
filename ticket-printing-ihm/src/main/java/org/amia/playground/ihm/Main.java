package org.amia.playground.ihm;
import java.awt.Color;
import java.awt.Graphics;
import java.security.SecureRandom;
import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;


class DrawPanel extends JPanel
{
   private SecureRandom randomNumbers = new SecureRandom();   
   private MyLine[] lines; // array on lines

   // constructor, creates a panel with random shapes
   public DrawPanel()
   {//  w w  w . j av  a 2s.  co  m
      setBackground(Color.WHITE);
      
      lines = new MyLine[5 + randomNumbers.nextInt(5)];

      // create lines
      for (int count = 0; count < lines.length; count++)
      {
         // generate random coordinates
         int x1 = randomNumbers.nextInt(300);
         int y1 = randomNumbers.nextInt(300);
         int x2 = randomNumbers.nextInt(300);
         int y2 = randomNumbers.nextInt(300);
         
         // generate a random color
         Color color = new Color(randomNumbers.nextInt(256), 
            randomNumbers.nextInt(256), randomNumbers.nextInt(256));
         
         // add the line to the list of lines to be displayed
         lines[count] = new MyLine(x1, y1, x2, y2, color);
      } 
   } 

   // for each shape array, draw the individual shapes
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);

      // draw the lines
      for (MyLine line : lines)
         line.draw(g);
   } 
}

class MyLine
{
   private int x1; // x-coordinate of first endpoint
   private int y1; // y-coordinate of first endpoint
   private int x2; // x-coordinate of second endpoint
   private int y2; // y-coordinate of second endpoint
   private Color color; // color of this line

   // constructor with input values
   public MyLine(int x1, int y1, int x2, int y2, Color color)
   {
      this.x1 = x1; 
      this.y1 = y1; 
      this.x2 = x2; 
      this.y2 = y2; 
      this.color = color; 
   } 
   
   // Actually draws the line
   public void draw(Graphics g)
   {
      g.setColor(color);
      g.drawLine(x1, y1, x2, y2);
   } 
}

public class Main
{
   public static void main(String[] args)
   {
      DrawPanel panel = new DrawPanel();      
      JFrame app = new JFrame();
      
      app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      app.add(panel);
      app.setSize(300, 300);
      app.setVisible(true);
   } 
}