package es.uclm.iso2.irrigation.ui;

import es.uclm.iso2.irrigation.domain.GreenhouseConditions;
import es.uclm.iso2.irrigation.domain.InvalidReadingException;
import es.uclm.iso2.irrigation.domain.Recommendation;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Scanner;

/**
 * Command-line interface of the irrigation advisor (task T3).
 *
 * <p>It only reads the data from the keyboard, builds a {@link GreenhouseConditions} and prints
 * its {@link Recommendation}. It has no irrigation rules and does not check the ranges of the
 * data: the domain does that in its constructor (Q1 of the README), and the console shows the
 * reason when the domain rejects the data.
 *
 * <p>The input and the output are received in the constructor, so the console can also be
 * used with streams other than the keyboard and the screen.
 */
public class IrrigationConsole {

    /**
     * Format of the time of day: hours (0–23) and minutes, for example {@code 9:30} or {@code 14:05}.
     * The strict resolver rejects {@code 24:00} instead of turning it into {@code 00:00}.
     */
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("H:mm").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner in;
    private final PrintStream out;

    /**
     * Creates the console.
     *
     * @param in  where the data are read from, for example {@code System.in}
     * @param out where the questions and the result are written, for example {@code System.out}
     */
    public IrrigationConsole(InputStream in, PrintStream out) {
        this.in = new Scanner(in, StandardCharsets.UTF_8);
        this.out = out;
    }

    /**
     * Asks for the conditions of the greenhouse and prints the recommendation.
     *
     * <p>If a value cannot be read (it is not a number, not y/n or not a time), the console
     * explains why and asks for that value again. If the domain rejects the data, the console
     * prints the reason and asks for the four values again. It handles one set of conditions
     * per run.
     *
     * @return the recommendation that was printed, or {@code null} if the input ended before
     *         all the data were entered
     */
    public Recommendation run() {
        out.println("Irrigation advisor");
        while (true) {
            Double moisture = readNumber("Soil moisture (%): ");
            if (moisture == null) {
                return endOfInput();
            }
            Double temperature = readNumber("Temperature (Celsius): ");
            if (temperature == null) {
                return endOfInput();
            }
            Boolean rain = readYesNo("Rain forecast in the next 24 hours? (y/n): ");
            if (rain == null) {
                return endOfInput();
            }
            LocalTime time = readTime("Time of day (HH:mm): ");
            if (time == null) {
                return endOfInput();
            }
            try {
                GreenhouseConditions conditions = new GreenhouseConditions(moisture, temperature, rain, time);
                Recommendation recommendation = conditions.recommend();
                out.println("Recommendation: " + recommendation.getLabel());
                return recommendation;
            } catch (InvalidReadingException e) {
                out.println("Invalid data: " + e.getMessage());
                out.println("Please enter the data again.");
            }
        }
    }

    /**
     * Starts the console on the keyboard and the screen.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        new IrrigationConsole(System.in, System.out).run();
    }

    /** Tells the user that the input ended before all the data were entered. */
    private Recommendation endOfInput() {
        out.println("No more input: the program ends without a recommendation.");
        return null;
    }

    /** Reads one line, or returns {@code null} when the input has ended. */
    private String readLine(String prompt) {
        out.print(prompt);
        if (!in.hasNextLine()) {
            out.println();
            return null;
        }
        return in.nextLine().trim();
    }

    /** Reads a decimal number written with a dot, asking again until it is one. */
    private Double readNumber(String prompt) {
        while (true) {
            String text = readLine(prompt);
            if (text == null) {
                return null;
            }
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                out.println("'" + text + "' is not a number. Write it with a dot, for example 7.5.");
            }
        }
    }

    /** Reads y/yes or n/no (any case), asking again until it is one of them. */
    private Boolean readYesNo(String prompt) {
        while (true) {
            String text = readLine(prompt);
            if (text == null) {
                return null;
            }
            switch (text.toLowerCase(Locale.ROOT)) {
                case "y", "yes" -> {
                    return Boolean.TRUE;
                }
                case "n", "no" -> {
                    return Boolean.FALSE;
                }
                default -> out.println("Answer y or n.");
            }
        }
    }

    /** Reads a time of day in HH:mm (24 h), asking again until it is one. */
    private LocalTime readTime(String prompt) {
        while (true) {
            String text = readLine(prompt);
            if (text == null) {
                return null;
            }
            try {
                return LocalTime.parse(text, TIME_FORMAT);
            } catch (DateTimeParseException e) {
                out.println("'" + text + "' is not a time. Use HH:mm from 00:00 to 23:59, for example 14:30.");
            }
        }
    }
}
