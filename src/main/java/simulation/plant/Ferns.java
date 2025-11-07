package simulation.plant;

public class Ferns extends Plant{
    public Ferns(String name, double mass) {
        super(name, mass);
    }
    @Override
    protected double categoryOxygen() {
        return 0;
    }
    @Override
    public double plantPossibility() {
        return 30;
    }
}
