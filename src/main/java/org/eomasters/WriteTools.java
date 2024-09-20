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

import com.bc.ceres.core.PrintWriterProgressMonitor;
import java.io.IOException;
import java.nio.file.Path;
import org.esa.snap.core.dataio.ProductIO;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.GPF;

/**
 * Utility class providing methods to write products in different formats using various writing functions.
 */
class WriteTools {

  enum WRITE_FUNC {GPF, PIO}

  private WriteTools() {
  }

  /**
   * Writes the result product to the specified output path in the given format using the provided write function.
   *
   * @param resultProduct2 The product to be written.
   * @param outPath        The path where the product should be written.
   * @param formatName     The format in which the product should be written.
   * @param writeFunc      The write function to use for writing the product.
   * @throws IOException If an I/O error occurs during writing.
   */
  public static void writeResult(Product resultProduct2, Path outPath, String formatName, WRITE_FUNC writeFunc)
      throws IOException {
    switch (writeFunc) {
      case GPF:
        gpfWriteResult(resultProduct2, outPath, formatName);
        break;
      case PIO:
        pioWriteResult(resultProduct2, outPath, formatName);
        break;
      default:
        throw new IllegalStateException("Unsupported write function : " + writeFunc);
    }
    System.out.println("**** Result written to: " + outPath.toAbsolutePath());
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
}
