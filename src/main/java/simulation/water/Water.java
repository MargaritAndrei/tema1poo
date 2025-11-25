package simulation.water;

import simulation.air.Air;
import simulation.Entity;
import simulation.plant.Plant;

import static java.lang.Math.abs;

public final class Water extends Entity {
    protected String type;
    protected double salinity;
    protected double ph;
    protected double purity;
    protected double turbidity;
    protected double contaminantIndex;
    protected boolean isFrozen, scanned;
    protected int lastInteractionTimestamp;

    private static final double HUMIDITY_ADDED_VALUE = 0.1;
    private static final double PLANT_GROWTH_FACTOR = 0.2;

    private static final double MAX_PERCENT_VALUE = 100.0;
    private static final double IDEAL_PH = 7.5;
    private static final double MAX_PH_DEVIATION = 7.5;
    private static final double MAX_SALINITY = 350.0;
    private static final double MAX_TURBIDITY = 100.0;
    private static final double MAX_CONTAMINANT_INDEX = 100.0;

    private static final double WEIGHT_PURITY = 0.3;
    private static final double WEIGHT_PH = 0.2;
    private static final double WEIGHT_SALINITY = 0.15;
    private static final double WEIGHT_TURBIDITY = 0.1;
    private static final double WEIGHT_CONTAMINANT = 0.15;
    private static final double WEIGHT_FROZEN = 0.2;
    private static final double TOTAL_WEIGHT_MULTIPLIER = 100.0;

    public Water(final String name, final double mass, final String type, final double salinity,
                 final double ph, final double purity, final double turbidity,
                 final double contaminantIndex, final boolean isFrozen) {
        super(name, mass);
        scanned = false;
        this.type = type;
        this.salinity = Entity.round(salinity);
        this.ph = Entity.round(ph);
        this.purity = Entity.round(purity);
        this.turbidity = Entity.round(turbidity);
        this.contaminantIndex = Entity.round(contaminantIndex);
        this.isFrozen = isFrozen;
    }
    public String getType() {
        return type;
    }
    public double getPurity() {
        return purity;
    }
    public double getPh() {
        return ph;
    }
    public double getSalinity() {
        return salinity;
    }
    public double getTurbidity() {
        return turbidity;
    }
    public double getContaminantIndex() {
        return  contaminantIndex;
    }
    public boolean isFrozen() {
        return isFrozen;
    }
    public boolean isScanned() {
        return scanned;
    }
    public void setFrozen(final boolean frozen) {
        isFrozen = frozen;
    }
    public void setScanned(final boolean scanned) {
        this.scanned = scanned;
    }
    public void setScanTimestamp(final int timestamp) {
        lastInteractionTimestamp = timestamp;
    }
    /**
     * Processes the interaction with the air: adds humidity to the air.
     *
     * @param air              The air entity to interact with.
     * @param currentTimestamp The current timestamp of the simulation.
     */
    public void tryToInteractWithAir(final Air air, final int currentTimestamp) {
        if (currentTimestamp - lastInteractionTimestamp >= 2) {
            lastInteractionTimestamp = currentTimestamp;
            air.addHumidity(HUMIDITY_ADDED_VALUE);
        }
    }
    /**
     * Processes the interaction with a plant: helps the plant grow.
     *
     * @param plant            The plant entity to interact with.
     * @param currentTimestamp The current timestamp of the simulation.
     */
    public void tryToGrowPlant(final Plant plant, final int currentTimestamp) {
        if (plant != null && plant.isScanned()) {
            plant.grow(PLANT_GROWTH_FACTOR,  currentTimestamp);
        }
    }
    /**
     * Calculates the general quality score of the water based on its properties.
     *
     * @return The calculated quality score.
     */
    public double calculateQuality() {
        double purityScore = purity / MAX_PERCENT_VALUE;
        double phScore = 1.0 - abs(ph - IDEAL_PH) / MAX_PH_DEVIATION;
        double salinityScore = 1.0 - (salinity / MAX_SALINITY);
        double turbidityScore = 1.0 - (turbidity / MAX_TURBIDITY);
        double contaminantScore = 1.0 - (contaminantIndex / MAX_CONTAMINANT_INDEX);
        double frozenScore = isFrozen ? 0.0 : 1.0;
        double quality = (WEIGHT_PURITY * purityScore
                + WEIGHT_PH * phScore
                + WEIGHT_SALINITY * salinityScore
                + WEIGHT_TURBIDITY * turbidityScore
                + WEIGHT_CONTAMINANT * contaminantScore
                + WEIGHT_FROZEN * frozenScore) * TOTAL_WEIGHT_MULTIPLIER;

        return Entity.round(quality);
    }
}
