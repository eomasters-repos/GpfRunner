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

public class GpfRunnerS3 extends BaseGpfRunner {

  /**
   * Main method for processing a Sentinel-3 OLCI product using the c2rcc.olci algorithm.
   *
   * @param args command line arguments (not used in this implementation)
   * @throws IOException if an I/O error occurs during product reading or writing.
   */
  public static void main(String[] args) throws IOException {
    var start = initProcessing();

    // Adapt the input and output paths here
    String inputPath = "D:\\EOData\\S3\\OLCI\\S3B_OL_1_EFR____20230717T104859_20230717T105159_20230717T232729_0180_081_379_2160_PS2_O_NT_003.SEN3";
    String outputPath = "D:\\EOData\\_temp\\S3B_OL_1_EFR_20230717T104859_c2rcc_.znap.zip";

    //*******************************************
    // Set up the processing chain

    Product inputProduct = loadInputProduct(inputPath);
    Product resultProduct = GPF.createProduct("c2rcc.olci",
                                              createParameterMap("outputUncertainties", false),
                                              inputProduct);

    //*******************************************

    // Write the processing result
    writeResult(resultProduct, outputPath, "ZNAP", WRITE_MODE.GPF);

    endProcessing(start);
  }

}
