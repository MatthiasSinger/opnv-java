package de.singer.matthias.opnvrouting;

import de.singer.matthias.opnvrouting.haltestellen.HaltestellenRepository;
import de.singer.matthias.opnvrouting.routing.Route;
import de.singer.matthias.opnvrouting.routing.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * @author Matthias Singer
 */
@RestController
public class MainController {
    private final HaltestellenRepository haltestelleRepository;
    private final Routing routing;

    @Autowired
    public MainController(HaltestellenRepository haltestelleRepository, Routing routing) {
        this.haltestelleRepository = haltestelleRepository;
        this.routing = routing;
    }

    /**
     * Route für das Laden der Haltestellen
     *
     * @return Haltestellen in Hof
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/haltestellen")
    public Set<String> haltestellen() {
        return haltestelleRepository.getAllNames();
    }

    /**
     * Route für das Ergebnis, benötigt Parameter zur Berechnung der Routen
     * @param start Startzeit aus HTTP Request
     * @param ende Endzeit aus HTTP Request
     * @param uhrzeit Uhrzeit aus HTTP Request
     * @param datum Datum aus HTTP Request
     * @return Liste der fünf besten Routen
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/result")
    public List<Route> result(
            @RequestParam("start") String start,
            @RequestParam("ende") String ende,
            @RequestParam("uhrzeit") String uhrzeit,
            @RequestParam("datum") String datum) {
        LocalDate date = LocalDate.parse(datum);
        LocalTime time = LocalTime.parse(uhrzeit);
        return routing.calculateRoutes(start, ende, date, time);
    }
}
