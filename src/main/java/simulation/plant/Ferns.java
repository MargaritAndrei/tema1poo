package simulation.plant;

public final class Ferns extends Plant {
    private static final double OXYGEN = 0.0;
    private static final double PLANT_POSSIBILITY = 30.0;
    public Ferns(final String name, final double mass) {
        super(name, mass);
    }
    protected double categoryOxygen() {
        return OXYGEN;
    }
    @Override
    public double plantPossibility() {
        return PLANT_POSSIBILITY;
    }
}
