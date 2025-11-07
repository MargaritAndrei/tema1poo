package simulation.plant;

public class GymnospermsPlants extends Plant {
    public GymnospermsPlants(String name, double mass) {
        super(name, mass);
    }
    @Override
    protected double categoryOxygen() {
        return 0;
    }
    @Override
    public double plantPossibility() {
        return 60;
    }
}
