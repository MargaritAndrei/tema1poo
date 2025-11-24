package simulation.animal;
public final class Detritivores extends Animal {
    private static final double DETRITIVORE_POSSIBILITY_SCORE = 90.0;
    public Detritivores(final String name, final double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return false;
    }
    @Override
    public double animalPossibility() {
        return DETRITIVORE_POSSIBILITY_SCORE;
    }
}
