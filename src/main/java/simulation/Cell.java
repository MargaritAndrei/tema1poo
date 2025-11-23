package simulation;

import simulation.plant.MaturityState;
import simulation.soil.Soil;
import simulation.air.Air;
import simulation.plant.Plant;
import simulation.animal.Animal;
import simulation.water.Water;

public class Cell {
    public Cell(Soil soil, Air air, Plant plant, Animal animal, Water water, int x, int y) {
        this.soil = soil;
        this.air = air;
        this.plant = plant;
        this.animal = animal;
        this.water = water;
        this.x = x;
        this.y = y;
    }
    private Soil soil;
    private Air air;
    private Plant plant;
    private Animal animal;
    private Water water;
    private int x,y;
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
    public void setSoil(Soil soil) {
        this.soil = soil;
    }
    public void setAir(Air air) {
        this.air = air;
    }
    public void setPlant(Plant plant) {
        this.plant = plant;
    }
    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
    public void setWater(Water water) {
        this.water = water;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int calculateRobotRiskScore(int currentTimestamp) {
        double sum = 0;
        double count = 2;
        sum += soil.calculateBlockProbability();
        sum += air.calculateToxicity(currentTimestamp);
        if (plant != null) {
            double score = plant.plantPossibility() / 100.0;
            double normalizedScore = Math.max(0, Math.min(100, score));
            sum += Entity.round(normalizedScore);
            count++;
        }
        if (animal != null) {
            sum += animal.calculateAttackRisk();
            count++;
        }
        double mean = Math.abs(sum / count);
        double normalizedMean = Math.max(0, Math.min(100, mean));
        return (int) Math.round(normalizedMean);
    }
    public void processEvolution(int currentTimestamp) {
        if (animal != null && animal.scanned) {
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
        if (animal != null && animal.scanned) {
            animal.tryToFeed(this);
        }
    }
}