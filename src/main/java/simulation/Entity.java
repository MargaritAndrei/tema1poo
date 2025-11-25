package simulation;

public abstract class Entity {
    private static final double ROUNDING_FACTOR = 100.0;

    private String name;
    private double mass;

    public Entity(final String name, final double mass) {
        this.name = name;
        this.mass = Entity.round(mass);
    }
    public final String getName() {
        return name;
    }
    public final double getMass() {
        return mass;
    }
    public final void setName(final String name) {
        this.name = name;
    }
    public final void setMass(final double mass) {
        this.mass = Entity.round(mass);
    }

    /**
     * Rounding function: implements Math.round with the parameters specified
     * in the requirements.
     *
     * @param value The value to be rounded.
     * @return The rounded value.
     */
    public static double round(final double value) {
        return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }
}
