package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;
import simulation.water.Water;
import simulation.plant.Plant;

public abstract class Soil extends Entity {
    public Soil(String name, double mass, double nitrogen, double waterRetention,
                double soilpH, double organicMatter) {
        super(name, mass);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
    }
    protected double nitrogen;
    protected double waterRetention;
    protected double soilpH;
    protected double organicMatter;
    protected int lastWaterAbsorptionTimestamp;
    public abstract double calculateQuality();
    public abstract double calculateBlockProbability();
    public abstract void addSpecificFieldsToJson(ObjectNode node);
    public double getNitrogen() {
        return nitrogen;
    }
    public double getWaterRetention() {
        return waterRetention;
    }
    public double getSoilpH() {
        return soilpH;
    }
    public double getOrganicMatter() {
        return organicMatter;
    }
    public void setScanTimestamp(int timestamp) {
        lastWaterAbsorptionTimestamp = timestamp;
    }
    public void tryToAbsorbWater(Water water, int currentTimestamp) {
        if (water != null && water.scanned) {
            if (currentTimestamp - lastWaterAbsorptionTimestamp >= 2) {
                waterRetention += 0.1;
                waterRetention = Entity.round(waterRetention);
                lastWaterAbsorptionTimestamp = currentTimestamp;
            }
        }
    }
    public void addOrganicMatter(double amount) {
        organicMatter += amount;
        double normalized = Math.max(0, Math.min(100, organicMatter));
        organicMatter = Entity.round(normalized);
    }
    public void tryToGrowPlant(Plant plant) {
        if (plant != null && plant.scanned) {
            plant.grow(0.2);
        }
    }
    public void addWaterRetention(double amount) {
        waterRetention += amount;
        double normalized = Math.max(0, Math.min(100, waterRetention));
        waterRetention = Entity.round(normalized);
    }
}