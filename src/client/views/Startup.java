package client.views;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.managers.ClientManager;
import client.managers.ClientManager.WindowStates;

public class Startup extends JPanel {
  private ClientManager clientManager;

  public Startup(ClientManager clientManager) {
    this.clientManager = clientManager;
  }

  public void launchAlert() {
    String addressAndPort = JOptionPane.showInputDialog(null,
        "Inserisci indirizzo IP e porta", "localhost:7373");
    String[] p = addressAndPort.split(":");

    if (p.length != 2) {
      JOptionPane.showMessageDialog(null, "Non hai inserito un indirizzo e porta validi!", "Errore",
          JOptionPane.ERROR_MESSAGE);

      clientManager.getWindowManager().dispose();
    } else {
      try {
        clientManager.setRemoteHost(p[0]);
        clientManager.setRemotePort(Integer.parseInt(p[1]));
        clientManager.setWindowStates(WindowStates.MENU);
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "La porta inserita non è valida!", "Errore",
            JOptionPane.ERROR_MESSAGE);

        clientManager.getWindowManager().close();
      }
    }
  }
}
