package simulation.animal;

public class Herbivores extends Animal {
    public Herbivores(String name, double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return false;
    }
    @Override
    public double animalPossibility() {
        return 85;
    }
}
