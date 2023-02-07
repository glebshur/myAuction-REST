package com.example.my_auction_rest.jobs;

import com.example.my_auction_rest.entity.Bet;
import com.example.my_auction_rest.entity.Lot;
import com.example.my_auction_rest.entity.WinningBet;
import com.example.my_auction_rest.service.LotService;
import com.example.my_auction_rest.service.WinningBetService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// Job that checks completion of lots
@Component
public class LotCompletionCheckJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(LotCompletionCheckJob.class);

    @Autowired
    LotService lotService;
    @Autowired
    WinningBetService winningBetService;

    public LotCompletionCheckJob() {
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Lot> activeLots = lotService.getAllLots(true);

        LocalDateTime currTime = LocalDateTime.now();

        List<Lot> modifiedLots = new ArrayList<>();
        List<WinningBet> winningBets = new ArrayList<>();

        for (Lot lot : activeLots) {
            // Check if lot is complete
            if(currTime.isAfter(lot.getEndDate())){
                lot.setActive(false);
                modifiedLots.add(lot);

                // Find winning bet
                Optional<Bet> maxBet = lot.getBets().stream()
                        .filter(bet -> !bet.isArchival())
                        .max(Comparator.comparing(Bet::getAmount));

                if(maxBet.isPresent()){
                    WinningBet winningBet = new WinningBet();
                    winningBet.setBet(maxBet.get());
                    winningBet.setCreatedDate(currTime);

                    winningBets.add(winningBet);
                }
            }
        }

        lotService.saveLots(modifiedLots);
        winningBetService.saveWinningBets(winningBets);

        LOG.debug("Check Job is performed");
    }
}
