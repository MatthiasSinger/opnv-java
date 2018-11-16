package de.singer.matthias.opnvrouting;

import de.singer.matthias.opnvrouting.haltestellen.Haltestelle;
import de.singer.matthias.opnvrouting.haltestellen.HaltestelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpnvRoutingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpnvRoutingApplication.class, args);
    }
}
