package simulation.water;

import simulation.air.Air;
import simulation.Entity;
import simulation.plant.Plant;

import static java.lang.Math.abs;

public class Water extends Entity {
    public Water(String name, double mass, double salinity, double ph,
                 double purity, double turbidity, double contaminantIndex, boolean isFrozen) {
        super(name, mass);
        scanned = false;
        lastInteractionTimestamp = 0;
        this.salinity = salinity;
        this.ph = ph;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
    }
    protected double salinity;
    protected double ph;
    protected double purity;
    protected double turbidity;
    protected double contaminantIndex;
    public boolean isFrozen, scanned;
    protected int lastInteractionTimestamp;
    public void tryToInteractWithAir(Air air, int currentTimestamp) {
        if (currentTimestamp - lastInteractionTimestamp >= 2) {
            lastInteractionTimestamp = currentTimestamp;
            air.addHumidity(0.1);
        }
    }
    public double calculateQuality() {
        double purity_score = purity / 100;
        double pH_score = 1 - abs(ph - 7.5) / 7.5;
        double salinity_score = 1 - (salinity / 350);
        double turbidity_score = 1 - (turbidity / 100);
        double contaminant_score = 1 - (contaminantIndex / 100);
        double frozen_score = isFrozen ? 0 : 1;
        double quality = (0.3 * purity_score
                + 0.2 * pH_score
                + 0.15 * salinity_score
                + 0.1 * turbidity_score
                + 0.15 * contaminant_score
                + 0.2 * frozen_score) * 100;
        return Entity.round(quality);
    }
    public void tryToGrowPlant(Plant plant) {
        if (plant != null) {
            plant.grow(0.2);
        }
    }
}