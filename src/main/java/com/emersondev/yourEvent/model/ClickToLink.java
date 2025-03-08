package com.emersondev.yourEvent.model;

import jakarta.persistence.*;

import java.security.Timestamp;

@Entity
@Table(name = "tbl_clicks_to_link")
public class ClickToLink {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "click_id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "owner_link")
    private User ownerLink;
    
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event eventId;
    
    @Column(name = "click_date")
    private Timestamp clickDAte;

    public Timestamp getClickDAte() {
        return clickDAte;
    }

    public void setClickDAte(Timestamp clickDAte) {
        this.clickDAte = clickDAte;
    }

    public Event getEventId() {
        return eventId;
    }

    public void setEventId(Event eventId) {
        this.eventId = eventId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getOwnerLink() {
        return ownerLink;
    }

    public void setOwnerLink(User ownerLink) {
        this.ownerLink = ownerLink;
    }
}
