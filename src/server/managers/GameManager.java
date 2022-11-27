package server.managers;

import server.entities.Food;
import server.entities.Player;
import server.entities.Spike;

public class GameManager {
  private PlayersManager pManager;
  private FoodManager fManager;
  private SpikeManager sManager;

  private final int sizeX = 4000;
  private final int sizeY = 4000;

  public GameManager() {
    pManager = new PlayersManager();
    fManager = new FoodManager();
    sManager = new SpikeManager();

    newGame();
  }

  public void newGame() {
    fManager.initFood(sizeX, sizeY);
    sManager.initSpike(sizeX, sizeY);
  }

  public int checkCollision(Player p) {
    for (Player pX : pManager.getPlayers()) {
      if (pX.getPos().collide(p.getPos(), p.getMass())) {
        return 0;
      }
    }

    for (Food fX : fManager.getFoods()) {
      if (fX.getPos().collide(p.getPos(), p.getMass())) {
        return 1;
      }
    }

    for (Spike sX : sManager.getSpikes()) {
      if (sX.getPos().collide(p.getPos(), p.getMass())) {
        return 2;
      }
    }

    return -1;
  }

  public Player getOtherPlayerCollision(Player p) {
    for (Player pX : pManager.getPlayers()) {
      if (pX.getPos().collide(p.getPos(), p.getMass())) {
        return pX;
      }
    }

    return null;
  }

  public void kill(Player player) {
    player.setDead(true);

    pManager.updatePlayer(player.getUsername(), player);
  }

  public PlayersManager getpManager() {
    return pManager;
  }

  public FoodManager getfManager() {
    return fManager;
  }

  public SpikeManager getsManager() {
    return sManager;
  }

  public int getSizeX() {
    return sizeX;
  }

  public int getSizeY() {
    return sizeY;
  }

}
