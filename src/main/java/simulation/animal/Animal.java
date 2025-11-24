package simulation.animal;

import simulation.Cell;
import simulation.Table;
import simulation.air.Air;
import simulation.Entity;
import simulation.plant.Plant;
import simulation.soil.Soil;
import simulation.water.Water;

public abstract class Animal extends Entity {
    private static final double MAX_PERCENTAGE = 100.0;
    private static final double ATTACK_RISK_DIVISOR = 10.0;
    private static final double TOXICITY_THRESHOLD_MULTIPLIER = 0.8;
    private static final int MIN_MOVE_INTERVAL = 2;
    private static final int DIRECTION_COUNT = 4;
    private static final double INITIAL_BEST_WATER_QUALITY = -2.0;
    private static final double FERTILIZER_HALF_MASS_FACTOR = 0.5;
    private static final double WATER_INTAKE_RATE = 0.08;
    private static final double FERTILIZER_FULL_MASS_FACTOR = 0.8;

    protected AnimalState state;
    private boolean scanned;
    protected int lastMoveTimestamp;
    protected int x;
    protected int y;
    private double fertilizerToProduce;
    public Animal(final String name, final double mass) {
        super(name, mass);
        this.state = AnimalState.hungry;
        this.scanned = false;
        this.lastMoveTimestamp = 0;
        this.fertilizerToProduce = 0;
    }

    protected abstract boolean isPredator();

    /**
     * Returneaza posibilitatea specifica tipului de animal.
     */
    public abstract double animalPossibility();

    public final int getX() {
        return x;
    }
    public final int getY() {
        return y;
    }
    public final void setX(final int newX) {
        this.x = newX;
    }
    public final void setY(final int newY) {
        this.y = newY;
    }
    public final boolean isScanned() {
        return scanned;
    }
    public final void setScanned(final boolean scannedStatus) {
        this.scanned = scannedStatus;
    }

