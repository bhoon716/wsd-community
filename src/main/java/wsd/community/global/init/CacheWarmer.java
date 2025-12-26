package wsd.community.global.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import wsd.community.domain.stats.service.StatsService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheWarmer implements ApplicationRunner {

    private final StatsService statsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting Cache Warming...");
        long start = System.currentTimeMillis();

        try {
            // Warm top writers cache (limit=10, days=30)
            statsService.getTopWriters(10, 30);
            log.info("Warmed Top Writers cache");

            // Warm top commenters cache (limit=10, days=30)
            statsService.getTopCommenters(10, 30);
            log.info("Warmed Top Commenters cache");

        } catch (Exception e) {
            log.error("Failed to warm cache", e);
        }

        long end = System.currentTimeMillis();
        log.info("Cache Warming completed in {} ms", end - start);
    }
}
