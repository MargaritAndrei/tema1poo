package simulation.animal;

public final class Parasites extends Animal {
    private static final double PARASITE_POSSIBILITY_SCORE = 10.0;
    public Parasites(final String name, final double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return true;
    }
    @Override
    public double animalPossibility() {
        return PARASITE_POSSIBILITY_SCORE;
    }
}
