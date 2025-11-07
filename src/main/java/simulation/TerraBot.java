package simulation;

import simulation.animal.Animal;
import simulation.plant.Plant;
import simulation.water.Water;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerraBot {
    public TerraBot(int initialEnergy) {
        energy = initialEnergy;
        x = 0;
        y = 0;
        inventory = new ArrayList<>();
        database = new HashMap<>();
        chargeEndTime = 0;
    }
    private double energy;
    private int x, y;
    private int chargeEndTime;
    private List<String> inventory;
    private Map<String, List<String>> database;
    public double getEnergy() {
        return energy;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isCharging(int currentTimestamp) {
        return currentTimestamp < chargeEndTime;
    }
    public boolean spendEnergy(int amount) {
        if (energy >= amount) {
            this.energy -= amount;
            return true;
        }
        return false;
    }
    public String move(Table map, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        int dx[] = {-1, 0, 1, 0};
        int dy[] = {0, 1, 0, -1};
        Cell bestCell = null;
        int bestX = x;
        int bestY = y;
        int bestScore = Integer.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            Cell neighborCell = map.getCell(newX, newY);
            if (neighborCell != null) {
                int currentScore = neighborCell.calculateRobotRiskScore(currentTimestamp);
                if (currentScore < bestScore) {
                    bestScore = currentScore;
                    bestX = newX;
                    bestY = newY;
                }
            }
        }
        if (spendEnergy(bestScore)) {
            x = bestX;
            y = bestY;
            return "The robot has successfully moved to position (" + bestX + "," + bestY + ").";
        }
        return "ERROR: Not enough battery left. Cannot perform action";
    }
    public String rechargeBattery(int timeToCharge, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        chargeEndTime = currentTimestamp + timeToCharge;
        return "Robot battery is charging.";
    }
    public String scanObject(Cell currentCell, String color, String smell,
                             String sound, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        if (!spendEnergy(7)) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        if (color.equals("none") && smell.equals("none") && sound.equals("none")) {
            Water water = currentCell.getWater();
            if (water != null && !water.scanned) {
                water.scanned = true;
                inventory.add(water.getName());
                return "The scanned object is water.";
            }
        }
        if (color.equals("pink") && smell.equals("sweet") && sound.equals("none")) {
            Plant plant = currentCell.getPlant();
            if (plant != null && !plant.scanned) {
                plant.scanned = true;
                inventory.add(plant.getName());
                return "The scanned object is a plant.";
            }
        }
        if (color.equals("brown") && smell.equals("earthy") && sound.equals("muuu")) {
            Animal animal = currentCell.getAnimal();
            if (animal != null && !animal.scanned) {
                animal.scanned = true;
                inventory.add(animal.getName());
                return "The scanned object is an animal.";
            }
        }
        return "ERROR: Object not found. Cannot perform action";
    }
    public String learnFact(String subject, String components, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        if (!spendEnergy(2)) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        if (inventory.contains(components))  {
            database.computeIfAbsent(components, k -> new ArrayList<>()).add(subject);
            return "The fact has been successfully saved in the database.";
        } else {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }
    }
    public String improveEnvironment(Cell currentCell, String improvementType,
                                     String componentName, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        if (!spendEnergy(10)) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        if (!inventory.contains(componentName)) {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }
        String requiredFact = "Method to " + improvementType;

        return null;
    }
}
