package simulation.Animal;

public class Parasites extends Animal {
    public Parasites(String name, double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return true;
    }
    @Override
    public double animalPossibility() {
        return 10;
    }
}
