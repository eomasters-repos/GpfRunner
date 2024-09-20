/*-
 * ========================LICENSE_START=================================
 * GPF Runner - Project Description
 * -> https://www.eomasters.org/
 * ======================================================================
 * Copyright (C) 2024 Marco Peters
 * ======================================================================
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * -> http://www.gnu.org/licenses/gpl-3.0.html
 * =========================LICENSE_END==================================
 */

package org.eomasters;

import static java.time.Instant.now;

import eu.esa.snap.hdf.HdfActivator;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import org.eomasters.WriteTools.WRITE_FUNC;
import org.esa.snap.core.dataio.ProductIO;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.GPF;
import org.esa.snap.core.gpf.main.GPT;
import org.esa.snap.core.util.SystemUtils;
import org.esa.snap.dataio.netcdf.NetCdfActivator;

public class GpfRunnerS1 {

  /**
   * The main method serves as the entry point for the application, processing a satellite product based on provided
   * parameters.
   *
   * @param args Command-line arguments where: args[0] is the input product file path, args[1] is the output product
   *             file path, args[2] (optional) is the path to a properties file containing processing parameters.
   * @throws IOException if an input or output operation fails.
   */
  public static void main(String[] args) throws IOException {
    Instant start = now();
    System.out.println("**** Starting Processing: " + start.atZone(java.time.ZoneId.systemDefault()));

    initSnapAndGpf();

    // Adapt the input and output paths here
    Product inputProduct = loadInputProduct("D:\\EOData\\S1\\S1A_IW_GRDH_1SDV_20230731T181831_20230731T181856_049671_05F90D_88B4.zip");
    Path outPath = Path.of("D:\\EOData\\S1\\S1A_IW_GRDH_1SDV_20230731T181831_tc.znap.zip");

    //*******************************************
    // Set up the processing chain

    Map<String, Object> subsetParams = Map.of( "sourceBands", "Amplitude_VH,Intensity_VH,Amplitude_VV,Intensity_VV");
    Product result = GPF.createProduct("Terrain-Correction", subsetParams, inputProduct);

    //*******************************************

    // Write the processing result
    WriteTools.writeResult(result, outPath, "ZNAP", WRITE_FUNC.GPF);

    Instant end = now();
    System.out.println("**** Stopped Processing: " + end.atZone(java.time.ZoneId.systemDefault()));
    System.out.println("**** Duration: " + format(Duration.between(start, end)));
  }

  /**
   * Loads a Product from the given file path.
   *
   * @param filePath The path to the file from which to load the Product.
   * @return The loaded Product object.
   * @throws IOException if the file cannot be read or does not contain a valid Product.
   */
  private static Product loadInputProduct(String filePath) throws IOException {
    Product inputProduct = ProductIO.readProduct(filePath);
    if (inputProduct == null) {
      throw new IOException("Could not read input product: " + filePath);
    }
    return inputProduct;
  }

  /**
   * Initializes the SNAP and GPT libraries for processing. Sets up the necessary system properties, default locale, and
   * activates required third-party libraries for HDF and NetCDF operations.
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
   * Formats a given {@link Duration} object into a string representation in the format of "H:MM:SS".
   *
   * @param duration The {@link Duration} object to be formatted.
   * @return A string representing the formatted duration in the "H:MM:SS" format.
   */
  private static String format(Duration duration) {
    return String.format("%d:%02d:%02d",
        duration.toHours(),
        duration.toMinutesPart(),
        duration.toSecondsPart());

  }
}
