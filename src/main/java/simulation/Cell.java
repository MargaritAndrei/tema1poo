package simulation.Cell;

import simulation.Entity;
import simulation.Soil.Soil;
import simulation.Air.Air;
import simulation.Plant.Plant;
import simulation.Animal.Animal;
import simulation.Water.Water;

public class Cell {
    public Cell(Soil soil, Air air, Plant plant, Animal animal, Water water) {
        this.soil = soil;
        this.air = air;
        this.plant = plant;
        this.animal = animal;
        this.water = water;
    }
    private Soil soil;
    private Air air;
    private Plant plant;
    private Animal animal;
    private Water water;
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
    public int calculateRobotRiskScore() {
        double sum = 0;
        int count = 2;
        sum += soil.calculateBlockProbability();
        sum += air.calculateToxicity();
        if (plant != null) {
            sum += plant.plantPossibility();
            count++;
        }
        if (animal != null) {
            sum += animal.calculateAttackRisk();
            count++;
        }
        double mean = sum / count;
        return (int) Math.round(mean);
    }
    public void processEvolution(int currentTimestamp) {
        if (animal != null && animal.scanned) {
            animal.updateState(air);
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
            animal.tryToFeed(this);
        }
    }
}