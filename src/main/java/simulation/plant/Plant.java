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
     * Sets the timestamp of the last growth event.
     *
     * @param timestamp The timestamp to set.
     */
    public final void setLastGrowthTimestamp(final int timestamp) {
        lastGrowthTimestamp = timestamp;
    }

    /**
     * Returns the maturity state of the plant.
     *
     * @return The current maturity state.
     */
    public final MaturityState getMaturityState() {
        return maturityState;
    }

    /**
     * Processes the growth of the plant.
     *
     * @param amount           The amount of growth to add.
     * @param currentTimestamp The current timestamp of the simulation.
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
     * Calculates the total oxygen production based on the plant's maturity state.
     *
     * @return The calculated oxygen production value.
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
     * Returns the base oxygen production for the specific plant category.
     *
     * @return The base oxygen production value.
     */
    protected abstract double categoryOxygen();
    /**
     * Calculates the possibility of the plant blocking the Terrabot.
     *
     * @return The blocking possibility value.
     */
    public abstract double plantPossibility();
    /**
     * Checks if the plant has been scanned.
     *
     * @return True if the plant is scanned, false otherwise.
     */
    public boolean isScanned() {
        return scanned;
    }
    /**
     * Sets the scanned status of the plant.
     *
     * @param scanned The new scanned status.
     */
    public void setScanned(final boolean scanned) {
        this.scanned = scanned;
    }
}
