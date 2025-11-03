package simulation.Water;

import simulation.Entity;

public class Water extends Entity {
    public Water(String name, double mass, double salinity, double ph,
                 double purity, double turbidity, double contaminantIndex, boolean isFrozen) {
        super(name, mass);
        scanned = false;
        lastInteractionTimestamp = 0;
        this.salinity = salinity;
        this.ph = ph;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
    }
    protected double salinity;
    protected double ph;
    protected double purity;
    protected double turbidity;
    protected double contaminantIndex;
    protected boolean isFrozen, scanned;
    protected int lastInteractionTimestamp;
}
