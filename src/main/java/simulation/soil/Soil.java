package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;
import simulation.water.Water;
import simulation.plant.Plant;

public abstract class Soil extends Entity {
    private static final double MAX_PERCENTAGE_VALUE = 100.0;
    private static final int WATER_ABSORPTION_INTERVAL = 2;
    private static final double WATER_ABSORPTION_AMOUNT = 0.1;
    private static final double DEFAULT_GROWTH_FACTOR = 0.2;

    protected double nitrogen;
    protected double waterRetention;
    protected double soilpH;
    protected double organicMatter;
    protected int lastWaterAbsorptionTimestamp;

    public Soil(final String name, final double mass, final double nitrogen,
                final double waterRetention, final double soilpH, final double organicMatter) {
        super(name, mass);
        this.nitrogen = Entity.round(nitrogen);
        this.waterRetention = Entity.round(waterRetention);
        this.soilpH = Entity.round(soilpH);
        this.organicMatter = Entity.round(organicMatter);
    }
    /**
     * Calculeaza calitatea solului.
     */
    public abstract double calculateQuality();
    /**
     * Calculeaza probabilitatea de a bloca TerraBot.
     */
    public abstract double calculateBlockProbability();
    /**
     * Adauga campurile specifice tipului de sol in output.
     */
    public abstract void addSpecificFieldsToJson(ObjectNode node);
    public final double getNitrogen() {
        return nitrogen;
    }

    public final double getWaterRetention() {
        return waterRetention;
    }

    public final double getSoilpH() {
        return soilpH;
    }

    public final double getOrganicMatter() {
        return organicMatter;
    }
    /**
     * Seteaza timestampul momentului scanarii solului de catre TerraBot.
     */
    public void setScanTimestamp(final int timestamp) {
        lastWaterAbsorptionTimestamp = timestamp;
    }

    /**
     * Interactiunea cu apa
     */
    public void tryToAbsorbWater(final Water water, final int currentTimestamp) {
        if (water != null && water.isScanned()) {
            if (currentTimestamp - lastWaterAbsorptionTimestamp >= WATER_ABSORPTION_INTERVAL) {
                waterRetention += WATER_ABSORPTION_AMOUNT;
                waterRetention = Entity.round(waterRetention);
                lastWaterAbsorptionTimestamp = currentTimestamp;
            }
        }
    }

    /**
     * Adauga amount la campul organicMatter.
     */
    public void addOrganicMatter(final double amount) {
        organicMatter += amount;
        final double normalized = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, organicMatter));
        organicMatter = Entity.round(normalized);
    }

    /**
     * Interactiunea cu planta.
     */
    public void tryToGrowPlant(final Plant plant, final int currentTimestamp) {
        if (plant != null && plant.isScanned()) {
            // Folosind constanta
            plant.grow(DEFAULT_GROWTH_FACTOR, currentTimestamp);
        }
    }

    /**
     * Adauga amount la waterRentention.
     */
    public void addWaterRetention(final double amount) {
        waterRetention += amount;
        final double normalized = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, waterRetention));
        waterRetention = Entity.round(normalized);
    }
}
