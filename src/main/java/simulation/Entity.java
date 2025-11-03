package simulation;

public abstract class Entity {
    private String name;
    private double mass;
    public Entity(String name, double mass) {
        this.name = name;
        this.mass = mass;
    }
    public String getName() {
        return name;
    }
    public double getMass() {
        return mass;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMass(double mass) {
        this.mass = mass;
    }
}
