package de.singer.matthias.opnvrouting.routing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Teilstrecke {
    private String startHst;
    private String endHst;
    private LocalDateTime startZeit;
    private LocalDateTime endZeit;
    private String linie;
}
