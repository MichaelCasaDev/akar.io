package server.managers;

import java.util.ArrayList;

import server.entities.Spike;
import server.utils.Pos;

public class SpikeManager {
  private ArrayList<Spike> spikes;

  public SpikeManager() {
    spikes = new ArrayList<Spike>();
  }

  public void initSpike(int sizeX, int sizeY) {
    for (int i = 0; i < sizeX * sizeY / 10000; i++) {
      int posX = (int) ((Math.random() * (sizeX - 0)) + 0);
      int posY = (int) ((Math.random() * (sizeY - 0)) + 0);

      spikes.add(new Spike(new Pos(posX, posY), i));
    }
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
