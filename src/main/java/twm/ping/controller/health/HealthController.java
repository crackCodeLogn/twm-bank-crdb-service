package twm.ping.controller.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vivek
 * @since 07/12/20
 */
@Slf4j
@RestController("HealthController")
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/ping")
    String ping() {
        String pingResult = "ALIVE-" + System.currentTimeMillis();
        log.info("PINGING back with status {}", pingResult);
        return pingResult;
    }
}