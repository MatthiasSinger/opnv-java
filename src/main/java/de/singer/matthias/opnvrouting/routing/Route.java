package de.singer.matthias.opnvrouting.routing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Maximilian May
 */
@Data
@AllArgsConstructor
public class Route implements Comparable<Route> {
    private String startHst;
    private String endHst;
    private LocalDateTime startZeit;
    private LocalDateTime endZeit;
    private List<Teilstrecke> teilstrecken;

    @Override
    public int compareTo(Route o) {
        return this.endZeit.compareTo(o.endZeit);
    }
}
