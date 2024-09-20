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

import com.bc.ceres.core.PrintWriterProgressMonitor;
import eu.esa.snap.hdf.HdfActivator;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import org.esa.snap.core.dataio.ProductIO;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.GPF;
import org.esa.snap.core.gpf.main.GPT;
import org.esa.snap.core.util.SystemUtils;
import org.esa.snap.dataio.netcdf.NetCdfActivator;

public class GpfRunnerS3 {

  public static void main(String[] args) throws IOException {
    Instant start = now();
    System.out.println("**** Starting Processing: " + start.atZone(java.time.ZoneId.systemDefault()));
    initSnapAndGpf();

    String inputPath = "D:\\EOData\\S3\\OLCI\\S3B_OL_1_EFR____20230717T104859_20230717T105159_20230717T232729_0180_081_379_2160_PS2_O_NT_003.SEN3";
    String outputPath = "D:\\EOData\\_temp\\S3B_OL_1_EFR_20230717T104859_c2rcc_.znap.zip";

    Product inputProduct = ProductIO.readProduct(inputPath);
    Product resultProduct = GPF.createProduct("c2rcc.olci", Map.of("outputUncertainties", false), inputProduct);
    // ProductIO.writeProduct(resultProduct, outputPath, "ZNAP", new PrintWriterProgressMonitor(System.out));
    GPF.writeProduct(resultProduct, new File(outputPath), "ZNAP", true, false, new PrintWriterProgressMonitor(System.out));
    Instant end = now();
    System.out.println("Duration: " + format(Duration.between(start, end)));
    System.out.println("**** Stopped Processing: " + end.atZone(java.time.ZoneId.systemDefault()));
  }

  private static void initSnapAndGpf() {
    if (System.getProperty("snap.context") == null) {
      System.setProperty("snap.context", "snap");
    }
    Locale.setDefault(Locale.ENGLISH); // Force usage of english locale
    SystemUtils.init3rdPartyLibs(GPT.class);
    HdfActivator.activate();
    NetCdfActivator.activate();
  }

  private static String format(Duration duration) {
    return String.format("%d:%02d:%02d",
        duration.toHours(),
        duration.toMinutesPart(),
        duration.toSecondsPart());

  }
}
