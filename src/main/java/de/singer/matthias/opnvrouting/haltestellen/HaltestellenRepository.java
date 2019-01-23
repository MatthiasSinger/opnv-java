package de.singer.matthias.opnvrouting.haltestellen;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class HaltestellenRepository implements CommandLineRunner {
    private Map<String, List<Integer>> haltestellen = new HashMap<>();

    /**
     * Gibt ein sortiertes Set der eingelesenen Haltestellen zurück
     *
     * @return sortierte Haltestellen
     */
    public Set<String> getAllNames() {
        return new TreeSet<>(haltestellen.keySet());
    }

    public List<Integer> getLinien(String name) {
        List<Integer> linien = haltestellen.get(name);
        if (linien == null)
            throw new RuntimeException("Haltestelle nicht vorhanden");
        return linien;
    }

    /**
     * Funktion des CommandLineRunner-Interface, wird beim Starten des Programms
     * ausgeführt und liest die haltestellen.txt ein
     * @param args automatisch generiert
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        ClassPathResource cpr = new ClassPathResource("data/haltestellen.txt");
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(cpr.getInputStream(), Charset.forName("UTF-8")));
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        for (String s : lines) {
            List<String> parts = Arrays.asList(s.split(","));
            String name = parts.get(0);
            List<String> linienStrings = parts.subList(1, parts.size());
            List<Integer> linien = linienStrings.stream().map(Integer::parseInt).sorted().collect(Collectors.toList());
            haltestellen.put(name, linien);
        }
    }
}
