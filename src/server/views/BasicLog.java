package server.views;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;

public class BasicLog {
  public static int WIDTH = 1280;
  public static int HEIGHT = 768;
  private DefaultListModel<String> model;
  private JList<String> list;

  public BasicLog() {
    JFrame frame = new JFrame("Akar.IO | Server");
    JPanel contentPane = new JPanel();
    contentPane.setLayout(null);
    frame.setContentPane(contentPane);

    String text = "Akar.IO Server";
    Font font = new Font("Helvetica", Font.PLAIN, 12);
    Canvas c = new Canvas();
    FontMetrics fontMetrics = c.getFontMetrics(font);

    JLabel title = new JLabel(text);
    title.setBounds((WIDTH / 2) - (fontMetrics.stringWidth(text) / 2), 8, WIDTH, 48);
    contentPane.add(title);

    JScrollPane scrollPane = new JScrollPane();

    scrollPane.setBounds(64, 64, WIDTH - 128, HEIGHT - 128);
    contentPane.add(scrollPane);

    list = new JList<String>();
    scrollPane.setViewportView(list);

    model = new DefaultListModel<String>();
    list.setModel(model);

    frame.setSize(WIDTH, HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public void addLine(String s) {
    model.addElement(s);

    int lastIndex = list.getModel().getSize() - 1;
    if (lastIndex >= 0) {
      list.ensureIndexIsVisible(lastIndex);
      list.setSelectedIndex(lastIndex);
    }
  }
}
