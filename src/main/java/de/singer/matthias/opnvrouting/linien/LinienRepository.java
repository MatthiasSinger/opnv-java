package de.singer.matthias.opnvrouting.linien;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LinienRepository implements CommandLineRunner {
    private Map<Integer, Linie> linien = new HashMap<>();

    public Linie getLinie(int i) {
        return linien.get(i);
    }

    /**
     * Funktion des CommandLineRunner-Interface, wird beim Starten des Programms
     * ausgeführt und liest die Linien CSV-Files ein
     *
     * @param args automatisch generiert
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 17; i++) {
            ClassPathResource cpr = new ClassPathResource("data/Linie" + i + ".csv");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(cpr.getInputStream(), Charset.forName("UTF-8")));
            List<String> lines = bufferedReader.lines().collect(Collectors.toList());
            linien.put(i, Linie.of(lines));
        }
    }
}
