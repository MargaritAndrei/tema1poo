package simulation.Plant;

public class FloweringPlants extends Plant {
    public FloweringPlants(String name, double mass) {
        super(name, mass);
    }
    @Override
    protected double categoryOxygen() {
        return 6.0;
    }
    @Override
    public double plantPossibility() {
        return 90;
    }
}
