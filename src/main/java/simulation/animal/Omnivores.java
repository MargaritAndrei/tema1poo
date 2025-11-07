package simulation.animal;

public class Omnivores extends Animal {
    public Omnivores(String name, double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return false;
    }
    @Override
    public double animalPossibility() {
        return 60;
    }
}
