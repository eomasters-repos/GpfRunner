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

public class GpfRunnerS1 extends BaseGpfRunner {

  /**
   * Main method for processing a Sentinel-1 product.
   *
   * @param args command line arguments (not used in this implementation)
   * @throws IOException if an I/O error occurs during product reading or writing
   */
  public static void main(String[] args) throws IOException {
    var startTime = initProcessing();

    // Adapt the input and output paths here
    Product inputProduct = loadInputProduct(
        "D:\\EOData\\S1\\S1A_IW_GRDH_1SDV_20230731T181831_20230731T181856_049671_05F90D_88B4.zip");
    String outPath = "D:\\EOData\\S1\\S1A_IW_GRDH_1SDV_20230731T181831_tc.znap.zip";

    //*******************************************
    // Set up the processing chain

    var subsetParams = createParameterMap("sourceBands", "Amplitude_VH,Intensity_VH,Amplitude_VV,Intensity_VV");
    Product result = GPF.createProduct("Terrain-Correction", subsetParams, inputProduct);

    //*******************************************

    // Write the processing result
    writeResult(result, outPath, "ZNAP", WRITE_MODE.GPF);

    endProcessing(startTime);
  }

}
