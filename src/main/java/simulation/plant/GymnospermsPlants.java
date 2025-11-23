package simulation.plant;

public final class GymnospermsPlants extends Plant {
    private static final double OXYGEN = 0.0;
    private static final double PLANT_POSSIBILITY = 60.0;
    public GymnospermsPlants(final String name, final double mass) {
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
