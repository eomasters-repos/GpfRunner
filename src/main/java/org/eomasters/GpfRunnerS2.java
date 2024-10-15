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

import java.io.IOException;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.GPF;

public class GpfRunnerS2 extends BaseGpfRunner {

  /**
   * Main method for resampling a Sentinel-2 product to 20m.
   *
   * @param args command line arguments (not used in this implementation)
   * @throws IOException if an I/O error occurs during product reading or writing
   */
  public static void main(String[] args) throws IOException {
    var startTime = initProcessing();

    String resolution = "20";

    // Adapt the input and output paths here
    String filePath = "D:\\EOData\\_temp\\S2B_MSIL2A_20240515T101559_N0510_R065_T32UPE_20240515T144033.SAFE.zip";
    String outPath = "D:\\EOData\\_temp\\S2B_MSIL2A_20240515T101559_resampled_" + resolution + "m.znap.zip";

    //*******************************************
    // Set up the processing chain

    Product l2aProduct = loadInputProduct(filePath);
    Product resultProduct = GPF.createProduct("Resample",
                                              createParameterMap("targetResolution", resolution),
                                              l2aProduct);

    //*******************************************

    // Write the processing result
    writeResult(resultProduct, outPath, "ZNAP", WRITE_MODE.GPF);

    endProcessing(startTime);
  }
}
