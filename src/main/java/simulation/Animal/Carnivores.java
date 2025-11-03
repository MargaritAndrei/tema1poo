package simulation.Animal;

public class Carnivores extends Animal {
    public Carnivores(String name, double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return true;
    }
    @Override
    public double animalPossibility() {
        return 30;
    }
}
