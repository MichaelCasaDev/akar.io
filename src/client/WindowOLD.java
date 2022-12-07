package client;

import javax.swing.*;

import client.views.GameplayOLD;

public class WindowOLD extends JFrame {
  public WindowOLD() {
    JFrame frame = new JFrame("Akar.IO");
    JScrollPane pane = new JScrollPane();
    JViewport vport = new JViewport();
    GameplayOLD panel = new GameplayOLD();

    panel.setvPort(vport);
    vport.add(panel);
    frame.setVisible(true);
    pane.setViewport(vport);
    vport.add(panel);
    frame.add(pane);
    frame.setSize(GameplayOLD.WIDTH, GameplayOLD.HEIGHT);
    pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
  }
}
