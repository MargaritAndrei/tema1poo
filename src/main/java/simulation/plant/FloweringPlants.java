package simulation.plant;

public final class FloweringPlants extends Plant {
    private static final double OXYGEN = 6.0;
    private static final double PLANT_POSSIBILITY = 90.0;
    public FloweringPlants(final String name, final double mass) {
        super(name, mass);
    }
    @Override
    protected double categoryOxygen() {
        return OXYGEN;
    }
    @Override
    public double plantPossibility() {
        return PLANT_POSSIBILITY;
    }
}
