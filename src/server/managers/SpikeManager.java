package server.managers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import server.entities.Spike;
import server.utils.Pos;

public class SpikeManager {
  private ArrayList<Spike> spikes;

  public SpikeManager() {
    spikes = new ArrayList<Spike>();
  }

  public void initSpike(int tot) {
    System.out.println("[GAME MAP " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Generating spikes...");

    for (int i = 0; i < tot; i++) {
      int posX = (int) ((Math.random() * (9000 - 1000)) + 1000);
      int posY = (int) ((Math.random() * (9000 - 1000)) + 1000);

      spikes.add(new Spike(new Pos(posX, posY), i));
    }

    System.out.println("[GAME MAP " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Spikes generated!");
  }

  public int getTotalSpikes() {
    return spikes.size();
  }

  public Spike[] getSpikes() {
    Spike[] spikeArr = new Spike[spikes.size()];
    for (int i = 0; i < spikes.size(); i++) {
      spikeArr[i] = spikes.get(i);
    }

    return spikeArr;
  }
}
