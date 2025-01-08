/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2025 Vladimir Orany.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package builders.dsl.spreadsheet.builder.poi;

import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder;
import builders.dsl.spreadsheet.builder.api.WorkbookDefinition;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

public class PoiSpreadsheetBuilder implements SpreadsheetBuilder {

    @FunctionalInterface
    public interface WorkbookSupplier {
        Workbook get() throws Exception;
    }

    public static SpreadsheetBuilder create(OutputStream out) {
        return new PoiSpreadsheetBuilder(XSSFWorkbook::new, out, true);
    }

    public static SpreadsheetBuilder create(File file) throws FileNotFoundException {
        return new PoiSpreadsheetBuilder(XSSFWorkbook::new, new FileOutputStream(file), true);
    }

    public static SpreadsheetBuilder create(OutputStream out, InputStream template) {
        return new PoiSpreadsheetBuilder(() -> new XSSFWorkbook(template), out, true);
    }

    public static SpreadsheetBuilder create(File file, InputStream template) throws IOException {
        return new PoiSpreadsheetBuilder(() -> new XSSFWorkbook(template), new FileOutputStream(file), true);
    }

    public static SpreadsheetBuilder create(OutputStream out, File template) {
        return new PoiSpreadsheetBuilder(() -> new XSSFWorkbook(template), out, true);
    }

    public static SpreadsheetBuilder create(File file, File template) throws IOException {
        return new PoiSpreadsheetBuilder(() -> new XSSFWorkbook(template), new FileOutputStream(file), true);
    }

    public static SpreadsheetBuilder prepare(Workbook workbook) {
        return new PoiSpreadsheetBuilder(() -> workbook, null, false);
    }

    public static SpreadsheetBuilder stream(OutputStream out) {
        return new PoiSpreadsheetBuilder(SXSSFWorkbook::new, out, true);
    }

    public static SpreadsheetBuilder stream(File file) throws FileNotFoundException {
        return new PoiSpreadsheetBuilder(SXSSFWorkbook::new, new FileOutputStream(file), true);
    }

    public static SpreadsheetBuilder stream(OutputStream out, InputStream template) {
        return new PoiSpreadsheetBuilder(() -> new SXSSFWorkbook(new XSSFWorkbook(template)), out, true);
    }

    public static SpreadsheetBuilder stream(File file, InputStream template) throws IOException {
        return new PoiSpreadsheetBuilder(() -> new SXSSFWorkbook(new XSSFWorkbook(template)), new FileOutputStream(file), true);
    }

    public static SpreadsheetBuilder stream(OutputStream out, File template) {
        return new PoiSpreadsheetBuilder(() -> new SXSSFWorkbook(new XSSFWorkbook(template)), out, true);
    }

    public static SpreadsheetBuilder stream(File file, File template) throws IOException {
        return new PoiSpreadsheetBuilder(() -> new SXSSFWorkbook(new XSSFWorkbook(template)), new FileOutputStream(file), true);
    }

    private final WorkbookSupplier workbookSupplier;
    private final OutputStream outputStream;
    private final boolean closeWorkbook;

    private PoiSpreadsheetBuilder(WorkbookSupplier workbookSupplier, OutputStream outputStream, boolean closeWorkbook) {
        this.workbookSupplier = workbookSupplier;
        this.outputStream = outputStream;
        this.closeWorkbook = closeWorkbook;
    }

    @Override
    public void build(Consumer<WorkbookDefinition> workbookDefinition) {
        Workbook workbook = null;
        try {
             workbook = workbookSupplier.get();
            PoiWorkbookDefinition poiWorkbook = new PoiWorkbookDefinition(workbook);
            workbookDefinition.accept(poiWorkbook);
            poiWorkbook.resolve();
            if (outputStream != null) {
                writeTo(workbook, outputStream);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Exception building workbook", e);
        } finally {
            if (closeWorkbook && workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    System.getLogger(PoiSpreadsheetBuilder.class.getName()).log(System.Logger.Level.ERROR, "Exception closing workbook", e);
                }
            }
        }
    }

    private void writeTo(Workbook workbook, OutputStream outputStream) {
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {
                    // do nothing
                }
            }
        }
    }

}
