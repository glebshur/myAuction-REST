package com.example.my_auction_rest.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Lot lot;
    @Column(nullable = false)
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime createdDate;
    private boolean archival;

    public Bet() {
    }

    public Bet(Long id, Lot lot, Long userId, BigDecimal amount, LocalDateTime createdDate, boolean archival) {
        this.id = id;
        this.lot = lot;
        this.userId = userId;
        this.amount = amount;
        this.createdDate = createdDate;
        this.archival = archival;
    }
}
