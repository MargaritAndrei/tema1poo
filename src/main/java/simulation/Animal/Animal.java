package simulation.Animal;

import simulation.Entity;

public abstract class Animal extends Entity {
    public Animal(String name, double mass) {
        super(name, mass);
        state = AnimalState.hungry;
        scanned = false;
        lastMoveTimestamp = 0;
    }
    protected AnimalState state;
    protected boolean scanned;
    protected int lastMoveTimestamp;
    protected abstract boolean isPredator();
    public abstract double animalPossibility();
    public void updateState() {

    }
    public void tryToMove() {

    }
    public void tryToFeed() {

    }
    public double calculateAttackRisk() {
        return (100 - animalPossibility()) / 10.0;
    }
}
