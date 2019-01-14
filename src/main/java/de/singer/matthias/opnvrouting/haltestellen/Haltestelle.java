package de.singer.matthias.opnvrouting.haltestellen;

import lombok.Data;

import java.util.List;

@Data
public class Haltestelle {
    private String name;
    private List<Integer> linien;
}
