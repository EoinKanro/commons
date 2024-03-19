package io.github.eoinkanro.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Class with system utils
 * It exists mostly for coverage... :c
 */

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemUtils {

    /**
     * Sleep current thread
     *
     * @param sleepMs millis to sleep
     */
    public static void sleep(long sleepMs) {
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
            log.error("Interrupt error", e);
            Thread.currentThread().interrupt();
        }
    }
}
