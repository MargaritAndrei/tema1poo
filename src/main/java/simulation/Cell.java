package simulation;

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
        int count = 2;
        sum += soil.calculateBlockProbability();
        sum += air.calculateToxicity(currentTimestamp);
        if (plant != null) {
            sum += Entity.round(plant.plantPossibility() / 100.0);
            count++;
        }
        if (animal != null) {
            sum += animal.calculateAttackRisk();
            count++;
        }
        double mean = Math.abs(sum / count);
        return (int) Math.round(mean);
    }
    public void processEvolution(int currentTimestamp) {
        if (animal != null && animal.scanned) {
            animal.updateState(air, currentTimestamp);
        }
        soil.tryToGrowPlant(plant);
        if (water != null && water.scanned) {
            soil.tryToAbsorbWater(water, currentTimestamp);
            water.tryToInteractWithAir(air, currentTimestamp);
            water.tryToGrowPlant(plant);
        }
        if (plant != null && plant.scanned) {
            air.addOxygen(plant.calculateOxygenProduction());
        }
        if (animal != null && animal.scanned) {
            animal.produceFertilizer(soil);
            animal.tryToFeed(this);
        }
    }
}