    /**
     * Calculeaza posiblitatea de a ataca Terrabot.
     */
    public final double calculateAttackRisk() {
        double score = (MAX_PERCENTAGE - animalPossibility()) / ATTACK_RISK_DIVISOR;
        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE, score));
        return Entity.round(normalizeScore);
    }

    public final void setLastMoveTimestamp(final int timestamp) {
        lastMoveTimestamp = timestamp;
    }

    /**
     * Interactiunea dintre animal si sol: animalul fertilizeaza solul dupa
     * ce se hraneste.
     */
    public final void produceFertilizer(final Soil soil) {
        soil.addOrganicMatter(fertilizerToProduce);
        fertilizerToProduce = 0;
    }

    /**
     * Interactiunea dintre animal si aer: animalul se imbolnaveste daca
     * aerul este toxic.
     */
    public final void updateState(final Air currentAir, final int currentTimestamp) {
        final double toxicThreshold = TOXICITY_THRESHOLD_MULTIPLIER * currentAir.getMaxScore();
        final double toxicity = currentAir.calculateToxicity(currentTimestamp);
        if (toxicity > toxicThreshold) {
            state = AnimalState.sick;
        } else if (state == AnimalState.sick) {
            state = AnimalState.hungry;
        }
    }

    /**
     * Proceseaza miscarea animalului: animalul incearca sa se miste pe
     * patratica mancand animalul de pe acea patratica, daca animalul
     * care se misca este pradator.
     */
    private Cell processMoveInteraction(final Cell targetCell, final int currentTimestamp) {
        tryToFeed(targetCell);
        if (targetCell.getAnimal() == null) {
            lastMoveTimestamp = currentTimestamp;
            return targetCell;
        }
        return null;
    }

    /**
     * Miscarea animalului.
     */
    public final Cell move(final Table map, final int currentX, final int currentY,
                           final int currentTimestamp) {
        if (!scanned || currentTimestamp - lastMoveTimestamp < MIN_MOVE_INTERVAL) {
            return null;
        }

        Cell moveCell = null;
        double bestWaterQuality = INITIAL_BEST_WATER_QUALITY;
        final int[] dx = {0, 1, 0, -1};
        final int[] dy = {1, 0, -1, 0};

        for (int i = 0; i < DIRECTION_COUNT; i++) {
            final int newX = currentX + dx[i];
            final int newY = currentY + dy[i];
            final Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                final Plant plant = candidateCell.getPlant();
                final Water water = candidateCell.getWater();
                final Animal animal = candidateCell.getAnimal();

                if (animal != null && !isPredator()) {
                    continue;
                }

                if (plant != null && water != null && plant.isScanned() && water.isScanned()) {
                    if (candidateCell.getWater().calculateQuality() > bestWaterQuality) {
                        bestWaterQuality = candidateCell.getWater().calculateQuality();
                        moveCell = candidateCell;
                    }
                }
            }
        }

        if (moveCell != null) {
            if (processMoveInteraction(moveCell, currentTimestamp) != null) {
                System.out.println(getName() + " moved to cell "
                        + "(" + moveCell.getX() + ", " + moveCell.getY() + ")"
                        + " at timestamp " + currentTimestamp);
                return moveCell;
            }
        }

        for (int i = 0; i < DIRECTION_COUNT; i++) {
            final int newX = currentX + dx[i];
            final int newY = currentY + dy[i];
            final Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                final Plant plant = candidateCell.getPlant();
                final Animal animal = candidateCell.getAnimal();

                if (animal != null && !isPredator()) {
                    continue;
                }

                if (plant != null && plant.isScanned()) {
                    if (processMoveInteraction(candidateCell, currentTimestamp) != null) {
                        System.out.println(getName() + " moved to cell "
                                + "(" + candidateCell.getX() + ", " + candidateCell.getY() + ")"
                                + " at timestamp " + currentTimestamp);
                        return candidateCell;
                    }
                }
            }
        }
        bestWaterQuality = INITIAL_BEST_WATER_QUALITY;

        for (int i = 0; i < DIRECTION_COUNT; i++) {
            final int newX = currentX + dx[i];
            final int newY = currentY + dy[i];
            final Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                final Water water = candidateCell.getWater();
                final Animal animal = candidateCell.getAnimal();

                if (animal != null && !isPredator()) {
                    continue;
                }
                if (water != null && water.isScanned()
                        && water.calculateQuality() > bestWaterQuality) {
                    bestWaterQuality = candidateCell.getWater().calculateQuality();
                    moveCell = candidateCell;
                }
            }
        }

        if (moveCell != null) {
            if (processMoveInteraction(moveCell, currentTimestamp) != null) {
                System.out.println(getName() + " moved to cell "
                        + "(" + moveCell.getX() + ", " + moveCell.getY() + ")"
                        + " at timestamp " + currentTimestamp);
                return moveCell;
            }
        }

        for (int i = 0; i < DIRECTION_COUNT; i++) {
            final int newX = currentX + dx[i];
            final int newY = currentY + dy[i];
            final Cell candidateCell = map.getCell(newX, newY);
            if (candidateCell != null) {
                final Animal animal = candidateCell.getAnimal();

                if (animal != null && !isPredator()) {
                    continue;
                }

                if (processMoveInteraction(candidateCell, currentTimestamp) != null) {
                    System.out.println(getName() + " moved to cell "
                            + "(" + candidateCell.getX() + ", " + candidateCell.getY() + ")"
                            + " at timestamp " + currentTimestamp);
                    return candidateCell;
                }
            }
        }
        return null;
    }

    /**
     * Functia de hranire a animalului: daca se poate acesta se hraneste
     * cu ce exista pe patratica curenta.
     */
    public final void tryToFeed(final Cell currentCell) {
        if (!scanned) {
            return;
        }
        state = AnimalState.well_fed;
        final Water water = currentCell.getWater();
        final Plant plant = currentCell.getPlant();
        final Animal prey = currentCell.getAnimal();
        final Soil soil = currentCell.getSoil();

        final boolean plantScanned = (plant != null && plant.isScanned());
        final boolean waterScanned = (water != null && water.isScanned());

        if (isPredator() && prey != null && prey != this) {
            setMass(getMass() + prey.getMass());
            fertilizerToProduce = FERTILIZER_HALF_MASS_FACTOR;
            produceFertilizer(soil);
            currentCell.setAnimal(null);
            return;
        }

        if (plantScanned && waterScanned) {
            currentCell.setPlant(null);
            final double waterToDrink = Math.min(getMass() * WATER_INTAKE_RATE, water.getMass());
            water.setMass(water.getMass() - waterToDrink);
            if (water.getMass() <= 0) {
                currentCell.setWater(null);
            }
            setMass(getMass() + waterToDrink + plant.getMass());
            fertilizerToProduce = FERTILIZER_FULL_MASS_FACTOR;
            produceFertilizer(soil);
            return;
        }

        if (plantScanned) {
            currentCell.setPlant(null);
            setMass(getMass() + plant.getMass());
            fertilizerToProduce = FERTILIZER_HALF_MASS_FACTOR;
            produceFertilizer(soil);
            return;
        }

        if (waterScanned) {
            final double waterToDrink = Math.min(getMass() * WATER_INTAKE_RATE, water.getMass());
            water.setMass(water.getMass() - waterToDrink);
            if (water.getMass() <= 0) {
                currentCell.setWater(null);
            }
            setMass(getMass() + waterToDrink);
            fertilizerToProduce = FERTILIZER_HALF_MASS_FACTOR;
            produceFertilizer(soil);
            return;
        }
        state = AnimalState.hungry;
    }
}
