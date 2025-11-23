package simulation.animal;

import simulation.Cell;
import simulation.Table;
import simulation.air.Air;
import simulation.Entity;
import simulation.plant.Plant;
import simulation.soil.Soil;
import simulation.water.Water;

public abstract class Animal extends Entity {
    protected AnimalState state;
    public boolean scanned;
    protected int lastMoveTimestamp;
    protected int x, y;
    private double fertilizerToProduce;
    public Animal(String name, double mass) {
        super(name, mass);
        state = AnimalState.hungry;
        scanned = false;
        lastMoveTimestamp = 0;
        fertilizerToProduce = 0;
    }
    protected abstract boolean isPredator();
    public abstract double animalPossibility();
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public double calculateAttackRisk() {
        double score = (100 - animalPossibility()) / 10.0;
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    public void setLastMoveTimestamp(int timestamp) {
        lastMoveTimestamp = timestamp;
    }
    public void produceFertilizer(Soil soil) {
        soil.addOrganicMatter(fertilizerToProduce);
        fertilizerToProduce = 0;
    }
    public void updateState(Air currentAir, int currentTimestamp) {
        double toxicThreshold = 0.8 * currentAir.getMaxScore();
        double toxicity = currentAir.calculateToxicity(currentTimestamp);
        if (toxicity > toxicThreshold) {
            state = AnimalState.sick;
        } else if (state == AnimalState.sick) {
            state = AnimalState.hungry;
        }
    }
    private Cell processMoveInteraction(Cell targetCell, int currentTimestamp) {
        tryToFeed(targetCell);
        if (targetCell.getAnimal() == null) {
            lastMoveTimestamp = currentTimestamp;
            return targetCell;
        }
        return null;
    }
    public Cell move(Table map, int x, int y, int currentTimestamp) {
        if (!scanned || currentTimestamp - lastMoveTimestamp < 2) {
            return null;
        }
        Cell moveCell = null;
        double bestWaterQuality = -1.0;
        int dx[] = {0, 1, 0, -1};
        int dy[] = {1, 0, -1, 0};
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                Plant plant = candidateCell.getPlant();
                Water water = candidateCell.getWater();
                Animal animal = candidateCell.getAnimal();
                if (animal != null && !isPredator()) {
                    continue;
                }
                if (plant != null && water != null && plant.isScanned() && water.isScanned()) {
                    if (candidateCell.getWater().calculateQuality() > bestWaterQuality) {
                        bestWaterQuality = candidateCell.getWater().calculateQuality();
                        moveCell = candidateCell;
                    }
                }
            }
        }
        if (moveCell != null) {
            if (processMoveInteraction(moveCell, currentTimestamp) != null) {
                System.out.println(getName() + " moved to cell " + "(" +
                        moveCell.getX() + ", " + moveCell.getY() + ")" + " at timestamp " + currentTimestamp);
                return moveCell;
            }
        }
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                Plant plant = candidateCell.getPlant();
                Animal animal  = candidateCell.getAnimal();
                if (animal != null && !isPredator()) {
                    continue;
                }
                if (plant != null && plant.isScanned()) {
                    if (processMoveInteraction(candidateCell, currentTimestamp) != null) {
                        System.out.println(getName() + " moved to cell " + "(" +
                                candidateCell.getX() + ", " + candidateCell.getY() + ")" +
                                " at timestamp " + currentTimestamp);
                        return candidateCell;
                    }
                }
            }
        }
        bestWaterQuality = -2.0;
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                Water water = candidateCell.getWater();
                Animal animal = candidateCell.getAnimal();
                if (animal != null && !isPredator()) {
                    continue;
                }
                if (water != null && water.isScanned() &&
                        water.calculateQuality() > bestWaterQuality) {
                    bestWaterQuality = candidateCell.getWater().calculateQuality();
                    moveCell = candidateCell;
                }
            }
        }
        if (moveCell != null) {
            if (processMoveInteraction(moveCell, currentTimestamp) != null) {
                System.out.println(getName() + " moved to cell " + "(" +
                        moveCell.getX() + ", " + moveCell.getY() + ")" + " at timestamp " + currentTimestamp);
                return moveCell;
            }
        }
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                Animal animal = candidateCell.getAnimal();
                if (animal != null && !isPredator()) {
                    continue;
                }
                if (processMoveInteraction(candidateCell, currentTimestamp) != null) {
                    System.out.println(getName() + " moved to cell " + "(" +
                            candidateCell.getX() + ", " + candidateCell.getY() + ")" +
                            " at timestamp " + currentTimestamp);
                    return candidateCell;
                }
            }
        }
        return null;
    }
    public void tryToFeed(Cell currentCell) {
        if (!scanned) {
            return;
        }
        state = AnimalState.well_fed;
        Water water = currentCell.getWater();
        Plant plant = currentCell.getPlant();
        Animal prey = currentCell.getAnimal();
        Soil soil = currentCell.getSoil();
        boolean plantScanned = (plant != null && plant.isScanned());
        boolean waterScanned = (water != null && water.isScanned());
        if (isPredator() && prey != null && prey != this) {
            setMass(getMass() + prey.getMass());
            fertilizerToProduce = 0.5;
            produceFertilizer(soil);
            currentCell.setAnimal(null);
            return;
        }
        if (plantScanned && waterScanned) {
            currentCell.setPlant(null);
            double intakeRate = 0.08;
            double waterToDrink = Math.min(getMass() * intakeRate, water.getMass());
            water.setMass(water.getMass() - waterToDrink);
            if (water.getMass() <= 0) {
                currentCell.setWater(null);
            }
            setMass(getMass() + waterToDrink + plant.getMass());
            fertilizerToProduce = 0.8;
            produceFertilizer(soil);
            return;
        }
        if (plantScanned) {
            currentCell.setPlant(null);
            setMass(getMass() + plant.getMass());
            fertilizerToProduce = 0.5;
            produceFertilizer(soil);
            return;
        }
        if (waterScanned) {
            double intakeRate = 0.08;
            double waterToDrink = Math.min(getMass() * intakeRate, water.getMass());
            water.setMass(water.getMass() - waterToDrink);
            if (water.getMass() <= 0) {
                currentCell.setWater(null);
            }
            setMass(getMass() + waterToDrink);
            fertilizerToProduce = 0.5;
            produceFertilizer(soil);
            return;
        }
        state = AnimalState.hungry;
    }
}