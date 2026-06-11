/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2026 Vladimir Orany.
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
package builders.dsl.spreadsheet.cli;

import builders.dsl.spreadsheet.api.Cell;
import builders.dsl.spreadsheet.api.ForegroundFill;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.api.Row;
import builders.dsl.spreadsheet.api.Sheet;
import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder;
import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder;
import builders.dsl.spreadsheet.parser.data.json.JsonSpreadsheetParser;
import builders.dsl.spreadsheet.parser.data.yml.YmlSpreadsheetParser;
import builders.dsl.spreadsheet.query.api.CellCriterion;
import builders.dsl.spreadsheet.query.api.CellStyleCriterion;
import builders.dsl.spreadsheet.query.api.RowCriterion;
import builders.dsl.spreadsheet.query.api.SheetCriterion;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteriaResult;
import builders.dsl.spreadsheet.query.api.WorkbookCriterion;
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({"java:S106", "java:S3776"})
public final class SpreadsheetCli {

    private static final String SHEET = "sheet";
    private static final String SHEETS = "sheets";
    private static final String ROW = "row";
    private static final String NUMBER = "number";
    private static final String COLUMN = "column";
    private static final String VALUE = "value";

    private SpreadsheetCli() {
        // utility class
    }

    public static void main(String[] args) throws IOException {
        run(args);
    }

