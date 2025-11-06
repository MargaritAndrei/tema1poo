package simulation.Animal;

import simulation.Air.Air;
import simulation.Cell.Cell;
import simulation.Entity;
import simulation.Map;
import simulation.Plant.Plant;
import simulation.Soil.Soil;
import simulation.Water.Water;

public abstract class Animal extends Entity {
    public Animal(String name, double mass) {
        super(name, mass);
        state = AnimalState.hungry;
        scanned = false;
        lastMoveTimestamp = 0;
    }
    protected AnimalState state;
    public boolean scanned;
    protected int lastMoveTimestamp;
    protected abstract boolean isPredator();
    public abstract double animalPossibility();
    public void updateState(Air currentAir) {
        double toxicThreshold = 0.8 * currentAir.getmaxScore();
        double toxicity = currentAir.calculateToxicity();
        if (toxicity > toxicThreshold) {
            state = AnimalState.sick;
        }
    }
    public Cell move(Map map, int x, int y, int currentTimestamp) {
        if (!scanned || currentTimestamp - lastMoveTimestamp < 2) {
            return null;
        }
        Cell moveCell = null;
        double bestWaterQuality = -1.0;
        int dx[] = {-1, 0, 1, 0};
        int dy[] = {0, 1, 0, -1};
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                if (candidateCell.getPlant() != null && candidateCell.getWater() != null) {
                    if (candidateCell.getWater().calculateQuality() > bestWaterQuality) {
                        bestWaterQuality = candidateCell.getWater().calculateQuality();
                        moveCell = candidateCell;
                    }
                }
            }
        }
        if (moveCell != null) {
            lastMoveTimestamp = currentTimestamp;
            return moveCell;
        }
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null && candidateCell.getPlant() != null) {
                lastMoveTimestamp = currentTimestamp;
                return candidateCell;
            }
        }
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null && candidateCell.getWater() != null &&
                    candidateCell.getWater().calculateQuality() > bestWaterQuality) {
                bestWaterQuality = candidateCell.getWater().calculateQuality();
                moveCell = candidateCell;
            }
        }
        if (moveCell != null) {
            lastMoveTimestamp = currentTimestamp;
            return moveCell;
        }
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                lastMoveTimestamp = currentTimestamp;
                return candidateCell;
            }
        }
        return null;
    }
    public void tryToFeed(Cell currentCell) {
        if (!scanned || state == AnimalState.sick) {
            return;
        }
        state = AnimalState.well_fed;
        Soil soil = currentCell.getSoil();
        Water water = currentCell.getWater();
        Plant plant = currentCell.getPlant();
        Animal prey = currentCell.getAnimal();
        if (isPredator() && prey != null) {
            setMass(getMass() + prey.getMass());
            soil.addOrganicMatter(0.5);
            currentCell.setAnimal(null);
            return;
        }
        boolean plantScanned = (plant != null && plant.scanned);
        boolean waterScanned = (water != null && water.scanned);
        if (plantScanned && waterScanned) {
            currentCell.setPlant(null);
            double intakeRate = 0.08;
            double waterToDrink = Math.min(getMass() * intakeRate, water.getMass());
            water.setMass(water.getMass() - waterToDrink);
            setMass(getMass() + waterToDrink + plant.getMass());
            soil.addOrganicMatter(0.8);
            return;
        }
        if (plantScanned) {
            currentCell.setPlant(null);
            setMass(getMass() + plant.getMass());
            soil.addOrganicMatter(0.5);
            return;
        }
        if (waterScanned) {
            double intakeRate = 0.08;
            double waterToDrink = Math.min(getMass() * intakeRate, water.getMass());
            water.setMass(water.getMass() - waterToDrink);
            setMass(getMass() + waterToDrink);
            soil.addOrganicMatter(0.5);
            return;
        }
        state = AnimalState.hungry;
    }
    public double calculateAttackRisk() {
        return (100 - animalPossibility()) / 10.0;
    }
}