package client;

import javax.swing.*;

import client.views.Gameplay;

public class Window extends JFrame {
  public Window() {
    JFrame frame = new JFrame("Akar.IO");
    JScrollPane pane = new JScrollPane();
    JViewport vport = new JViewport();
    Gameplay panel = new Gameplay();

    vport.add(panel);
    frame.setVisible(true);
    pane.setViewport(vport);
    vport.add(panel);
    frame.add(pane);
    frame.setSize(1280, 768);
    panel.setvPort(vport);
    pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
  }
}