    static void run(String[] args) throws IOException {
        if (args.length == 0 || "--help".equals(args[0]) || "-h".equals(args[0])) {
            printHelp();
            return;
        }

        switch (args[0]) {
            case "create":
                requireArgumentCount(args, 3, "create <input.json|yaml|yml> <output.xlsx>");
                create(Path.of(args[1]), new File(args[2]));
                break;
            case "query":
                requireArgumentCount(args, 3, "query <workbook.xlsx> <query.json|yaml|yml>");
                query(new File(args[1]), Path.of(args[2]));
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + args[0]);
        }
    }

    private static void printHelp() {
        System.out.println("spreadsheet-builder-cli");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  create <input.json|yaml|yml> <output.xlsx>");
        System.out.println("  query <workbook.xlsx> <criteria.json|yaml|yml>");
        System.out.println();
        System.out.println("The create command accepts the data format supported by spreadsheet-builder-data.");
        System.out.println("The query command accepts a serialized criteria tree: sheets, rows, cells, page, and or.");
    }

    private static void requireArgumentCount(String[] args, int count, String usage) {
        if (args.length != count) {
            throw new IllegalArgumentException("Usage: " + usage);
        }
    }

    private static void create(Path input, File output) throws IOException {
        SpreadsheetBuilder builder = PoiSpreadsheetBuilder.create(output);
        try (FileInputStream stream = new FileInputStream(input.toFile())) {
            if (isJson(input)) {
                new JsonSpreadsheetParser(builder).parse(stream);
            } else {
                new YmlSpreadsheetParser(builder).parse(stream);
            }
        }
        System.out.println("created " + output.getPath());
    }

    private static void query(File workbookFile, Path queryFile) throws IOException {
        Map<String, Object> serializedCriteria = readMap(queryFile);
        SpreadsheetCriteria criteria = PoiSpreadsheetCriteria.FACTORY.forFile(workbookFile);
        SpreadsheetCriteriaResult result = criteria.query(workbook -> applyWorkbook(workbook, serializedCriteria));
        ObjectMapper json = new ObjectMapper();
        System.out.println(json.writerWithDefaultPrettyPrinter().writeValueAsString(resultMap(result)));
    }

    private static void applyWorkbook(WorkbookCriterion workbook, Map<String, Object> spec) {
        for (Object sheetValue : list(spec.get(SHEETS))) {
            Map<String, Object> sheet = map(sheetValue);
            if (sheet.containsKey("name")) {
                workbook.sheet(string(sheet.get("name")), criterion -> applySheet(criterion, sheet));
            } else {
                workbook.sheet(criterion -> applySheet(criterion, sheet));
            }
        }
        for (Object alternative : list(spec.get("or"))) {
            workbook.or((Consumer<WorkbookCriterion>) criterion -> applyWorkbook(criterion, map(alternative)));
        }
        if (!spec.containsKey(SHEETS) && spec.containsKey(SHEET)) {
            workbook.sheet(string(spec.get(SHEET)), criterion -> applySheet(criterion, spec));
        }
    }

    private static void applySheet(SheetCriterion sheet, Map<String, Object> spec) {
        if (spec.containsKey("state")) {
            sheet.state(enumValue(Keywords.SheetState.class, spec.get("state")));
        }
        if (spec.containsKey("page")) {
            applyPage(sheet, map(spec.get("page")));
        }
        for (Object rowValue : list(spec.get("rows"))) {
            applyRowSelection(sheet, map(rowValue));
        }
        for (Object alternative : list(spec.get("or"))) {
            sheet.or((Consumer<SheetCriterion>) criterion -> applySheet(criterion, map(alternative)));
        }
    }

    private static void applyPage(SheetCriterion sheet, Map<String, Object> spec) {
        sheet.page(page -> {
            if (spec.containsKey("orientation")) {
                page.orientation(enumValue(Keywords.Orientation.class, spec.get("orientation")));
            }
            if (spec.containsKey("paper")) {
                page.paper(enumValue(Keywords.Paper.class, spec.get("paper")));
            }
        });
    }

    private static void applyRowSelection(SheetCriterion sheet, Map<String, Object> spec) {
        if (spec.containsKey("from") && spec.containsKey("to")) {
            sheet.row(integer(spec.get("from")), integer(spec.get("to")));
            sheet.row(row -> applyRow(row, spec));
        } else if (spec.containsKey(NUMBER)) {
            sheet.row(integer(spec.get(NUMBER)), row -> applyRow(row, spec));
        } else if (spec.containsKey(ROW)) {
            sheet.row(integer(spec.get(ROW)), row -> applyRow(row, spec));
        } else {
            sheet.row(row -> applyRow(row, spec));
        }
    }

    private static void applyRow(RowCriterion row, Map<String, Object> spec) {
        for (Object cellValue : list(spec.get("cells"))) {
            applyCellSelection(row, map(cellValue));
        }
        for (Object alternative : list(spec.get("or"))) {
            row.or((Consumer<RowCriterion>) criterion -> applyRow(criterion, map(alternative)));
        }
    }

    private static void applyCellSelection(RowCriterion row, Map<String, Object> spec) {
        if (spec.containsKey("from") && spec.containsKey("to")) {
            Object from = spec.get("from");
            Object to = spec.get("to");
            if (from instanceof Number && to instanceof Number) {
                row.cell(integer(from), integer(to), cell -> applyCell(cell, spec));
            } else {
                row.cell(string(from), string(to), cell -> applyCell(cell, spec));
            }
        } else if (spec.containsKey(COLUMN)) {
            Object column = spec.get(COLUMN);
            if (column instanceof Number) {
                row.cell(integer(column), cell -> applyCell(cell, spec));
            } else {
                row.cell(string(column), cell -> applyCell(cell, spec));
            }
        } else {
            row.cell(cell -> applyCell(cell, spec));
        }
    }

    private static void applyCell(CellCriterion cell, Map<String, Object> spec) {
        if (spec.containsKey(VALUE)) {
            cell.value(spec.get(VALUE));
        }
        if (spec.containsKey("string")) {
            cell.string(string(spec.get("string")));
        }
        if (spec.containsKey(NUMBER)) {
            cell.number(decimal(spec.get(NUMBER)));
        }
        if (spec.containsKey("bool")) {
            cell.bool(Boolean.valueOf(string(spec.get("bool"))));
        }
        if (spec.containsKey("localDate")) {
            cell.localDate(LocalDate.parse(string(spec.get("localDate"))));
        }
        if (spec.containsKey("localDateTime")) {
            cell.localDateTime(LocalDateTime.parse(string(spec.get("localDateTime"))));
        }
        if (spec.containsKey("localTime")) {
            cell.localTime(LocalTime.parse(string(spec.get("localTime"))));
        }
        if (spec.containsKey("rowspan")) {
            cell.rowspan(integer(spec.get("rowspan")));
        }
        if (spec.containsKey("colspan")) {
            cell.colspan(integer(spec.get("colspan")));
        }
        if (spec.containsKey("name")) {
            cell.name(string(spec.get("name")));
        }
        if (spec.containsKey("comment")) {
            cell.comment(string(spec.get("comment")));
        }
        if (spec.containsKey("style")) {
            cell.style(style -> applyStyle(style, map(spec.get("style"))));
        }
        for (Object alternative : list(spec.get("or"))) {
            cell.or((Consumer<CellCriterion>) criterion -> applyCell(criterion, map(alternative)));
        }
    }

    private static void applyStyle(CellStyleCriterion style, Map<String, Object> spec) {
        if (spec.containsKey("background")) {
            style.background(string(spec.get("background")));
        }
        if (spec.containsKey("foreground")) {
            style.foreground(string(spec.get("foreground")));
        }
        if (spec.containsKey("fill")) {
            style.fill(enumValue(ForegroundFill.class, spec.get("fill")));
        }
        if (spec.containsKey("indent")) {
            style.indent(integer(spec.get("indent")));
        }
        if (spec.containsKey("rotation")) {
            style.rotation(integer(spec.get("rotation")));
        }
        if (spec.containsKey("format")) {
            style.format(string(spec.get("format")));
        }
    }

    private static Map<String, Object> resultMap(SpreadsheetCriteriaResult result) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put(SHEETS, result.getSheets().stream().map(SpreadsheetCli::sheetMap).toList());
        output.put("rows", result.getRows().stream().map(SpreadsheetCli::rowMap).toList());
        output.put("cells", result.getCells().stream().map(SpreadsheetCli::cellMap).toList());
        return output;
    }

    private static Map<String, Object> sheetMap(Sheet sheet) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", sheet.getName());
        return map;
    }

    private static Map<String, Object> rowMap(Row row) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(SHEET, row.getSheet().getName());
        map.put(ROW, row.getNumber());
        map.put("values", row.getCells().stream().map(Cell::getValue).toList());
        return map;
    }

    private static Map<String, Object> cellMap(Cell cell) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(SHEET, cell.getRow().getSheet().getName());
        map.put(ROW, cell.getRow().getNumber());
        map.put(COLUMN, cell.getColumnAsString());
        map.put(VALUE, cell.getValue());
        return map;
    }

    private static Map<String, Object> readMap(Path input) throws IOException {
        ObjectMapper mapper = isJson(input) ? new ObjectMapper() : new ObjectMapper(new YAMLFactory());
        return mapper.readValue(input.toFile(), new TypeReference<Map<String, Object>>() { });
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> map(Object value) {
        if (value instanceof Map<?, ?>) {
            return (Map<String, Object>) value;
        }
        return Map.of();
    }

    private static List<?> list(Object value) {
        if (value instanceof Collection<?>) {
            return new ArrayList<>((Collection<?>) value);
        }
        if (value == null) {
            return List.of();
        }
        return List.of(value);
    }

    private static String string(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static Integer integer(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.valueOf(string(value));
    }

    private static Double decimal(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return Double.valueOf(string(value));
    }

    private static <T extends Enum<T>> T enumValue(Class<T> type, Object value) {
        String name = string(value).trim().replace('-', '_').toUpperCase(Locale.ROOT);
        return Enum.valueOf(type, name);
    }

    private static boolean isJson(Path path) {
        return path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".json");
    }
}
