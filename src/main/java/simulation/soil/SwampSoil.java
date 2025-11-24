package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;

public final class SwampSoil extends Soil {
    private static final double NITROGEN_QUALITY_WEIGHT = 1.1;
    private static final double ORGANIC_MATTER_QUALITY_WEIGHT = 2.2;
    private static final double WATER_LOGGING_QUALITY_PENALTY = 5.0;
    private static final double WATER_LOGGING_BLOCK_MULTIPLIER = 10.0;
    private static final double MAX_PERCENTAGE_VALUE = 100.0;

    private final double waterLogging;

    public SwampSoil(final String name, final double mass, final double nitrogen,
                     final double waterRetention, final double soilpH,
                     final double organicMatter, final double waterLogging) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.waterLogging = Entity.round(waterLogging);
    }

    public double getWaterLogging() {
        return waterLogging;
    }

    /**
     * Calculeaza calitatea solului.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_QUALITY_WEIGHT)
                + (organicMatter * ORGANIC_MATTER_QUALITY_WEIGHT)
                - (waterLogging * WATER_LOGGING_QUALITY_PENALTY);
        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Calculeaza blockProbability.
     */
    @Override
    public double calculateBlockProbability() {
        double score = waterLogging * WATER_LOGGING_BLOCK_MULTIPLIER;

        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Adds the specific field 'waterLogging' to the JSON object node.
     * @param node The JSON object node to modify.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("waterLogging", getWaterLogging());
    }
}
