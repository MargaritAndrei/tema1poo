package simulation;

import simulation.plant.MaturityState;
import simulation.soil.Soil;
import simulation.air.Air;
import simulation.plant.Plant;
import simulation.animal.Animal;
import simulation.water.Water;

public final class Cell {
    private static final double MAX_PERCENTAGE = 100.0;
    private static final int BASE_RISK_COMPONENTS = 2; // Soil Block Prob + Air Toxicity

    private Soil soil;
    private Air air;
    private Plant plant;
    private Animal animal;
    private Water water;
    private int x;
    private int y;
    public Cell(final Soil soil, final Air air, final Plant plant, final Animal animal,
                final Water water, final int x, final int y) {
        this.soil = soil;
        this.air = air;
        this.plant = plant;
        this.animal = animal;
        this.water = water;
        this.x = x;
        this.y = y;
    }
    public Soil getSoil() {
        return soil;
    }
    public Air getAir() {
        return air;
    }
    public Plant getPlant() {
        return plant;
    }
    public Animal getAnimal() {
        return animal;
    }
    public Water getWater() {
        return water;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setSoil(final Soil newSoil) {
        soil = newSoil;
    }
    public void setAir(final Air newAir) {
        air = newAir;
    }
    public void setPlant(final Plant newPlant) {
        plant = newPlant;
    }
    public void setAnimal(final Animal newAnimal) {
        this.animal = newAnimal;
    }
    public void setWater(final Water newWater) {
        this.water = newWater;
    }
    public void setX(final int newX) {
        this.x = newX;
    }
    public void setY(final int newY) {
        this.y = newY;
    }

    /**
     * Calculeaza riscul unei celule in care TerraBot
     * se poate deplasa.
     */
    public int calculateRobotRiskScore(final int currentTimestamp) {
        double sum = 0;
        double count = BASE_RISK_COMPONENTS;
        sum += soil.calculateBlockProbability();
        sum += air.calculateToxicity(currentTimestamp);
        if (plant != null) {
            double score = plant.plantPossibility() / MAX_PERCENTAGE;
            double normalizedScore = Math.max(0, Math.min(MAX_PERCENTAGE, score));
            sum += Entity.round(normalizedScore);
            count++;
        }
        if (animal != null) {
            sum += animal.calculateAttackRisk();
            count++;
        }
        final double mean = Math.abs(sum / count);
        final double normalizedMean = Math.max(0, Math.min(MAX_PERCENTAGE, mean));
        return (int) Math.round(normalizedMean);
    }

    /**
     * Proceseaza interactiunile dintre entitati
     * de pe o celula.
     */
    public void processEvolution(final int currentTimestamp) {
        if (animal != null && animal.isScanned()) {
            animal.updateState(air, currentTimestamp);
        }
        soil.tryToGrowPlant(plant, currentTimestamp);
        if (plant != null && plant.getMaturityState() == MaturityState.dead) {
            this.plant = null;
        }
        if (water != null && water.isScanned()) {
            soil.tryToAbsorbWater(water, currentTimestamp);
            water.tryToInteractWithAir(air, currentTimestamp);
            water.tryToGrowPlant(plant, currentTimestamp);
        }
        if (plant != null && plant.getMaturityState() == MaturityState.dead) {
            this.plant = null;
        }
        if (plant != null && plant.isScanned()) {
            air.addOxygen(plant.calculateOxygenProduction());
        }
        if (animal != null && animal.isScanned()) {
            animal.tryToFeed(this);
        }
    }
}
