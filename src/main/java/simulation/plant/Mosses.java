package simulation.plant;

public class Mosses extends Plant {
    public Mosses(String name, double mass) {
        super(name, mass);
    }
    @Override
    protected double categoryOxygen() {
        return 0.8;
    }
    @Override
    public double plantPossibility() {
        return 40;
    }
}
