package simulation.plant;

import simulation.Entity;

public abstract class Plant extends Entity {
    protected MaturityState maturityState;
    protected double growthProgress;
    private boolean scanned;
    protected int lastGrowthTimestamp;

    private static final double BONUS_YOUNG_OXYGEN = 0.2;
    private static final double BONUS_MATURE_OXYGEN = 0.7;
    private static final double BONUS_OLD_OXYGEN = 0.4;
    private static final double GROWTH_TIMESTAMPS_REQUIRED = 2;
    private static final double MAX_GROWTH_PROGRESS = 1.0;
    private static final double NO_OXYGEN_PRODUCTION = 0.0;

    public Plant(final String name, final double mass) {
        super(name, mass);
        growthProgress = 0;
        maturityState = MaturityState.young;
        scanned = false;
        lastGrowthTimestamp = 0;
    }

    /**
     * Seteaza cand a crescut planta ultima oara.
     */
    public final void setLastGrowthTimestamp(final int timestamp) {
        lastGrowthTimestamp = timestamp;
    }

    /**
     * Returneaza maturitystate-ul plantei.
     */
    public final MaturityState getMaturityState() {
        return maturityState;
    }

    /**
     * Proceseaza cresterea plantei.
     */
    public final void grow(final double amount, final int currentTimestamp) {
        if (maturityState == MaturityState.dead) {
            return;
        }
        if (currentTimestamp - lastGrowthTimestamp < GROWTH_TIMESTAMPS_REQUIRED) {
            return;
        }
        if (!scanned) {
            return;
        }
        growthProgress += amount;
        growthProgress = Entity.round(growthProgress);

        if (growthProgress >= MAX_GROWTH_PROGRESS) {
            growthProgress = NO_OXYGEN_PRODUCTION; // Reset growth progress
            if (maturityState == MaturityState.young) {
                maturityState = MaturityState.mature;
            } else if (maturityState == MaturityState.mature) {
                maturityState = MaturityState.old;
            } else if (maturityState == MaturityState.old) {
                maturityState = MaturityState.dead;
            }
        }
    }

    /**
     * Calculates the total oxygen production.
     */
    public final double calculateOxygenProduction() {
        if (maturityState == MaturityState.dead) {
            return NO_OXYGEN_PRODUCTION;
        }
        double bonusOxygen;
        if (maturityState == MaturityState.young) {
            bonusOxygen = BONUS_YOUNG_OXYGEN;
        } else if (maturityState == MaturityState.mature) {
            bonusOxygen = BONUS_MATURE_OXYGEN;
        } else {
            bonusOxygen = BONUS_OLD_OXYGEN;
        }
        return Entity.round(categoryOxygen() + bonusOxygen);
    }
    /**
     * Returneaza oxigenul produs.
     */
    protected abstract double categoryOxygen();
    /**
     * Calculeaza posiblitatea de a bloca terrabot.
     */
    public abstract double plantPossibility();
    /**
     * Returneaza daca planta a fost scanata.
     */
    public boolean isScanned() {
        return scanned;
    }
    /**
     * Seteaza scanarea plantei.
     */
    public void setScanned(final boolean scanned) {
        this.scanned = scanned;
    }
}
