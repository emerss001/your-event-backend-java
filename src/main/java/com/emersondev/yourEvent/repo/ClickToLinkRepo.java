package com.emersondev.yourEvent.repo;

import com.emersondev.yourEvent.model.ClickToLink;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClickToLinkRepo extends CrudRepository<ClickToLink, Integer> {
    
    @Query(value = "select count(*) as quantidade "
                    + "from tbl_clicks_to_link "
                    + "where event_id = :event_id and owner_link = :owner_id", nativeQuery = true)
    public int findByQuantityClicks(@Param("event_id") Integer event_id, @Param("owner_id") Integer owner_id);
}
