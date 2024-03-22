package io.github.eoinkanro.commons.utils;

import io.github.eoinkanro.commons.utils.model.OsType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Class with system utils
 */

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemUtils {

    //--------------------- OS --------------------
    private static final String SYSTEM_PARAMETER_OS = "os.name";

    private static final String WINDOWS_OS = "windows";

    private static final String MAC_OS = "macos";
    private static final String OS_X = "os x";

    private static final String OS_NIX = "nix";
    private static final String OS_NUX = "nux";
    private static final String OS_AIX = "aix";
    private static final String OS_RIX = "rix";
    private static final String OS_BSD = "bsd";
    private static final String OS_SUSE = "suse";
    private static final String OS_UBUNTU = "ubuntu";
    private static final String OS_FEDORA = "fedora";
    private static final String OS_DEBIAN = "debian";
    private static final String OS_GNU = "gnu";
    private static final String OS_RASPB = "raspb";
    private static final String OS_SOLARIS = "solaris";
    private static final String OS_HP_UX = "hp-ux";
    private static final String OS_POP = "pop!";
    private static final String OS_MANJARO = "manjaro";


    private static OsType osType = null;

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

    /**
     * Get type of OS {@link OsType}
     *
     * @return os type
     */
    public static OsType getOsType() {
        if (osType == null) {
            String os = System.getProperty(SYSTEM_PARAMETER_OS).toLowerCase();

            if (os.contains(WINDOWS_OS)) {
                osType = OsType.WINDOWS;
            } else if (os.contains(MAC_OS) || os.contains(OS_X)) {
                osType = OsType.MAC;
            } else if (os.contains(OS_NIX) || os.contains(OS_NUX) || os.contains(OS_AIX) || os.contains(OS_RIX)
                       || os.contains(OS_BSD) || os.contains(OS_SUSE) || os.contains(OS_UBUNTU) || os.contains(OS_FEDORA)
                       || os.contains(OS_DEBIAN) || os.contains(OS_GNU) || os.contains(OS_RASPB) || os.contains(OS_SOLARIS)
                       || os.contains(OS_HP_UX) || os.contains(OS_POP) || os.contains(OS_MANJARO)) {
                osType = OsType.UNIX;
            } else {
                osType = OsType.OTHER;
            }
        }

        return osType;
    }

}
