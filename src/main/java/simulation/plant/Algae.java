package simulation.plant;

public final class Algae extends Plant {
    private static final double OXYGEN = 0.5;
    private static final double PLANT_POSSIBILITY = 20.0;
    public Algae(final String name, final double mass) {
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
