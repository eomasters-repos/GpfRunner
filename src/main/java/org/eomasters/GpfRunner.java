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

public class GpfRunner extends BaseGpfRunner {

  /**
   * The main method serves as the entry point for the processing. It shows how to extract a region from a product and
   * resample it to a desired width and height.
   *
   * @param args command line arguments (not used in this implementation)
   * @throws IOException if an input or output operation fails.
   */
  public static void main(String[] args) throws IOException {
    var startTime = initProcessing();

    // Adapt the input and output paths here
    Product inputProduct = loadInputProduct("Path to your input product");
    String outPath = "path to your output product";

    //*******************************************
    // Set up the processing chain

    var subsetParams = createParameterMap("region", "10,10,1000,1000");
    Product intermediate1 = GPF.createProduct("Subset", subsetParams, inputProduct);

    var resampleParams = createParameterMap("targetWidth", 500, "targetHeight", 500);
    Product result = GPF.createProduct("Resample", resampleParams, intermediate1);

    //*******************************************

    // Write the processing result
    writeResult(result, outPath, "ZNAP", WRITE_MODE.GPF);

    endProcessing(startTime);
  }

}
