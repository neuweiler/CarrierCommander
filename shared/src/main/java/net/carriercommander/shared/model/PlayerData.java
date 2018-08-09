package net.carriercommander.shared.model;

import com.jme3.network.serializing.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Object holding all the necessary data required to transfer between client and server.
 *
 * @author Michael Neuweiler
 */
@Serializable
public class PlayerData {

  private static final int NUM_WALRUS = 4;
  private static final int NUM_MANTA  = 4;

  private int              id;
  private CarrierData      carrier;
  private List<WalrusData> walrus;
  private List<MantaData>  manta;

  public PlayerData() {
    carrier = new CarrierData();

    walrus = new ArrayList<>(NUM_WALRUS);
    for (int i = 0; i < NUM_WALRUS; i++) {
      walrus.add(new WalrusData());
    }

    manta = new ArrayList<>(NUM_MANTA);
    for (int i = 0; i < NUM_MANTA; i++) {
      manta.add(new MantaData());
    }
  }

  public boolean isModified() {
    return carrier.isModified() |
        walrus.stream().anyMatch(WalrusData::isModified) |
        manta.stream().anyMatch(MantaData::isModified);
  }

  public void clean() {
    carrier.clean();
    walrus.forEach(WalrusData::clean);
    manta.forEach(MantaData::clean);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public CarrierData getCarrier() {
    return carrier;
  }

  public WalrusData getWalrus(int index) {
    return walrus.get(index);
  }

  public MantaData getManta(int index) {
    return manta.get(index);
  }


}
