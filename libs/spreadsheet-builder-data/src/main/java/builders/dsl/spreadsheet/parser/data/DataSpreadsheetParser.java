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
package builders.dsl.spreadsheet.parser.data;

import builders.dsl.spreadsheet.api.BorderStyle;
import builders.dsl.spreadsheet.api.Color;
import builders.dsl.spreadsheet.api.FontStyle;
import builders.dsl.spreadsheet.api.ForegroundFill;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.BorderDefinition;
import builders.dsl.spreadsheet.builder.api.CellDefinition;
import builders.dsl.spreadsheet.builder.api.CellStyleDefinition;
import builders.dsl.spreadsheet.builder.api.CommentDefinition;
import builders.dsl.spreadsheet.builder.api.DimensionModifier;
import builders.dsl.spreadsheet.builder.api.FontDefinition;
import builders.dsl.spreadsheet.builder.api.HasStyle;
import builders.dsl.spreadsheet.builder.api.ImageCreator;
import builders.dsl.spreadsheet.builder.api.PageDefinition;
import builders.dsl.spreadsheet.builder.api.RowDefinition;
import builders.dsl.spreadsheet.builder.api.SheetDefinition;
import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder;
import builders.dsl.spreadsheet.builder.api.WorkbookDefinition;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: apply "stylesheet" from list of styles

public class DataSpreadsheetParser {

    private static final List<String> REQUIRES_NAME = Collections.singletonList("name");
    private static final Pattern DIMENSION_IN_POINTS = Pattern.compile("(\\d+)\\s?(p(oin)?ts?)?");
    private static final Pattern DIMENSION_IN_CM = Pattern.compile("(\\d+)\\s?cm");
    private static final Pattern DIMENSION_IN_INCHES = Pattern.compile("(\\d+)\\s?in(ch(es)?)?");
    private static final Pattern ISO_DATE_PATTERN = Pattern.compile("^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:\\.\\d{1,9})?(?:Z|[+-][01]\\d:[0-5]\\d)$");

    private final SpreadsheetBuilder builder;

    public DataSpreadsheetParser(SpreadsheetBuilder builder) {
        this.builder = builder;
    }

    private static <T> void doSafe(String entryPath, T value, Consumer<T> operation) {
        try {
            operation.accept(value);
        } catch (InvalidPropertyException e){
            // if alredy IPE, just rethrow
            throw e;
        } catch (Exception e) {
            // wrap any other exception to IPE
            throw new InvalidPropertyException(e, entryPath, value);
        }
    }

    private void handleNumber(String path, Object value, Consumer<Number> consumer) {
        if (value instanceof Number) {
            consumer.accept((Number) value);
            return;
        }
        throw new InvalidPropertyException("Value must be number", path, value);
    }

    private void handleBoolean(String path, Object value, Consumer<Boolean> consumer) {
        if (value instanceof Boolean) {
            consumer.accept((Boolean) value);
            return;
        }
        throw new InvalidPropertyException("Value must be boolean", path, value);
    }

    private void ifTrue(String path, Object value, Runnable runnable) {
        handleBoolean(path, value, bool -> {
            if (bool) {
                runnable.run();
            }
        });
    }

    private static void eachItemWithIndex(Object value, String path, BiConsumer<Object, Integer> consumer) {
        if (!(value instanceof Iterable)) {
            throw new InvalidPropertyException("Value must be iterable!", path, value);
        }
        Iterable<?> iterable = (Iterable<?>) value;
        int index = 0;
        for (Object item : iterable) {
            consumer.accept(item, index++);
        }
    }

    private static void eachItemWithPath(Object value, String path, BiConsumer<Object, String> consumer) {
        eachItemWithIndex(value, path, (item, index) -> {
            final String itemPath = path + "[" + index + "]";
            consumer.accept(item, itemPath);
        });
    }

    private static void withMap(Object value, String path, Consumer<Map<String, Object>> consumer) {
        withMap(value, path, Collections.emptyList(), consumer);
    }

