package com.example.my_auction_rest.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    @Column(updatable = false)
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal initCost;
    private boolean active;
    private boolean archival;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "lot", orphanRemoval = true)
    private List<Bet> bets = new ArrayList<>();

    public Lot() {
    }

    public Lot(String title, String description, LocalDateTime startDate, LocalDateTime endDate, BigDecimal initCost, boolean active, boolean archival, List<Bet> bets) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.initCost = initCost;
        this.active = active;
        this.archival = archival;
        this.bets = bets;
    }

    public Lot(Long id, String title, String description, LocalDateTime startDate, LocalDateTime endDate, BigDecimal initCost, boolean active, boolean archival, User user, List<Bet> bets) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.initCost = initCost;
        this.active = active;
        this.archival = archival;
        this.user = user;
        this.bets = bets;
    }
}
