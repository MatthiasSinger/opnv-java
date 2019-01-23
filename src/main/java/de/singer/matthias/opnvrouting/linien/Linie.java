package de.singer.matthias.opnvrouting.linien;

import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Linie {
    private List<Stop> stops;

    private Linie(List<Stop> stops) {
        this.stops = stops;
    }

    /**
     * Erstellt ein Listenobjekt aus einer Liste von Zeilen
     *
     * @param lines Zeilen einer CSV-Datei
     * @return Linie als Objekt
     */
    public static Linie of(List<String> lines) {
        List<Stop> stops = new ArrayList<>();
        String[] split = lines.get(0).split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = strip(split[i]);
        }
        for (int i = 1; i < lines.size(); i++) {
            List<String> parts = Arrays.asList(lines.get(i).split(","));
            String name = parts.get(0).replaceAll("\\s+$", "");
            List<String> wochentags = parts.subList(1, Integer.parseInt(split[1]));
            List<String> samstags;
            List<String> sonntags;
            if (split.length == 2) {
                samstags = parts.subList(Integer.parseInt(split[1]), parts.size());
                sonntags = Collections.emptyList();
            } else {
                samstags = parts.subList(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                sonntags = parts.subList(Integer.parseInt(split[2]), parts.size());
            }
            List<LocalTime> wochentagsZeiten = wochentags.stream().map(Linie::parseTime).collect(Collectors.toList());
            List<LocalTime> samstagsZeiten = samstags.stream().map(Linie::parseTime).collect(Collectors.toList());
            List<LocalTime> sonntagsZeiten = sonntags.stream().map(Linie::parseTime).collect(Collectors.toList());
            stops.add(new Stop(name, wochentagsZeiten, samstagsZeiten, sonntagsZeiten));
        }
        return new Linie(stops);
    }

    private static LocalTime parseTime(String input) {
        if (strip(input).isEmpty()) return null;
        String[] split = input.split("\\.");
        for (int i = 0; i < split.length; i++) {
            split[i] = strip(split[i]);
        }
        if (split.length == 2) {
            return LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        } else {
            return LocalTime.of(Integer.parseInt(split[0]), 0);
        }
    }

    private static String strip(String s) {
        return s.replaceAll("\\s+", "");
    }
}
