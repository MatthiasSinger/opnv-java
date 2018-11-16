package de.singer.matthias.opnvrouting.haltestellen;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HaltestelleRepository extends CrudRepository<Haltestelle, Long> {
}
