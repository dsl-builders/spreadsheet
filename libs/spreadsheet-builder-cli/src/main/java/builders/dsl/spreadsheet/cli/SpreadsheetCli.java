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
import builders.dsl.spreadsheet.api.Row;
import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder;
import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder;
import builders.dsl.spreadsheet.parser.data.json.JsonSpreadsheetParser;
import builders.dsl.spreadsheet.parser.data.yml.YmlSpreadsheetParser;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteriaResult;
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class SpreadsheetCli {

    private SpreadsheetCli() {
        // utility class
    }

    public static void main(String[] args) throws Exception {
        run(args);
    }

    static void run(String[] args) throws Exception {
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
        System.out.println("  query <workbook.xlsx> <query.json|yaml|yml>");
        System.out.println();
        System.out.println("The create command accepts the data format supported by spreadsheet-builder-data.");
        System.out.println("The query command accepts: sheet, where.column, and optional where.equals/where.contains.");
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
        Map<String, Object> query = readMap(queryFile);
        String sheetName = string(query.getOrDefault("sheet", "Sheet1"));
        Map<String, Object> where = map(query.get("where"));
        int column = parseColumn(string(where.getOrDefault("column", "A")));
        Object expected = where.get("equals");
        String contains = where.containsKey("contains") ? string(where.get("contains")) : null;

        SpreadsheetCriteria criteria = PoiSpreadsheetCriteria.FACTORY.forFile(workbookFile);
        SpreadsheetCriteriaResult result = criteria.query(workbook -> workbook.sheet(sheetName, sheet -> sheet.row(row -> row.cell(column))));
        List<Map<String, Object>> matches = new ArrayList<>();

        for (Row row : result.getRows()) {
            Cell matchedCell = findCell(row.getCells(), column);
            if (matchedCell == null || !matches(matchedCell.getValue(), expected, contains)) {
                continue;
            }
            Map<String, Object> match = new LinkedHashMap<>();
            match.put("sheet", row.getSheet().getName());
            match.put("row", row.getNumber());
            match.put("matchedColumn", columnToName(column));
            match.put("matchedValue", matchedCell.getValue());
            match.put("values", row.getCells().stream().map(Cell::getValue).toList());
            matches.add(match);
        }

        ObjectMapper json = new ObjectMapper();
        System.out.println(json.writerWithDefaultPrettyPrinter().writeValueAsString(Map.of("matches", matches)));
    }

    private static Cell findCell(Collection<? extends Cell> cells, int column) {
        for (Cell cell : cells) {
            if (cell.getColumn() == column) {
                return cell;
            }
        }
        return null;
    }

    private static boolean matches(Object actual, Object expected, String contains) {
        if (expected != null) {
            return Objects.equals(String.valueOf(actual), String.valueOf(expected));
        }
        if (contains != null) {
            return String.valueOf(actual).toLowerCase(Locale.ROOT).contains(contains.toLowerCase(Locale.ROOT));
        }
        return true;
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

    private static String string(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static boolean isJson(Path path) {
        return path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".json");
    }

    private static int parseColumn(String text) {
        String column = text.trim().toUpperCase(Locale.ROOT);
        if (column.matches("\\d+")) {
            return Integer.parseInt(column);
        }

        int result = 0;
        for (int i = 0; i < column.length(); i++) {
            char character = column.charAt(i);
            if (character < 'A' || character > 'Z') {
                throw new IllegalArgumentException("Invalid column: " + text);
            }
            result = result * 26 + character - 'A' + 1;
        }
        return result;
    }

    private static String columnToName(int column) {
        StringBuilder name = new StringBuilder();
        int current = column;
        while (current > 0) {
            current--;
            name.insert(0, (char) ('A' + current % 26));
            current /= 26;
        }
        return name.toString();
    }
}
