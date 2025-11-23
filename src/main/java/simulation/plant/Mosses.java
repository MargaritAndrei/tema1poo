package simulation.plant;

public final class Mosses extends Plant {
    private static final double OXYGEN = 0.8;
    private static final double PLANT_POSSIBILITY = 40.0;
    public Mosses(final String name, final double mass) {
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
