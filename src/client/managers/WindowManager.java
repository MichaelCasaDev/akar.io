package client.managers;

import javax.swing.*;

import client.views.Gameplay;

public class WindowManager extends JFrame {
  private JPanel jPanel;
  public static int WIDTH = 1280;
  public static int HEIGHT = 768;
  private JViewport vport = new JViewport();

  public WindowManager() {
    JFrame frame = new JFrame("Akar.IO");
    JScrollPane pane = new JScrollPane();

    vport.add(this.jPanel);
    pane.setViewport(vport);
    pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    frame.add(pane);
    frame.setSize(WIDTH, HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public void setjPanel(JPanel jPanel) {
    this.jPanel = jPanel;

    if (jPanel instanceof Gameplay) {
      ((Gameplay) jPanel).setvPort(vport);
    }

    vport.add(this.jPanel);
  }

  public JPanel getjPanel() {
    return jPanel;
  }

  public void close() {
    System.out.println("Closed");
    System.exit(0);
  }
}
