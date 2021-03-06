package com.sgone.capstone.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "trip_assignment")
public class TripAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonIgnoreProperties({"tripAssignments"})
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "application_user_id")
    @JsonIgnoreProperties({})
    private ApplicationUser applicationUser;

    public TripAssignment() {}

    public TripAssignment(Long id,
                          Trip trip,
                          ApplicationUser applicationUser) {
        this.id = id;
        this.trip = trip;
        this.applicationUser = applicationUser;
    }

    public TripAssignment(Trip trip,
                          ApplicationUser applicationUser) {
        this.trip = trip;
        this.applicationUser = applicationUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }
}
