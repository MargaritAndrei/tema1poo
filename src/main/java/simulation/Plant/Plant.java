package simulation.Plant;

import simulation.Entity;

public abstract class Plant extends Entity {
    public Plant (String name, double mass) {
        super(name, mass);
        growthProgress = 0;
        maturityState = MaturityState.young;
        scanned = false;
    }
    protected MaturityState maturityState;
    protected double growthProgress;
    public boolean scanned;
    protected abstract double categoryOxygen();
    public abstract double plantPossibility();
    public void grow (double amount) {
        if (maturityState == MaturityState.dead) {
            return;
        }
        growthProgress += amount;
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
        double bonusOxygen = 0;
        if (maturityState == MaturityState.young) {
            bonusOxygen = 0.2;
        } else if (maturityState == MaturityState.mature) {
            bonusOxygen = 0.7;
        } else {
            bonusOxygen = 0.4;
        }
        return categoryOxygen() + bonusOxygen;
    }
}