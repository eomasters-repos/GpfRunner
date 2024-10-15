package org.eomasters;

import static java.time.Instant.now;

import com.bc.ceres.core.PrintWriterProgressMonitor;
import eu.esa.snap.hdf.HdfActivator;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.esa.snap.core.dataio.ProductIO;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.GPF;
import org.esa.snap.core.gpf.main.GPT;
import org.esa.snap.core.util.SystemUtils;
import org.esa.snap.dataio.netcdf.NetCdfActivator;

public abstract class BaseGpfRunner {

  protected enum WRITE_MODE {GPF, PIO}


  /**
   * Initializes the processing environment by setting up necessary configurations and recording the start time.
   *
   * @return The Instant representing the start time of the processing.
   */
  protected static Instant initProcessing() {
    Instant start = now();
    System.out.println("**** Starting Processing: " + start.atZone(java.time.ZoneId.systemDefault()));
    initSnapAndGpf();
    return start;
  }

  /**
   * Ends the processing by calculating and displaying the duration since the given start time.
   *
   * @param start The Instant representing the start time of the processing.
   */
  protected static void endProcessing(Instant start) {
    Instant end = now();
    System.out.println("Duration: " + format(Duration.between(start, end)));
    System.out.println("**** Stopped Processing: " + end.atZone(java.time.ZoneId.systemDefault()));
  }

  /**
   * Loads a Product from the given file path.
   *
   * @param filePath The path to the file from which to load the Product.
   * @return The loaded Product object.
   * @throws IOException if the file cannot be read or does not contain a valid Product.
   */
  protected static Product loadInputProduct(String filePath) throws IOException {
    Product inputProduct = ProductIO.readProduct(filePath);
    if (inputProduct == null) {
      throw new IOException("Could not read input product: " + filePath);
    }
    return inputProduct;
  }

  /**
   * Writes the result product to the specified output path in the given format using the provided write function.
   *
   * @param product    The product to be written.
   * @param outPath    The path where the product should be written.
   * @param formatName The format in which the product should be written.
   * @param writeFunc  The write function to use for writing the product.
   * @throws IOException If an I/O error occurs during writing.
   */
  protected static void writeResult(Product product, String outPath, String formatName, WRITE_MODE writeFunc)
      throws IOException {
    switch (writeFunc) {
      case GPF:
        gpfWriteResult(product, Path.of(outPath), formatName);
        break;
      case PIO:
        pioWriteResult(product, Path.of(outPath), formatName);
        break;
      default:
        throw new IllegalStateException("Unsupported write function : " + writeFunc);
    }
  }

  /**
   * Creates a parameter map from an array of name-value pairs.
   *
   * @param nameValueList An array where even indices are parameter names (as Strings) and odd indices are parameter
   *                      values (as Objects).
   * @return A map containing the name-value pairs as key-value pairs.
   * @throws IllegalArgumentException if the input array length is not even.
   */
  protected static Map<String, Object> createParameterMap(Object... nameValueList) {
    if ((nameValueList.length % 2) != 0) {
      throw new IllegalArgumentException(
          "Number of name-values elements must be even. Always a name (String) and value (Object) is required.");
    }
    HashMap<String, Object> paramMap = new HashMap<>();
    for (int i = 0; i < nameValueList.length; i += 2) {
      String name = (String) nameValueList[i];
      Object value = nameValueList[i + 1];
      paramMap.put(name, value);
    }
    return paramMap;
  }

  /**
   * Writes the result product to the specified output path in the given format using ProductIO.
   *
   * @param writeProduct The product to be written.
   * @param outPath      The path where the product should be written.
   * @param formatName   The format in which the product should be written.
   * @throws IOException If an I/O error occurs during writing.
   */
  private static void pioWriteResult(Product writeProduct, Path outPath, String formatName) throws IOException {
    ProductIO.writeProduct(writeProduct, outPath.toFile(), formatName, false,
                           new PrintWriterProgressMonitor(System.out));
  }

  /**
   * Writes the result product to the specified output path in the given  format using GPF.
   *
   * @param writeProduct The product to be written.
   * @param outPath      The path where the product should be written.
   * @param formatName   The format in which the product should be written.
   * @throws IOException If an I/O error occurs during writing.
   */
  private static void gpfWriteResult(Product writeProduct, Path outPath, String formatName) throws IOException {
    GPF.writeProduct(writeProduct, outPath.toFile(), formatName, true, false,
                     new PrintWriterProgressMonitor(System.out));
  }

  /**
   * Initializes the SNAP and GPF processing environment by setting necessary system properties, forcing the English
   * locale, and activating necessary libraries.
   *
   * <ol>
   * <li>Sets the "snap.context" system property to "snap" if it is not already defined.</li>
   * <li>Forces usage of the English locale by setting it as the default locale.</li>
   * <li>Initializes third-party libraries needed for GPT.</li>
   * <li>Activates HDF and NetCDF support by calling their respective activators.</li>
   * </ol>
   */
  private static void initSnapAndGpf() {
    if (System.getProperty("snap.context") == null) {
      System.setProperty("snap.context", "snap");
    }
    Locale.setDefault(Locale.ENGLISH); // Force usage of english locale
    SystemUtils.init3rdPartyLibs(GPT.class);
    HdfActivator.activate();
    NetCdfActivator.activate();
  }

  /**
   * Formats a Duration object into a string representation in the format "hours:minutes:seconds".
   *
   * @param duration The Duration object to be formatted.
   * @return A string representing the formatted duration.
   */
  private static String format(Duration duration) {
    return String.format("%d:%02d:%02d",
                         duration.toHours(),
                         duration.toMinutesPart(),
                         duration.toSecondsPart());

  }
}
