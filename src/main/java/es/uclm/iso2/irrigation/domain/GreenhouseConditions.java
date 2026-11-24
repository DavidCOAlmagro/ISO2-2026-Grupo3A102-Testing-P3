package es.uclm.iso2.irrigation.domain;

import java.time.LocalTime;

/**
 * Conditions of the greenhouse at one moment, and the irrigation recommendation for them
 * (statement ET.02.04 and decisions Q1–Q8 of the README).
 *
 * <p>The data are validated in the constructor, so an object of this class is always valid.
 */
public class GreenhouseConditions {

    /** Lowest valid soil moisture, in % (Q1). */
    public static final double MIN_MOISTURE = 0;
    /** Highest valid soil moisture, in % (Q1). */
    public static final double MAX_MOISTURE = 100;

    /** Below this moisture (%), peak hours give Critical Watering. */
    public static final double CRITICAL_MOISTURE = 10;
    /** Below this moisture (%), Heavy Watering; from it (included, Q6), Light Watering. */
    public static final double HEAVY_WATERING_MOISTURE = 20;
    /** Up to this moisture (%, included, Q6), Light Watering; below it, Do Not Water if rain is forecast. */
    public static final double LIGHT_WATERING_MOISTURE = 40;
    /** Above this moisture (%), Emergency Drainage if it is cold. */
    public static final double DRAINAGE_MOISTURE = 60;

    /** Below this temperature (°C), Emergency Drainage if the soil is wet. */
    public static final double COLD_TEMPERATURE = 5;
    /** Above this temperature (°C), Heavy or Light Watering. */
    public static final double HOT_TEMPERATURE = 25;

    /** Start of the peak hours, included (Q2). */
    public static final LocalTime PEAK_START = LocalTime.of(12, 0);
    /** End of the peak hours, not included (Q2). */
    public static final LocalTime PEAK_END = LocalTime.of(16, 0);

    private final double soilMoisture;
    private final double temperature;
    private final boolean rainForecast;
    private final LocalTime timeOfDay;

    /**
     * Creates the conditions of the greenhouse and checks that they are valid (Q1).
     *
     * @param soilMoisture soil moisture in %, from 0 to 100 (both included)
     * @param temperature  temperature in °C, any finite number
     * @param rainForecast {@code true} if rain is forecast in the next 24 hours (Q8)
     * @param timeOfDay    time of day; it cannot be {@code null}
     * @throws InvalidReadingException if the moisture is not a number or is outside 0–100,
     *                                 if the temperature is not a finite number,
     *                                 or if the time is {@code null}
     */
    public GreenhouseConditions(double soilMoisture, double temperature, boolean rainForecast,
                                LocalTime timeOfDay) {
        if (Double.isNaN(soilMoisture) || soilMoisture < MIN_MOISTURE || soilMoisture > MAX_MOISTURE) {
            throw new InvalidReadingException(
                    "Soil moisture must be from " + MIN_MOISTURE + " to " + MAX_MOISTURE + " %: " + soilMoisture);
        }
        if (!Double.isFinite(temperature)) {
            throw new InvalidReadingException("Temperature must be a finite number: " + temperature);
        }
        if (timeOfDay == null) {
            throw new InvalidReadingException("Time of day is required");
        }
        this.soilMoisture = soilMoisture;
        this.temperature = temperature;
        this.rainForecast = rainForecast;
        this.timeOfDay = timeOfDay;
    }

    /**
     * Returns the irrigation recommendation for these conditions.
     *
     * <p>The rules are checked from top to bottom and the first one that matches wins (Q3),
     * as in the decision table of the README:
     * <ol>
     *   <li>moisture &gt; 60 and temperature &lt; 5: Emergency Drainage</li>
     *   <li>peak hours and moisture &lt; 10: Critical Watering (Q4, Q5)</li>
     *   <li>rain forecast and moisture &lt; 40: Do Not Water</li>
     *   <li>no rain, temperature &gt; 25 and moisture &le; 40: a watering, which is
     *     <ol>
     *       <li>Postpone Watering in peak hours</li>
     *       <li>Heavy Watering if moisture &lt; 20</li>
     *       <li>Light Watering otherwise (20 &le; moisture &le; 40, Q6)</li>
     *     </ol>
     *   </li>
     *   <li>otherwise: No Action (Q7)</li>
     * </ol>
     * Peak hours go from 12:00 (included) to 16:00 (not included) (Q2).
     *
     * @return the recommendation; never {@code null}
     */
    public Recommendation recommend() {
        boolean peakHours = !timeOfDay.isBefore(PEAK_START) && timeOfDay.isBefore(PEAK_END);

        if (soilMoisture > DRAINAGE_MOISTURE && temperature < COLD_TEMPERATURE) {
            return Recommendation.EMERGENCY_DRAINAGE;
        }
        if (peakHours && soilMoisture < CRITICAL_MOISTURE) {
            return Recommendation.CRITICAL_WATERING;
        }
        if (rainForecast && soilMoisture < LIGHT_WATERING_MOISTURE) {
            return Recommendation.DO_NOT_WATER;
        }
        if (!rainForecast && temperature > HOT_TEMPERATURE && soilMoisture <= LIGHT_WATERING_MOISTURE) {
            if (peakHours) {
                return Recommendation.POSTPONE_WATERING;
            }
            if (soilMoisture < HEAVY_WATERING_MOISTURE) {
                return Recommendation.HEAVY_WATERING;
            }
            return Recommendation.LIGHT_WATERING;
        }
        return Recommendation.NO_ACTION;
    }
}
