package es.uclm.iso2.irrigation.domain;

/**
 * Recommendations that the irrigation advisor can give (statement ET.02.04 and Q7 of the README).
 */
public enum Recommendation {

    /** Moisture above 60 % and temperature below 5 °C. */
    EMERGENCY_DRAINAGE("Emergency Drainage"),

    /** Peak hours and moisture below 10 % (Q4). */
    CRITICAL_WATERING("Critical Watering"),

    /** Rain forecast and moisture below 40 %. */
    DO_NOT_WATER("Do Not Water"),

    /** No rain, temperature above 25 °C and moisture below 20 %, outside peak hours. */
    HEAVY_WATERING("Heavy Watering"),

    /** No rain, temperature above 25 °C and moisture from 20 % to 40 % (Q6), outside peak hours. */
    LIGHT_WATERING("Light Watering"),

    /** Heavy or Light Watering that falls in peak hours (Q7). */
    POSTPONE_WATERING("Postpone Watering"),

    /** No rule applies (Q7). */
    NO_ACTION("No Action");

    private final String label;

    Recommendation(String label) {
        this.label = label;
    }

    /**
     * Returns the name of the recommendation: the one of the statement, or our own name
     * for Postpone Watering and No Action (Q7).
     *
     * @return the text to show to the user, for example {@code "Heavy Watering"}
     */
    public String getLabel() {
        return label;
    }
}
