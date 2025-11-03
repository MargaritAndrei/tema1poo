package simulation.Soil;

import simulation.Entity;
import simulation.Water.Water;

public abstract class Soil extends Entity {
    public Soil(String name, double mass, double nitrogen, double waterRetention,
    double soilpH, double organicMatter) {
        super(name, mass);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
        lastWaterAbsorptionTimestamp = 0;
    }
    protected double nitrogen;
    protected double waterRetention;
    protected double soilpH;
    protected double organicMatter;
    protected int lastWaterAbsorptionTimestamp;
    public abstract double calculateQuality();
    public abstract double calculateBlockProbability();
    public void tryToAbsorbWater(Water water, int currentTimestamp) {

    }
    public void addOrganicMatter(double amount) {

    }
}
