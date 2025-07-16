package org.hl7.fhir.utilities;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

  public static final String OSX = "OS X";
  public static final String MAC = "MAC";
  public static final String WINDOWS = "WINDOWS";
  public static final String LINUX = "Linux";

  public static final String TEST_TXT = "test.txt";

  public static final String LINUX_TEMP_DIR = "/tmp/";
  public static final String LINUX_USER_DIR = System.getProperty("user.home") + "/";
  public static final String LINUX_JAVA_HOME = System.getenv("JAVA_HOME") + "/";

  public static final String WIN_TEMP_DIR = "c:\\temp\\";
  public static final String WIN_USER_DIR = System.getProperty("user.home") + "\\";
  public static final String WIN_JAVA_HOME = System.getenv("JAVA_HOME") + "\\";

  public static final String OSX_USER_DIR = System.getProperty("user.home") + "/";
  public static final String OSX_JAVA_HOME = System.getenv("JAVA_HOME") + "/";
  @Disabled
  @Test
  @DisplayName("Test Utilities.path maps temp directory correctly")
  public void testTempDirPath() throws IOException {
    assertEquals(Utilities.path("[tmp]", TEST_TXT), ToolGlobalSettings.getTempPath() +File.separator+ TEST_TXT);
  }
  @Disabled

  @Test
  @DisplayName("Test Utilities.path maps user directory correctly")
  public void testUserDirPath() throws IOException {
    assertEquals(Utilities.path("[tmp]", TEST_TXT), ToolGlobalSettings.getTempPath() +File.separator+ TEST_TXT);
  }
  @Disabled

  @Test
  @DisplayName("Test Utilities.path maps JAVA_HOME correctly")
  public void testJavaHomeDirPath() throws IOException {
    assertEquals(Utilities.path("[tmp]", TEST_TXT), ToolGlobalSettings.getTempPath() +File.separator+ TEST_TXT);
  }

  private String getJavaHomeDirectory() {
    String os = SystemUtils.OS_NAME;
    if (os.contains(OSX) || os.contains(MAC)) {
      return OSX_JAVA_HOME;
    } else if (os.contains(LINUX)) {
      return LINUX_JAVA_HOME;
    } else if (os.toUpperCase().contains(WINDOWS)) {
      return WIN_JAVA_HOME;
    } else {
      throw new IllegalStateException("OS not recognized...cannot verify created directories.");
    }
  }

  private String getUserDirectory() {
    String os = SystemUtils.OS_NAME;
    if (os.contains(OSX) || os.contains(MAC)) {
      return OSX_USER_DIR;
    } else if (os.contains(LINUX)) {
      return LINUX_USER_DIR;
    } else if (os.toUpperCase().contains(WINDOWS)) {
      return WIN_USER_DIR;
    } else {
      throw new IllegalStateException("OS not recognized...cannot verify created directories.");
    }
  }

  private String getTempDirectory() throws IOException {
    String os = SystemUtils.OS_NAME;
    if (os.contains(OSX) || os.contains(MAC)) {
      return getOsxTempDir();
    } else if (os.contains(LINUX)) {
      return LINUX_TEMP_DIR;
    } else if (os.toUpperCase().contains(WINDOWS)) {
      File tmp = new File("c:\\temp");
      if(tmp.exists()) {
        return WIN_TEMP_DIR;
      } else {
        return System.getProperty("java.io.tmpdir");
      }
    } else {
      throw new IllegalStateException("OS not recognized...cannot verify created directories.");
    }
  }

  /**
   * Getting the temporary directory in OSX is a little different from Linux and Windows. We need to create a temporary
   * file and then extract the directory path from it.
   *
   * @return Full path to tmp directory on OSX machines.
   * @throws IOException
   */
  public static String getOsxTempDir() throws IOException {
    File file = File.createTempFile("throwaway", ".file");
    return file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('/')) + '/';
  }

  public static final int BOUND = 500;
  public static final Random RAND = new Random();

  public static final int GB_MEASURE_JUST_OVER = (int) Math.pow(Utilities.ONE_MB, 3) + RAND.nextInt(BOUND);
  public static final int GB_MEASURE_EXACT = (int) Math.pow(Utilities.ONE_MB, 3);
  public static final int GB_MEASURE_JUST_UNDER = (int) Math.pow(Utilities.ONE_MB, 3) - RAND.nextInt(BOUND);

  public static final int MB_MEASURE_JUST_OVER = (int) Math.pow(Utilities.ONE_MB, 2) + RAND.nextInt(BOUND);
  public static final int MB_MEASURE_EXACT = (int) Math.pow(Utilities.ONE_MB, 2);
  public static final int MB_MEASURE_JUST_UNDER = (int) Math.pow(Utilities.ONE_MB, 2) - RAND.nextInt(BOUND);

  public static final int KB_MEASURE_JUST_OVER = Utilities.ONE_MB + RAND.nextInt(BOUND);
  public static final int KB_MEASURE_EXACT = Utilities.ONE_MB;
  public static final int KB_MEASURE_JUST_UNDER = Utilities.ONE_MB - RAND.nextInt(BOUND);

  public static final int BT_MEASURE = Utilities.ONE_MB + RAND.nextInt(BOUND);
  public static final int EMPTY = 0;

  public static final int BIG_NEG = Utilities.ONE_MB * -1;

  @Test
  @DisplayName("Test size bounds on file size utility.")
  void describeSizeTest() {
    Assertions.assertAll("GB Measure Limits",
      () -> assertTrue(Utilities.describeSize(GB_MEASURE_JUST_OVER).contains(Utilities.GB)),
      () -> assertTrue(Utilities.describeSize(GB_MEASURE_EXACT).contains(Utilities.MB)),
      () -> assertTrue(Utilities.describeSize(GB_MEASURE_JUST_UNDER).contains(Utilities.MB))
    );
    Assertions.assertAll("MB Measure Limits",
      () -> assertTrue(Utilities.describeSize(MB_MEASURE_JUST_OVER).contains(Utilities.MB)),
      () -> assertTrue(Utilities.describeSize(MB_MEASURE_EXACT).contains(Utilities.KB)),
      () -> assertTrue(Utilities.describeSize(MB_MEASURE_JUST_UNDER).contains(Utilities.KB))
    );
    Assertions.assertAll("KB Measure Limits",
      () -> assertTrue(Utilities.describeSize(KB_MEASURE_JUST_OVER).contains(Utilities.KB)),
      () -> assertTrue(Utilities.describeSize(KB_MEASURE_EXACT).contains(Utilities.BT)),
      () -> assertTrue(Utilities.describeSize(KB_MEASURE_JUST_UNDER).contains(Utilities.BT))
    );
    Assertions.assertAll("BT Measure Limits",
      () -> assertTrue(Utilities.describeSize(BT_MEASURE).contains(Utilities.BT)),
      () -> assertTrue(Utilities.describeSize(EMPTY).contains(Utilities.BT))
    );
    Assertions.assertThrows(IllegalArgumentException.class, () -> Utilities.describeSize(BIG_NEG));
  }

  public static Stream<Arguments> windowsRootPaths() {
    return Stream.of(
      Arguments.of((Object)new String[]{"C:"}),
      Arguments.of((Object)new String[]{"D:"}),
      Arguments.of((Object)new String[]{"C:", "anything"}),
      Arguments.of((Object)new String[]{"D:", "anything"}),
      Arguments.of((Object)new String[]{"C:/", "anything"}),
      Arguments.of((Object)new String[]{"C:/.", "anything"}),
      Arguments.of((Object)new String[]{"C:\\"}),
      Arguments.of((Object)new String[]{"D:\\"}),
      Arguments.of((Object)new String[]{"C:/child/.."}),
      Arguments.of((Object)new String[]{"C:/child/..", "anything"}),
      Arguments.of((Object)new String[]{"C:/child/../child/.."}),
      Arguments.of((Object)new String[]{"C:/child/../child/..", "anything"}),
      Arguments.of((Object)new String[]{"C:/child/second/../.."}),
      Arguments.of((Object)new String[]{"C:/child/second/../..", "anything"}),
      Arguments.of((Object)new String[]{"C:\\child\\.."}),
      Arguments.of((Object)new String[]{"C:\\child\\..", "anything"}),
      Arguments.of((Object)new String[]{"C:\\child\\..\\child/.."}),
      Arguments.of((Object)new String[]{"C:\\child\\..\\child\\..", "anything"}),
      Arguments.of((Object)new String[]{"C:\\child\\second\\..\\.."}),
      Arguments.of((Object)new String[]{"C:\\child\\second\\..\\..", "anything"})
    );
  }
  @ParameterizedTest
  @MethodSource("windowsRootPaths")
  @EnabledOnOs({OS.WINDOWS})
  public void testPathCantStartWithRootWindows(String[] pathStrings) {
    testCantStartWithRoot(pathStrings);
  }

  public static Stream<Arguments> macAndLinuxRootPaths() {
    return Stream.of(
      Arguments.of((Object)new String[]{"/"}),
      Arguments.of((Object)new String[]{"/", "anything"}),
      Arguments.of((Object)new String[]{"//"}),
      Arguments.of((Object)new String[]{"//", "anything"}),
      Arguments.of((Object)new String[]{"//child/.."}),
      Arguments.of((Object)new String[]{"//child/..", "anything"}),
      Arguments.of((Object)new String[]{"//child/../child/.."}),
      Arguments.of((Object)new String[]{"//child/../child/..", "anything"}),
      Arguments.of((Object)new String[]{"//child/second/../.."}),
      Arguments.of((Object)new String[]{"//child/second/../..", "anything"})
    );
  }
  @ParameterizedTest
  @MethodSource("macAndLinuxRootPaths")
  @EnabledOnOs({OS.MAC, OS.LINUX})
  public void testPathCantStartWithRootMacAndLinux(String[] pathStrings) {
    testCantStartWithRoot(pathStrings);
  }

  private static void testCantStartWithRoot(String[] pathStrings) {
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
      Utilities.path(pathStrings);
    });
    assertTrue(thrown.getMessage().endsWith(pathStrings[0]));
  }

  public static Stream<Arguments> macAndLinuxNonFirstElementStartPaths() {
    return Stream.of(
      Arguments.of((Object)new String[]{"/root", ".."}),
      Arguments.of((Object)new String[]{"/root", "child/../.."}),
      Arguments.of((Object)new String[]{"/root", "child", "/../.."}),
      Arguments.of((Object)new String[]{"/root", "child", "../.."}),
      Arguments.of((Object)new String[]{"/root/a", "../.."}),
      Arguments.of((Object)new String[]{"/root/a", "child/../.."}),
      Arguments.of((Object)new String[]{"/root/a", "child", "/../../.."}),
      Arguments.of((Object)new String[]{"/root/a", "child", "../../.."})
    );
  }

  @ParameterizedTest
  @MethodSource("macAndLinuxNonFirstElementStartPaths")
  @EnabledOnOs({OS.MAC, OS.LINUX})
  public void testPathMustStartWithFirstElementMacAndLinux(String[] pathStrings) {
    testPathMustStartWithFirstElement(pathStrings);
  }

  private static void testPathMustStartWithFirstElement(String[] pathStrings) {
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
      Utilities.path(pathStrings);
    });
    assertTrue(thrown.getMessage().startsWith("Computed path does not start with first element: " + pathStrings[0]));
  }

  public static Stream<Arguments> macAndLinuxValidPaths() {
    return Stream.of(
      Arguments.of((Object) new String[]{"/root"}, "/root"),
      Arguments.of( (Object) new String[]{"/root", "child"}, "/root/child"),
      Arguments.of((Object) new String[]{"/root", "../root/child"}, "/root/child"),
      Arguments.of((Object) new String[]{"/root", "child", "anotherchild"}, "/root/child/anotherchild")
    );
  }

  @ParameterizedTest
  @MethodSource("macAndLinuxValidPaths")
  @EnabledOnOs({OS.MAC, OS.LINUX})
  public void testValidPathsMacAndLinux(String[] pathStrings, String expectedPath) throws IOException {
    testValidPath(pathStrings,expectedPath);
  }

  public static Stream<Arguments> windowsValidPaths() {
    return Stream.of(
      Arguments.of((Object) new String[]{"C://root"}, "C:\\\\root"),
      Arguments.of( (Object) new String[]{"C://root", "child"}, "C:\\\\root\\child"),
      Arguments.of((Object) new String[]{"C://root", "../root/child"}, "C:\\\\root\\child"),
      Arguments.of((Object) new String[]{"C://root", "child", "anotherchild"}, "C:\\\\root\\child\\anotherchild"),
      Arguments.of((Object) new String[]{"C:\\\\root"}, "C:\\\\root"),
      Arguments.of( (Object) new String[]{"C:\\\\root", "child"}, "C:\\\\root\\child"),
      Arguments.of((Object) new String[]{"C:\\\\root", "..\\root\\child"}, "C:\\\\root\\child"),
      Arguments.of((Object) new String[]{"C:\\\\root", "child", "anotherchild"}, "C:\\\\root\\child\\anotherchild")
    );
  }

  @ParameterizedTest
  @MethodSource("windowsValidPaths")
  @EnabledOnOs({OS.WINDOWS})
  public void testValidPathsWindows(String[] pathStrings, String expectedPath) throws IOException {
    testValidPath(pathStrings,expectedPath);
  }

  private static void testValidPath(String[] pathsStrings, String expectedPath) throws IOException {
    String actualPath = Utilities.path(pathsStrings);
    assertEquals(expectedPath, actualPath);
  }

  public static Stream<Arguments> nullOrEmptyFirstEntryPaths() {
    return Stream.of(
      Arguments.of((Object)new String[]{null, "child"}),
      Arguments.of((Object)new String[]{null, "child/otherchild"}),
      Arguments.of((Object)new String[]{null, "child", "otherchild"}),
      Arguments.of((Object)new String[]{"", "child"}),
      Arguments.of((Object)new String[]{"", "child/otherchild"}),
      Arguments.of((Object)new String[]{"", "child", "otherchild"}),
      Arguments.of((Object)new String[]{"  ", "child"}),
      Arguments.of((Object)new String[]{"  ", "child/otherchild"}),
      Arguments.of((Object)new String[]{"  ", "child", "otherchild"})
    );
  }

  @ParameterizedTest
  @MethodSource("nullOrEmptyFirstEntryPaths")
  public void testNullOrEmptyFirstPathEntryFails(String[] pathsStrings) {
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
      Utilities.path(pathsStrings);
    });
    assertEquals("First entry cannot be null or empty",thrown.getMessage());
  }
}