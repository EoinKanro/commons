package io.github.eoinkanro.commons.utils;

import io.github.eoinkanro.commons.utils.model.OsType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SystemUtilsTest {

    private static final String SYSTEM_PARAMETER_OS = "os.name";
    private static final String CURRENT_OS = System.getProperty(SYSTEM_PARAMETER_OS);

    @ParameterizedTest
    @MethodSource("provide_getOsType_returnWindows")
    void getOsType_ok(String osName, OsType expected) {
        clearSavedOs();

        System.setProperty(SYSTEM_PARAMETER_OS, osName);
        assertEquals(expected, SystemUtils.getOsType());
        System.setProperty(SYSTEM_PARAMETER_OS, CURRENT_OS);
    }

    @SneakyThrows
    private void clearSavedOs() {
        Field field = SystemUtils.class.getDeclaredField("osType");
        field.setAccessible(true);
        field.set(null, null);
        field.setAccessible(false);
    }

    static Stream<Arguments> provide_getOsType_returnWindows() {
        return Stream.of(
                Arguments.of("Windows 11", OsType.WINDOWS),
                Arguments.of("Windows 8.1", OsType.WINDOWS),
                Arguments.of("Windows 7", OsType.WINDOWS),
                Arguments.of("Windows Vista", OsType.WINDOWS),
                Arguments.of("Windows Server", OsType.WINDOWS),
                Arguments.of("macOS Monterey", OsType.MAC),
                Arguments.of("macOS Big Sur", OsType.MAC),
                Arguments.of("OS X El Capitan", OsType.MAC),
                Arguments.of("OS X Yosemite", OsType.MAC),
                Arguments.of("Mac OS X Leopard", OsType.MAC),
                Arguments.of("Mac OS X Tiger", OsType.MAC),
                Arguments.of("Ubuntu", OsType.UNIX),
                Arguments.of("Debian", OsType.UNIX),
                Arguments.of("Fedora", OsType.UNIX),
                Arguments.of("Red Hat Enterprise Linux", OsType.UNIX),
                Arguments.of("Arch Linux", OsType.UNIX),
                Arguments.of("Linux Mint", OsType.UNIX),
                Arguments.of("openSUSE", OsType.UNIX),
                Arguments.of("SUSE Linux Enterprise Server", OsType.UNIX),
                Arguments.of("Kali Linux", OsType.UNIX),
                Arguments.of("Alpine Linux", OsType.UNIX),
                Arguments.of("Void Linux", OsType.UNIX),
                Arguments.of("FreeBSD", OsType.UNIX),
                Arguments.of("OpenBSD", OsType.UNIX),
                Arguments.of("NetBSD", OsType.UNIX),
                Arguments.of("DragonFly BSD", OsType.UNIX),
                Arguments.of("MINIX 3", OsType.UNIX),
                Arguments.of("FreeNIX", OsType.UNIX),
                Arguments.of("NixOS", OsType.UNIX),
                Arguments.of("Raspberry Pi OS", OsType.UNIX),
                Arguments.of("GNU", OsType.UNIX),
                Arguments.of("Oracle Solaris", OsType.UNIX),
                Arguments.of("OpenSolaris", OsType.UNIX),
                Arguments.of("AIX 7.2", OsType.UNIX),
                Arguments.of("AIX 5L", OsType.UNIX),
                Arguments.of("Ultrix", OsType.UNIX),
                Arguments.of("IRIX", OsType.UNIX),
                Arguments.of("HP-UX", OsType.UNIX),
                Arguments.of("Tru64 UNIX", OsType.UNIX),
                Arguments.of("Pop!_OS", OsType.UNIX),
                Arguments.of("Manjaro", OsType.UNIX),
                Arguments.of("Android Oreo", OsType.OTHER),
                Arguments.of("Android 11", OsType.OTHER),
                Arguments.of("Android 12", OsType.OTHER),
                Arguments.of("IOS 11", OsType.OTHER),
                Arguments.of("OpenVMS", OsType.OTHER),
                Arguments.of("Zorin OS", OsType.OTHER),
                Arguments.of("OS/2", OsType.OTHER),
                Arguments.of("VxWorks", OsType.OTHER),
                Arguments.of("μC/OS", OsType.OTHER)
        );
    }

    @Test
    void getOsType_callTwiceChangeProperty_returnFirst() {
        clearSavedOs();

        System.setProperty(SYSTEM_PARAMETER_OS, "randomString");
        OsType first = SystemUtils.getOsType();

        System.setProperty(SYSTEM_PARAMETER_OS, "Windows 11");
        assertEquals(first, SystemUtils.getOsType());
    }

    @Test
    @SneakyThrows
    void sleep_interrupt_ok() {
        int tenSeconds = 10000;
        int oneSecond =  1000;

        var thread = new Thread(() -> SystemUtils.sleep(tenSeconds));
        thread.start();

        Thread.sleep(oneSecond);
        thread.interrupt();

        Thread.sleep(oneSecond);
        assertTrue(thread.isInterrupted());
        assertFalse(thread.isAlive());
    }

}
