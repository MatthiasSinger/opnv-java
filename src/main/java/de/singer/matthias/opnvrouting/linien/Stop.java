package de.singer.matthias.opnvrouting.linien;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class Stop {
    private String name;
    private List<LocalTime> wochentags;
    private List<LocalTime> samstags;
    private List<LocalTime> sonntags;
}
