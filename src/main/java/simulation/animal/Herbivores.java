package simulation.animal;
public final class Herbivores extends Animal {
    private static final double HERBIVORE_POSSIBILITY_SCORE = 85.0;
    public Herbivores(final String name, final double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return false;
    }
    @Override
    public double animalPossibility() {
        return HERBIVORE_POSSIBILITY_SCORE;
    }
}
