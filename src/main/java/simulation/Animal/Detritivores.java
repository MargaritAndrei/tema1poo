package simulation.Animal;

public class Detritivores extends Animal {
    public Detritivores(String name, double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return false;
    }
    @Override
    public double animalPossibility() {
        return 90;
    }
}
