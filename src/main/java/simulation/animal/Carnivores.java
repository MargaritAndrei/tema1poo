package simulation.animal;

public final class Carnivores extends Animal {
    private static final double CARNIVORE_POSSIBILITY_SCORE = 30.0;
    public Carnivores(final String name, final double mass) {
        super(name, mass);
    }
    @Override
    protected boolean isPredator() {
        return true;
    }
    @Override
    public double animalPossibility() {
        return CARNIVORE_POSSIBILITY_SCORE;
    }
}
