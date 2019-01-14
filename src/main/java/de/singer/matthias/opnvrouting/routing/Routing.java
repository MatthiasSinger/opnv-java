package de.singer.matthias.opnvrouting.routing;

import de.singer.matthias.opnvrouting.haltestellen.HaltestellenRepository;
import de.singer.matthias.opnvrouting.linien.Linie;
import de.singer.matthias.opnvrouting.linien.LinienRepository;
import de.singer.matthias.opnvrouting.linien.Stop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class Routing {
    private HaltestellenRepository haltestellenRepository;
    private LinienRepository linienRepository;

    @Autowired
    public Routing(HaltestellenRepository haltestellenRepository, LinienRepository linienRepository) {
        this.haltestellenRepository = haltestellenRepository;
        this.linienRepository = linienRepository;
    }

    public List<Route> calculateRoutes(String start, String ende, LocalDate date, LocalTime time) {
        List<Route> routes = new ArrayList<>();
        List<Integer> startLinien = haltestellenRepository.getLinien(start);
        List<Integer> endLinien = haltestellenRepository.getLinien(ende);
        for (int startLinie : startLinien) {
            for (int endLinie : endLinien) {
                routes.addAll(calculateRoutesForLine(start, ende, date, time, startLinie, endLinie));
            }
        }
        Collections.sort(routes);
        int endIndex = routes.size() < 5 ? routes.size() : 5;
        return routes.subList(0, endIndex);
    }

    private List<Route> calculateRoutesForLine(String start, String ende, LocalDate date, LocalTime time, int startLinie, int endLinie) {
        List<List<Teilstrecke>> teilstreckenList = new ArrayList<>();
        if (startLinie == endLinie) {
            List<Teilstrecke> teilstrecken = calculateTeilstrecke(start, ende, date, time, startLinie);
            if (!teilstrecken.isEmpty()) {
                for (Teilstrecke teilstrecke : teilstrecken) {
                    teilstreckenList.add(Collections.singletonList(teilstrecke));
                }
            }
        } else {
            Linie startL = linienRepository.getLinie(startLinie);
            Stop startStop = null;
            Stop endStop = null;
            for (Stop stop : startL.getStops()) {
                if (stop.getName().equals(start)) {
                    startStop = stop;
                } else if (startStop != null && haltestellenRepository.getLinien(stop.getName()).contains(endLinie)) {
                    endStop = stop;
                }
            }
            if (startStop != null && endStop != null) {
                List<Teilstrecke> teilstrecken1 = calculateTeilstrecke(start, endStop.getName(), date, time, startLinie);
                List<Teilstrecke> teilstrecken2;
                if (!teilstrecken1.isEmpty()) {
                    teilstrecken2 = calculateTeilstrecke(teilstrecken1.get(0).getEndHst(), ende, date,
                            teilstrecken1.get(0).getEndZeit().toLocalTime(), endLinie);
                    if (!teilstrecken2.isEmpty()) {
                        int min = Math.min(teilstrecken1.size(), teilstrecken2.size());
                        for (int i = 0; i < min; i++) {
                            teilstreckenList.add(List.of(teilstrecken1.get(i), teilstrecken2.get(i)));
                        }
                    }
                }
            }
        }
        List<Route> result = new ArrayList<>();
        for (List<Teilstrecke> teilstrecken : teilstreckenList) {
            result.add(new Route(start, ende, teilstrecken.get(0).getStartZeit(),
                    teilstrecken.get(teilstrecken.size() - 1).getEndZeit(), teilstrecken));
        }
        return result;
    }

    private List<Teilstrecke> calculateTeilstrecke(String start, String ende, LocalDate date, LocalTime time, int linieNr) {
        List<Teilstrecke> teilstrecken = new ArrayList<>();
        Linie linie = linienRepository.getLinie(linieNr);
        Stop startStop = null;
        Stop endStop = null;
        for (Stop stop : linie.getStops()) {
            if (stop.getName().equals(start)) {
                startStop = stop;
            } else if (stop.getName().equals(ende) && startStop != null) {
                endStop = stop;
                break;
            }
        }

        if (startStop != null && endStop != null) {
            int min = Math.min(startStop.getWochentags().size(), endStop.getWochentags().size());
            for (int i = 0; i < min; i++) {
                LocalTime currentStart = startStop.getWochentags().get(i);
                LocalTime currentEnd = endStop.getWochentags().get(i);
                if (currentStart != null && currentEnd != null && currentStart.isAfter(time)) {
                    LocalDateTime startAtDate = LocalDateTime.of(date, currentStart);
                    LocalDateTime endAtDate = LocalDateTime.of(date, currentEnd);
                    teilstrecken.add(new Teilstrecke(start, ende, startAtDate, endAtDate, "Linie " + linieNr));
                }
            }
        }
        return teilstrecken;
    }
}
