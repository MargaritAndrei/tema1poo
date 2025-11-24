package simulation.animal;

public final class Omnivores extends Animal {
    private static final double OMNIVORE_POSSIBILITY_SCORE = 60.0;
    public Omnivores(final String name, final double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return false;
    }
    @Override
    public double animalPossibility() {
        return OMNIVORE_POSSIBILITY_SCORE;
    }
}
