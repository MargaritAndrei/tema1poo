package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;

public final class TundraSoil extends Soil {
    private static final double NITROGEN_QUALITY_WEIGHT = 0.7;
    private static final double ORGANIC_MATTER_QUALITY_WEIGHT = 0.5;
    private static final double PERMAFROST_QUALITY_PENALTY = 1.5;
    private static final double MAX_PERCENTAGE_VALUE = 100.0;

    private static final double PERMAFROST_BASE_BLOCK_VALUE = 50.0;
    private static final double BLOCK_PROBABILITY_DIVISOR = 50.0;
    private static final double BLOCK_PROBABILITY_MULTIPLIER = 100.0;

    private final double permafrostDepth;

    public TundraSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilpH,
                      final double organicMatter, final double permafrostDepth) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.permafrostDepth = Entity.round(permafrostDepth);
    }

    public double getPermafrostDepth() {
        return permafrostDepth;
    }

    /**
     * Calculeaza calitatea solului.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_QUALITY_WEIGHT)
                + (organicMatter * ORGANIC_MATTER_QUALITY_WEIGHT)
                - (permafrostDepth * PERMAFROST_QUALITY_PENALTY);

        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Calculeaza blockProbability.
     */
    @Override
    public double calculateBlockProbability() {
        double score = (PERMAFROST_BASE_BLOCK_VALUE - permafrostDepth)
                / BLOCK_PROBABILITY_DIVISOR * BLOCK_PROBABILITY_MULTIPLIER;

        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Adauga campurile specifice in output-ul json.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("permafrostDepth", getPermafrostDepth());
    }
}
