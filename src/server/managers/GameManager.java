package server.managers;

import server.entities.Food;
import server.entities.Player;
import server.entities.Spike;

public class GameManager {
  private PlayersManager pManager;
  private FoodManager fManager;
  private SpikeManager sManager;

  // game map sizes
  private final int sizeX = 4000;
  private final int sizeY = 4000;

  public GameManager() {
    pManager = new PlayersManager();
    fManager = new FoodManager();
    sManager = new SpikeManager();

    newGame();
  }

  public void newGame() {
    fManager.initFood(3000);
    sManager.initSpike(60);
  }

  public int checkCollision(Player p) {
    for (Player pX : pManager.getPlayers()) {
      if (!p.getUsername().equals(pX.getUsername())
          && p.collide(pX.getMass(), pX.getMass(), pX.getPos().getPosX(), pX.getPos().getPosY())) {
        return 0;
      }
    }

    for (Food fX : fManager.getFoods()) {
      if (fX != null && p.collide(20, 20, fX.getPos().getPosX(), fX.getPos().getPosY())) {
        return 1;
      }
    }

    for (Spike sX : sManager.getSpikes()) {
      if (p.collide(sX.getSize(), sX.getSize(), sX.getPos().getPosX(), sX.getPos().getPosY())) {
        return 2;
      }
    }

    return -1;
  }

  public Player getOtherPlayerCollision(Player p) {
    for (Player pX : pManager.getPlayers()) {
      if (!p.getUsername().equals(pX.getUsername())
          && p.collide(pX.getMass(), pX.getMass(), pX.getPos().getPosX(), pX.getPos().getPosY()) && !pX.isDead()) {
        return pX;
      }
    }

    return null;
  }

  public Food getOtherFoodCollision(Player p) {
    for (Food fX : fManager.getFoods()) {
      if (fX != null && p.collide(20, 20, fX.getPos().getPosX(), fX.getPos().getPosY())) {
        return fX;
      }
    }

    return null;
  }

  public void kill(Player player) {
    player.setDead(true);

    pManager.updatePlayer(player);
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
