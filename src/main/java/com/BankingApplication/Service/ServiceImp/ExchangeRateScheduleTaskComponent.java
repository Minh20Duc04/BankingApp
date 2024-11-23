package com.BankingApplication.Service.ServiceImp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ExchangeRateScheduleTaskComponent implements CommandLineRunner { //lập lịch tự động sau 12h để lấy tỉ giá chuyển đổi

    private final ExchangeRateService rateService;
    private final ScheduledExecutorService scheduler;
    private final Logger logger = LoggerFactory.getLogger(ExchangeRateScheduleTaskComponent.class);

    public ExchangeRateScheduleTaskComponent(ExchangeRateService rateService, ScheduledExecutorService scheduler) {
        this.rateService = rateService;
        this.scheduler = scheduler;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Calling The Currency API endpoint for exchange rate");
        scheduler.scheduleAtFixedRate(rateService::getExchangeRates, 0, 12, TimeUnit.HOURS);
        logger.info("Ended Calling The Currency API endpoint");
    }
}
