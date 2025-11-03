package simulation.Plant;

public class Algae extends Plant {
    public Algae (String name, double mass) {
        super(name, mass);
    }
    @Override
    protected double categoryOxygen() {
        return 0.5;
    }
    @Override
    public double plantPossibility() {
        return 20;
    }
}
