package simulation.plant;

import simulation.Entity;

public abstract class Plant extends Entity {
    protected MaturityState maturityState;
    protected double growthProgress;
    public boolean scanned;
    public Plant (String name, double mass) {
        super(name, mass);
        growthProgress = 0;
        maturityState = MaturityState.young;
        scanned = false;
        lastGrowthTimestamp = 0;
    }
    protected int lastGrowthTimestamp;
    protected abstract double categoryOxygen();
    public abstract double plantPossibility();
    public void setLastGrowthTimestamp(int timestamp) {
        lastGrowthTimestamp = timestamp;
    }
    public MaturityState getMaturityState() {
        return  maturityState;
    }
    public void grow (double amount, int currentTimestamp) {
        if (maturityState == MaturityState.dead) {
            return;
        }
        if (currentTimestamp - lastGrowthTimestamp < 2) {
            return;
        }
        if (!scanned) {
            return;
        }
        growthProgress += amount;
        growthProgress = Entity.round(growthProgress);
        if (growthProgress >= 1.0) {
            growthProgress = 0;
            if (maturityState == MaturityState.young) {
                maturityState = MaturityState.mature;
            } else if (maturityState == MaturityState.mature) {
                maturityState = MaturityState.old;
            } else if (maturityState == MaturityState.old) {
                maturityState = MaturityState.dead;
            }
        }
    }
    public double calculateOxygenProduction() {
        if (maturityState == MaturityState.dead) {
            return 0;
        }
        double bonusOxygen;
        if (maturityState == MaturityState.young) {
            bonusOxygen = 0.2;
        } else if (maturityState == MaturityState.mature) {
            bonusOxygen = 0.7;
        } else {
            bonusOxygen = 0.4;
        }
        return Entity.round(categoryOxygen() + bonusOxygen);
    }
}