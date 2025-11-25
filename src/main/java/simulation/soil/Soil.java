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
     * Calculates the soil quality.
     *
     * @return The calculated soil quality score.
     */
    public abstract double calculateQuality();
    /**
     * Calculates the probability of blocking the Terrabot.
     *
     * @return The calculated block probability.
     */
    public abstract double calculateBlockProbability();
    /**
     * Adds specific fields to the JSON output based on the soil type.
     *
     * @param node The JSON ObjectNode to which fields will be added.
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
     * Sets the timestamp when the soil was last scanned by the Terrabot.
     *
     * @param timestamp The timestamp of the scan.
     */
    public void setScanTimestamp(final int timestamp) {
        lastWaterAbsorptionTimestamp = timestamp;
    }

    /**
     * Handles the interaction with water: the soil absorbs water if conditions are met.
     *
     * @param water            The water entity to absorb from.
     * @param currentTimestamp The current timestamp of the simulation.
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
     * Adds an amount to the organic matter content of the soil.
     *
     * @param amount The amount of organic matter to add.
     */
    public void addOrganicMatter(final double amount) {
        organicMatter += amount;
        final double normalized = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, organicMatter));
        organicMatter = Entity.round(normalized);
    }

    /**
     * Handles the interaction with a plant: the plant grows if conditions are met.
     *
     * @param plant            The plant entity growing on the soil.
     * @param currentTimestamp The current timestamp of the simulation.
     */
    public void tryToGrowPlant(final Plant plant, final int currentTimestamp) {
        if (plant != null && plant.isScanned()) {
            // Folosind constanta
            plant.grow(DEFAULT_GROWTH_FACTOR, currentTimestamp);
        }
    }

    /**
     * Adds an amount to the water retention of the soil.
     *
     * @param amount The amount of water retention to add.
     */
    public void addWaterRetention(final double amount) {
        waterRetention += amount;
        final double normalized = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, waterRetention));
        waterRetention = Entity.round(normalized);
    }
}