    private static void withMap(Object value, String path, Iterable<String> requiredProperties, Consumer<Map<String, Object>> consumer) {
        withMap(value, path, requiredProperties, (map, localPath) -> consumer.accept(map));
    }

    private static void withMap(Object value, String path, Iterable<String> requiredProperties, BiConsumer<Map<String, Object>, String> consumer) {
        if (!(value instanceof Map)) {
            throw new InvalidPropertyException("Definition must be map", path, value);
        }

        Map<?, ?> map = (Map<?, ?>) value;
        for (String requiredProperty: requiredProperties) {
            if (!map.containsKey(requiredProperty)) {
                throw new InvalidPropertyException("Definition is missing '" + requiredProperty + "' property", path, value);
            }
        }

        doSafe(path, map, v -> consumer.accept(checkedMap(v), path));
    }

    private static void handleMap(String path, Map<String, Object> map, MapEntryHandler handler) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String entryPath = path + "." + entry.getKey();
            doSafe(entryPath, entry.getValue(), value -> handler.handle(entryPath, entry.getKey(), entry.getValue()));
        }
    }

    @SuppressWarnings({
            "unchecked",
            "rawtypes"
    })
    private static Map<String, Object> checkedMap(Map map) {
        return Collections.checkedMap(map, String.class, Object.class);
    }

    private static void eachItemAsMap(Object value, String path, Iterable<String> requiredProperties, BiConsumer<Map<String, Object>, String> consumer) {
        eachItemWithPath(value, path, (item, itemPath) -> withMap(item, itemPath, requiredProperties, map -> consumer.accept(map, itemPath)));
    }

    public void build(Iterable<Map<String, Object>> value) {
        build(Collections.singletonMap("sheets", value));
    }

    public void build(final Map<String, Object> definitionMap) {
        this.builder.build(w -> handleWorkbook(w, definitionMap));
    }

    @SuppressWarnings("unchecked")
    public void build(Object value) {
        if (value == null) {
            throw new InvalidPropertyException("No Data", "", null);
        }
        if (value instanceof Map) {
            build((Map<String, Object>) value);
        } else if (value instanceof Iterable) {
            build((Iterable<Map<String, Object>>) value);
        } else {
            throw new InvalidPropertyException("Unsupported definition type (" + value.getClass() + "): ", "", value);
        }
    }

    private void handleWorkbook(WorkbookDefinition w, Map<String, Object> definitionMap) {
        for (Map.Entry<String, Object> entry : definitionMap.entrySet()) {
            switch (entry.getKey()) {
                case "styles":
                    eachItemAsMap(entry.getValue(), entry.getKey(), REQUIRES_NAME, (map, localPath) -> w.style(String.valueOf(map.get("name")), s -> handleStyle(s, localPath, map)));
                    break;
                case "sheets":
                    eachItemWithPath(entry.getValue(), entry.getKey(), (item, itemPath) -> {
                        if (item instanceof Map) {
                            withMap(item, itemPath, REQUIRES_NAME, sheet -> handleSheet(w, itemPath, sheet));
                        } else {
                            Map<String, Object> sheet = new HashMap<>();
                            sheet.put("rows", item);
                            sheet.put("name", "Sheet1");
                            handleSheet(w, itemPath, sheet);
                        }
                    });
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + entry.getKey(), entry.getKey(), entry.getValue());
            }
        }
    }

    private void handleSheet(WorkbookDefinition w, String localPath, Map<String, Object> map) {
        w.sheet(String.valueOf(map.get("name")), s -> handleSheet(s, localPath, map));
    }

    private void handleSheet(SheetDefinition s, String path, Map<String, Object> map) {
        handleMap(path, map, (String entryPath, String key, Object value) -> {
            switch (key) {
                case "rows":
                    handleRows(s, path, value);
                    break;
                case "filter":
                    ifTrue(entryPath, value, () -> s.filter(Keywords.Auto.AUTO));
                    break;
                case "freeze":
                    handleFreeze(s, entryPath, value);
                    break;
                case "page":
                    withMap(value, entryPath, page -> s.page(p -> handlePage(p, entryPath, page)));
                    break;
                case "password":
                    s.password(String.valueOf(value));
                    break;
                case "state":
                    handleState(s, value);
                    break;
                case "name":
                    // handled already
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        });
    }

    private void handleState(SheetDefinition s, Object value) {
        s.state(Keywords.SheetState.valueOf(asEnumName(value)));
    }

    private void handleRows(SheetDefinition s, String path, Object value) {
        eachItemWithPath(value, path, (item, itemPath) -> {
            if (item instanceof Map) {
                withMap(item, itemPath, Collections.emptyList(), row -> handleRow(s, itemPath, row));
            } else if (item instanceof Iterable) {
                handleRow(s, itemPath, Collections.singletonMap("cells", item));
            }
        });
    }

    private void handlePage(PageDefinition p, String path, Map<String, Object> page) {
        handleMap(path, page, (String entryPath, String key, Object value) -> {
            switch (key) {
                case "fit":
                    withMap(value, entryPath, fit -> handlePageFit(p, entryPath, fit));
                    break;
                case "orientation":
                    handlePageOrientation(p, value);
                    break;
                case "paper":
                    handlePagePaper(p, value);
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        });
    }

    private void handlePagePaper(PageDefinition p, Object value) {
        p.paper(Keywords.Paper.valueOf(asEnumName(value)));
    }

    private void handlePageOrientation(PageDefinition p, Object value) {
        p.orientation(Keywords.Orientation.valueOf(asEnumName(value)));
    }

    private void handlePageFit(PageDefinition p, String path, Map<String, Object> fit) {
        handleMap(path, fit, (entryPath, key, value) -> {
            switch (key) {
                case "width":
                    handleNumber(entryPath, value, number -> p.fit(Keywords.Fit.WIDTH).to(number.intValue()));
                    break;
                case "height":
                    handleNumber(entryPath, value, number -> p.fit(Keywords.Fit.HEIGHT).to(number.intValue()));
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        });
    }

    private void handleFreeze(SheetDefinition s, String path, Object freeze) {
        AtomicInteger row = new AtomicInteger(0);
        AtomicInteger column = new AtomicInteger(0);
        AtomicReference<String> columnAsString = new AtomicReference<>();
        withMap(freeze, path, map -> handleMap(path, map, (entryPath, key, value) -> {
            switch (key) {
                case "row":
                    handleNumber(entryPath, value, number -> row.set(number.intValue()));
                    break;
                case "column":
                    if (value instanceof Number) {
                        handleNumber(entryPath, value, number -> column.set(number.intValue()));
                    } else {
                        columnAsString.set(String.valueOf(value));
                    }
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        }));
        if (columnAsString.get() != null) {
            s.freeze(columnAsString.get(), row.get());
        } else {
            s.freeze(column.get(), row.get());
        }
    }

    private void handleRow(SheetDefinition s, String path, Map<String, Object> row) {
        if (row.containsKey("group") && row.size() == 1) {
            s.group(group -> eachItemAsMap(row.get("group"), path + "." + "group", Collections.emptyList(), (item, itemPath) -> handleRow(group, itemPath, item)));
        } else if (row.containsKey("collapse") && row.size() == 1) {
            s.collapse(collapse -> eachItemAsMap(row.get("collapse"), path + "." + "collapse", Collections.emptyList(), (item, itemPath) -> handleRow(collapse, itemPath, item)));
        } else if (row.containsKey("number")) {
            handleNumber(path, row.get("number"), number -> s.row(number.intValue(), r -> handleRow(r, path, row)));
        } else {
            s.row(r -> handleRow(r, path, row));
        }
    }

    private void handleRow(RowDefinition r, String path, Map<String, Object> map) {
        handleMap(path, map, (entryPath, key, value) -> {
            switch (key) {
                case "styles":
                    handleStyles(r, entryPath, value);
                    break;
                case "cells":
                    eachItemWithPath(value, entryPath, (item, itemPath) -> handleCell(r, itemPath, item));
                    break;
                case "number":
                    // already handled
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        });
    }

    private void handleCell(RowDefinition r, String itemPath, Object item) {
        if (item instanceof Map) {
            withMap(item, itemPath, map -> handleCellFromMap(r, itemPath, map));
        } else {
            r.cell(c -> handleCellValue(c, item));
        }
    }

    private void handleCellFromMap(RowDefinition r, String path, Map<String, Object> map) {
        if (map.containsKey("group") && map.size() == 1) {
            r.group(group -> eachItemWithPath(map.get("group"), path + "." + "group", (item, itemPath) -> handleCell(group, itemPath, item)));
        } else if (map.containsKey("collapse") && map.size() == 1) {
            r.collapse(collapse -> eachItemWithPath(map.get("collapse"), path + "." + "collapse", (item, itemPath) -> handleCell(collapse, itemPath, item)));
        } else if (map.containsKey("column")) {
            Object column = map.get("column");
            if (column instanceof Number) {
                handleNumber(path, column, number -> r.cell(number.intValue(), c -> handleCell(c, path, map)));
            } else {
                r.cell(String.valueOf(column), c -> handleCell(c, path, map));
            }
        } else {
            r.cell(c -> handleCell(c, path, map));
        }
    }

    private void handleCell(CellDefinition c, String path, Map<String, Object> map) {
        handleMap(path, map, (entryPath, key, value) -> {
            switch (key) {
                case "value":
                    handleCellValue(c, value);
                    break;
                case "name":
                    handleCellName(c, value);
                    break;
                case "formula":
                    handleCellFormula(c, value);
                    break;
                case "comment":
                    handleCellComment(c, entryPath, value);
                    break;
                case "link":
                    handleCellLink(c, entryPath, value);
                    break;
                case "colspan":
                    handleCellColspan(c, entryPath, value);
                    break;
                case "rowspan":
                    handleCellRowspan(c, entryPath, value);
                    break;
                case "width":
                    handleCellWidth(c, entryPath, value);
                    break;
                case "height":
                    handleCellHeight(c, entryPath, value);
                    break;
                case "text":
                    handleCellText(c, entryPath, value);
                    break;
                case "styles":
                    handleStyles(c, entryPath, value);
                    break;
                case "image":
                    handleCellImage(c, entryPath, value);
                    break;
                case "column":
                    // already handled
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        });
    }

    private void handleCellText(CellDefinition c, String entryPath, Object value) {
        if (value instanceof Iterable) {
            eachItemWithPath(value, entryPath, (item, itemPath) -> {
                if (item instanceof Map) {
                    withMap(item, itemPath, Collections.singletonList("content"), map -> {
                        Object font = map.get("font");
                        String content = String.valueOf(map.get("content"));
                        if (font == null) {
                            c.text(content);
                        } else {
                            String fontPath = itemPath + "." + "font";
                            withMap(font, fontPath, fontMap -> c.text(content, f -> handleFont(f, fontPath, fontMap)));
                        }
                    });
                } else {
                    c.text(String.valueOf(item));
                }
            });
        } else {
            c.value(value);
        }
    }

    private void handleCellImage(CellDefinition c, String path, Object image) {
        Map<String, Function<Keywords.Image, ImageCreator>> imageCreators = new LinkedHashMap<>();
        imageCreators.put("png", c::png);
        imageCreators.put("jpeg", c::jpeg);
        imageCreators.put("jpg", c::jpeg);
        imageCreators.put("pict", c::pict);
        imageCreators.put("emf", c::emf);
        imageCreators.put("wmf", c::wmf);
        imageCreators.put("dib", c::dib);

        if (image instanceof Map) {
            withMap(image, path, Arrays.asList("url", "type"), map -> {
                String type = String.valueOf(map.get("type")).toLowerCase();
                String url = String.valueOf(map.get("url"));
                if (!imageCreators.containsKey(type)) {
                    throw new InvalidPropertyException("Unknown image type", path + "." + "type", type);
                }
                try {
                    new URL(url);
                } catch (MalformedURLException e) {
                    throw new InvalidPropertyException("Malformed image URL", path + "." + "url", url);
                }
                imageCreators.get(type).apply(Keywords.Image.IMAGE).from(url);
            });
            return;
        }

        String url = String.valueOf(image);

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new InvalidPropertyException("Malformed image URL", path, url);
        }

        String lowerCase = url.toLowerCase();
        boolean linkCreated = false;

        for (String extension: imageCreators.keySet()) {
            if (lowerCase.endsWith(extension)) {
                imageCreators.get(extension).apply(Keywords.Image.IMAGE).from(url);
                linkCreated = true;
                break;
            }
        }

        if (!linkCreated) {
            throw new InvalidPropertyException("Could not determine image type. Please be sure that the URL ends with one of following extensions: " + String.join(",", imageCreators.keySet()), path, url);
        }
    }

    private void handleCellLink(CellDefinition c, String path, Object value) {
        String link = String.valueOf(value);
        try {
            URI uri = new URI(link);
            if ("http".equals(uri.getScheme()) || ("https".equals(uri.getScheme()))) {
                c.link(Keywords.To.TO).url(link);
            } else if ("mailto".equals(uri.getScheme())) {
                c.link(Keywords.To.TO).email(link.substring(7));
            } else if ("file".equals(uri.getScheme())) {
                c.link(Keywords.To.TO).file(link.substring(5));
            } else {
                c.link(Keywords.To.TO).name(link);
            }
        } catch (URISyntaxException e) {
            throw new InvalidPropertyException("Invalid link format", e, path, value);
        }
    }

    private void handleCellComment(CellDefinition c, String entryPath, Object value) {
        if (value instanceof String) {
            c.comment((String) value);
        } else {
            c.comment(comment -> withMap(value, entryPath, map -> handleCellComment(comment, entryPath, map)));
        }
    }

    private void handleCellComment(CommentDefinition c, String path, Map<String, Object> map) {
        handleMap(path, map, (entryPath, key, value) -> {
            switch (key) {
                case "author":
                    c.author(String.valueOf(value));
                    break;
                case "text":
                    c.text(String.valueOf(value));
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        });
    }

    private void handleCellWidth(CellDefinition c, String path, Object value) {
        if ("auto".equals(value)) {
            c.width(Keywords.Auto.AUTO);
        } else {
            handleDimension(value, path, c::width);
        }
    }

    private void handleDimension(Object value, String path, Function<Double, DimensionModifier> dimensionSetter) {
        if (value instanceof Number) {
            dimensionSetter.apply(((Number) value).doubleValue());
        } else {
            String dimension = String.valueOf(value);
            Matcher cmMatcher = DIMENSION_IN_CM.matcher(dimension);
            Matcher inchMatcher = DIMENSION_IN_INCHES.matcher(dimension);
            Matcher pointMatcher = DIMENSION_IN_POINTS.matcher(dimension);
            if (cmMatcher.matches()) {
                dimensionSetter.apply(Double.valueOf(cmMatcher.group(1))).cm();
            } else if (inchMatcher.matches()) {
                dimensionSetter.apply(Double.valueOf(inchMatcher.group(1))).inch();
            } else if (pointMatcher.matches()) {
                dimensionSetter.apply(Double.valueOf(pointMatcher.group(1)));
            } else {
                throw new InvalidPropertyException("Invalid dimension format", path, value);
            }
        }
    }

    private void handleCellHeight(CellDefinition c, String path, Object value) {
        handleDimension(value, path, c::height);
    }

    private void handleCellColspan(CellDefinition c, String path, Object value) {
        handleNumber(path, value, number -> c.colspan(number.intValue()));
    }

    private void handleCellRowspan(CellDefinition c, String path, Object value) {
        handleNumber(path, value, number -> c.rowspan(number.intValue()));
    }

    private void handleCellFormula(CellDefinition c, Object value) {
        c.formula(String.valueOf(value));
    }

    private void handleCellName(CellDefinition c, Object value) {
        c.name(String.valueOf(value));
    }

    private void handleCellValue(CellDefinition c, Object value) {
        if (value instanceof String && ISO_DATE_PATTERN.matcher((String)value).matches()) {
            c.value(Date.from(ZonedDateTime.parse((String)value, DateTimeFormatter.ISO_DATE_TIME).toInstant()));
            return;
        }
        c.value(value);
    }

    private void handleStyles(HasStyle r, String path, Object value) {
        List<String> styles = new ArrayList<>();
        List<Consumer<CellStyleDefinition>> stylesDefinitions = new ArrayList<>();

        eachItemWithPath(value, path, (item, itemPath) -> {
            if (item instanceof String) {
                styles.add((String) item);
            } else {
                withMap(item, itemPath, map -> stylesDefinitions.add(style -> handleStyle(style, itemPath, map)));
            }
        });

        r.styles(styles, stylesDefinitions);
    }

    private void handleFont(FontDefinition f, String path, Map<String, Object> map) {
        handleMap(path, map, (entryPath, key, value) -> {
            switch (key) {
                case "color":
                    handleFontColor(entryPath, f, value);
                    break;
                case "size":
                    handleFontSize(f, entryPath, value);
                    break;
                case "style":
                    handleFontStyle(f, value);
                    break;
                case "styles":
                    eachItemWithPath(value, entryPath, (item, itemPath) -> handleFontStyle(f, item));
                    break;
                case "name":
                    handleFontName(f, value);
                    break;
                case "content":
                    // from text - ignore
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        });
    }

    private void handleFontStyle(FontDefinition f, Object value) {
        f.style(FontStyle.valueOf(asEnumName(value)));
    }

    private void handleFontName(FontDefinition f, Object value) {
        f.name(String.valueOf(value));
    }

    private void handleFontSize(FontDefinition f, String path, Object value) {
        handleNumber(path, value, number -> f.size(number.intValue()));
    }

    private void handleFontColor(String path, FontDefinition f, Object value) {
        handleColor(path, value, f::color, f::color);
    }

    private void handleColor(String path, Object value, Consumer<String> hexColorConsumer, Consumer<Color> colorConsumer) {
        String color = String.valueOf(value);
        if (color.startsWith("#")) {
            hexColorConsumer.accept(color);
        } else {
            colorConsumer.accept(readFakeEnumValue(path, color, Color.class, true));
        }
    }

    private void handleBorder(BorderDefinition b, String path, Map<String, Object> map) {
        handleMap(path, map, (entryPath, key, value) -> {
            switch (key) {
                case "style":
                    handleBorderStyle(b, value);
                    break;
                case "color":
                    handleBorderColor(entryPath, b, value);
                    break;
                case "side":
                    // already handled
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        });
    }

    private void handleBorderColor(String path, BorderDefinition b, Object value) {
        handleColor(path, value, b::color, b::color);
    }

    private void handleBorderStyle(BorderDefinition b, Object value) {
        b.style(BorderStyle.valueOf(asEnumName(value)));
    }

    private void handleBorder(CellStyleDefinition s, String path, Map<String, Object> map) {
        String key = "side";
        if (!map.containsKey(key)) {
            s.border(b -> handleBorder(b, path, map));
            return;
        }
        List<Keywords.BorderSide> sides = new ArrayList<>();

        Object value = map.get(key);

        if (!(value instanceof Iterable)) {
            value = Collections.singletonList(value);
        }

        eachItemWithIndex(value, path + "." + key, (item, index) -> sides.add(readFakeEnumValue(path + "." + key + "[" + index + "]", item, Keywords.BorderSide.class)));

        if (sides.size() == 4 || sides.isEmpty()) {
            s.border(b -> handleBorder(b, path, map));
        } else if (sides.size() == 3) {
            s.border(sides.get(0), sides.get(1), sides.get(2), b -> handleBorder(b, path, map));
        } else if (sides.size() == 2) {
            s.border(sides.get(0), sides.get(1), b -> handleBorder(b, path, map));
        } else if (sides.size() == 1) {
            s.border(sides.get(0), b -> handleBorder(b, path, map));
        }
    }

    private void handleStyle(CellStyleDefinition s, String path, Map<String, Object> map) {
        handleMap(path, map, (entryPath, key, value) -> {
            switch (key) {
                case "align":
                    handleAlign(s, entryPath, value);
                    break;
                case "background":
                    handleBackground(entryPath, s, value);
                    break;
                case "base":
                    handleBase(s, value);
                    break;
                case "borders":
                    eachItemAsMap(value, entryPath, Collections.emptyList(), (border, localPath) -> handleBorder(s, localPath, border));
                    break;
                case "fill":
                    handleFill(s, value);
                    break;
                case "font":
                    withMap(value, entryPath,  Collections.emptyList(), (font, localPath) -> s.font(f -> handleFont(f, localPath, font)));
                    break;
                case "foreground":
                    handleForeground(entryPath, s, value);
                    break;
                case "format":
                    handleFormat(s, value);
                    break;
                case "indent":
                    handleIndent(s, entryPath, value);
                    break;
                case "rotation":
                    handleRotation(s, entryPath, value);
                    break;
                case "wrap":
                    handleWrap(s, entryPath, value);
                    break;
                case "name":
                    // handled already
                    break;
                default:
                    throw new InvalidPropertyException("Unknown property: " + key, path, value);
            }
        });
    }

    private void handleWrap(CellStyleDefinition s, String entryPath, Object value) {
        ifTrue(entryPath, value, () -> s.wrap(Keywords.Text.WRAP));
    }

    private void handleFill(CellStyleDefinition s, Object value) {
        s.fill(ForegroundFill.valueOf(asEnumName(value)));
    }

    private void handleBase(CellStyleDefinition s, Object value) {
        s.base(String.valueOf(value));
    }

    private void handleBackground(String path, CellStyleDefinition s, Object value) {
        handleColor(path, value, s::background, s::background);
    }

    private void handleForeground(String path, CellStyleDefinition s, Object value) {
        handleColor(path, value, s::foreground, s::foreground);
    }

    private void handleFormat(CellStyleDefinition s, Object value) {
        s.format(String.valueOf(value));
    }

    private void handleIndent(CellStyleDefinition s, String path, Object value) {
        handleNumber(path, value, number -> s.indent(number.intValue()));
    }

    private void handleRotation(CellStyleDefinition s, String path, Object value) {
        handleNumber(path, value, number -> s.rotation(number.intValue()));
    }

    private void handleAlign(CellStyleDefinition s, String path, Object value) {
        withMap(value, path, map -> s.align(
            readFakeEnumValue(path, map, "vertical", Keywords.VerticalAlignment.class),
            readFakeEnumValue(path, map, "horizontal", Keywords.HorizontalAlignment.class)
        ));
    }

    private <T> T readFakeEnumValue(String path, Map<String, Object> map, String key, Class<T> type) {
        if (map.containsKey(key)) {
            return readFakeEnumValue(path + '.' + key, map.get(key), type);
        }
        return null;
    }

    private <T> T readFakeEnumValue(String path, Object value, Class<T> type) {
        return readFakeEnumValue(path, value, type, false);
    }

    private <T> T readFakeEnumValue(String path, Object value, Class<T> type, boolean keepCase) {
        try {
            return type.cast(type.getField(keepCase ? String.valueOf(value) : asEnumName(value)).get(null));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new InvalidPropertyException(type.getSimpleName() + " "  + value + " does not exist", e, path, value);
        }
    }

    private String asEnumName(Object value) {
        if (String.valueOf(value).matches("^[A-Z_]+$")) {
            return String.valueOf(value);
        }
        return String.valueOf(value).replaceAll("(.)(\\p{Upper})", "$1_$2").toUpperCase();
    }

    @FunctionalInterface private interface MapEntryHandler {
        void handle(String entryPath, String key, Object value);
    }

}
