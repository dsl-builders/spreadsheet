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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.function.Consumer;

public class PoiSpreadsheetBuilder implements SpreadsheetBuilder {

    public static SpreadsheetBuilder create(OutputStream out) {
        return new PoiSpreadsheetBuilder(new XSSFWorkbook(), out);
    }

    public static SpreadsheetBuilder create(File file) throws FileNotFoundException {
        return new PoiSpreadsheetBuilder(new XSSFWorkbook(), new FileOutputStream(file));
    }

    public static SpreadsheetBuilder create(OutputStream out, InputStream template) throws IOException {
        return new PoiSpreadsheetBuilder(new XSSFWorkbook(template), out);
    }

    public static SpreadsheetBuilder create(File file, InputStream template) throws IOException {
        return new PoiSpreadsheetBuilder(new XSSFWorkbook(template), new FileOutputStream(file));
    }

    public static SpreadsheetBuilder create(OutputStream out, File template) throws IOException, InvalidFormatException {
        return new PoiSpreadsheetBuilder(new XSSFWorkbook(template), out);
    }

    public static SpreadsheetBuilder create(File file, File template) throws IOException, InvalidFormatException {
        return new PoiSpreadsheetBuilder(new XSSFWorkbook(template), new FileOutputStream(file));
    }

    public static SpreadsheetBuilder prepare(Workbook workbook) {
        return new PoiSpreadsheetBuilder(workbook, null);
    }

    public static SpreadsheetBuilder stream(OutputStream out) {
        return new PoiSpreadsheetBuilder(new SXSSFWorkbook(), out);
    }

    public static SpreadsheetBuilder stream(File file) throws FileNotFoundException {
        return new PoiSpreadsheetBuilder(new SXSSFWorkbook(), new FileOutputStream(file));
    }

    public static SpreadsheetBuilder stream(OutputStream out, InputStream template) throws IOException {
        return new PoiSpreadsheetBuilder(new SXSSFWorkbook(new XSSFWorkbook(template)), out);
    }

    public static SpreadsheetBuilder stream(File file, InputStream template) throws IOException {
        return new PoiSpreadsheetBuilder(new SXSSFWorkbook(new XSSFWorkbook(template)), new FileOutputStream(file));
    }

    public static SpreadsheetBuilder stream(OutputStream out, File template) throws IOException, InvalidFormatException {
        return new PoiSpreadsheetBuilder(new SXSSFWorkbook(new XSSFWorkbook(template)), out);
    }

    public static SpreadsheetBuilder stream(File file, File template) throws IOException, InvalidFormatException {
        return new PoiSpreadsheetBuilder(new SXSSFWorkbook(new XSSFWorkbook(template)), new FileOutputStream(file));
    }

    private final Workbook workbook;
    private final OutputStream outputStream;

    private PoiSpreadsheetBuilder(Workbook workbook, OutputStream outputStream) {
        this.workbook = workbook;
        this.outputStream = outputStream;
    }

    @Override
    public void build(Consumer<WorkbookDefinition> workbookDefinition) {
        PoiWorkbookDefinition poiWorkbook = new PoiWorkbookDefinition(workbook);
        workbookDefinition.accept(poiWorkbook);
        poiWorkbook.resolve();
        if (outputStream != null) {
            writeTo(outputStream);
        }
    }

    private void writeTo(OutputStream outputStream) {
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
