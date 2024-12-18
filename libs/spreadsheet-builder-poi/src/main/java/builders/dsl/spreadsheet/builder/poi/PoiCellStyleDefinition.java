/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2024 Vladimir Orany.
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

import builders.dsl.spreadsheet.api.ForegroundFill;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.FontDefinition;
import builders.dsl.spreadsheet.impl.AbstractBorderDefinition;
import builders.dsl.spreadsheet.impl.AbstractCellDefinition;
import builders.dsl.spreadsheet.impl.AbstractCellStyleDefinition;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PoiCellStyleDefinition extends AbstractCellStyleDefinition {

    private static final DefaultIndexedColorMap INDEXED_COLOR_MAP = new DefaultIndexedColorMap();

    PoiCellStyleDefinition(PoiCellDefinition cell) {
        super(cell.getRow().getSheet().getWorkbook());
        Workbook workbook = cell.getCell().getSheet().getWorkbook();
        if (cell.getCell().getCellStyle().equals(workbook.getCellStyleAt(0))) {
            style = (XSSFCellStyle) workbook.createCellStyle();
            cell.getCell().setCellStyle(style);
        } else {
            style = (XSSFCellStyle) cell.getCell().getCellStyle();
        }
    }

    PoiCellStyleDefinition(PoiWorkbookDefinition workbook) {
        super(workbook);
        this.style = (XSSFCellStyle) workbook.getWorkbook().createCellStyle();
    }

    @Override
    protected void doBackground(String hexColor) {
        if (style.getFillForegroundColor() == IndexedColors.AUTOMATIC.getIndex()) {
            foreground(hexColor);
        } else {
            style.setFillBackgroundColor(parseColor(hexColor));
        }
    }

    @Override
    protected void doForeground(String hexColor) {
        if (style.getFillForegroundColor() != IndexedColors.AUTOMATIC.getIndex()) {
            // already set as background color
            style.setFillBackgroundColor(style.getFillForegroundXSSFColor());
        }

        style.setFillForegroundColor(parseColor(hexColor));
        if (style.getFillPattern().equals(FillPatternType.NO_FILL)) {
            fill(ForegroundFill.SOLID_FOREGROUND);
        }
    }

    @Override
    protected void doFill(ForegroundFill fill) {
        switch (fill) {
            case NO_FILL:
                style.setFillPattern(FillPatternType.NO_FILL);
                break;
            case SOLID_FOREGROUND:
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                break;
            case FINE_DOTS:
                style.setFillPattern(FillPatternType.FINE_DOTS);
                break;
            case ALT_BARS:
                style.setFillPattern(FillPatternType.ALT_BARS);
                break;
            case SPARSE_DOTS:
                style.setFillPattern(FillPatternType.SPARSE_DOTS);
                break;
            case THICK_HORZ_BANDS:
                style.setFillPattern(FillPatternType.THICK_HORZ_BANDS);
                break;
            case THICK_VERT_BANDS:
                style.setFillPattern(FillPatternType.THICK_VERT_BANDS);
                break;
            case THICK_BACKWARD_DIAG:
                style.setFillPattern(FillPatternType.THICK_BACKWARD_DIAG);
                break;
            case THICK_FORWARD_DIAG:
                style.setFillPattern(FillPatternType.THICK_FORWARD_DIAG);
                break;
            case BIG_SPOTS:
                style.setFillPattern(FillPatternType.BIG_SPOTS);
                break;
            case BRICKS:
                style.setFillPattern(FillPatternType.BRICKS);
                break;
            case THIN_HORZ_BANDS:
                style.setFillPattern(FillPatternType.THIN_HORZ_BANDS);
                break;
            case THIN_VERT_BANDS:
                style.setFillPattern(FillPatternType.THIN_VERT_BANDS);
                break;
            case THIN_BACKWARD_DIAG:
                style.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
                break;
            case THIN_FORWARD_DIAG:
                style.setFillPattern(FillPatternType.THIN_FORWARD_DIAG);
                break;
            case SQUARES:
                style.setFillPattern(FillPatternType.SQUARES);
                break;
            case DIAMONDS:
                style.setFillPattern(FillPatternType.DIAMONDS);
                break;
        }
    }

    @Override
    protected FontDefinition createFont() {
        return new PoiFontDefinition(getWorkbook().getWorkbook(), style);
    }

    @Override
    protected void doIndent(int indent) {
        style.setIndention((short) indent);
    }

    @Override
    protected void doWrapText() {
        style.setWrapText(true);
    }

    @Override
    protected void doRotation(int rotation) {
        style.setRotation((short) rotation);
    }

    @Override
    protected void doFormat(String format) {
        DataFormat dataFormat = getWorkbook().getWorkbook().createDataFormat();
        style.setDataFormat(dataFormat.getFormat(format));
    }

    private PoiWorkbookDefinition getWorkbook() {
        return (PoiWorkbookDefinition) workbook;
    }

    @Override
    protected void doAlign(Keywords.VerticalAlignment verticalAlignment, Keywords.HorizontalAlignment horizontalAlignment) {
        if (Keywords.VerticalAndHorizontalAlignment.CENTER.equals(verticalAlignment)) {
            style.setVerticalAlignment(VerticalAlignment.CENTER);
        } else if (Keywords.PureVerticalAlignment.DISTRIBUTED.equals(verticalAlignment)) {
            style.setVerticalAlignment(VerticalAlignment.DISTRIBUTED);
        } else if (Keywords.VerticalAndHorizontalAlignment.JUSTIFY.equals(verticalAlignment)) {
            style.setVerticalAlignment(VerticalAlignment.JUSTIFY);
        } else if (Keywords.BorderSideAndVerticalAlignment.TOP.equals(verticalAlignment)) {
            style.setVerticalAlignment(VerticalAlignment.TOP);
        } else if (Keywords.BorderSideAndVerticalAlignment.BOTTOM.equals(verticalAlignment)) {
            style.setVerticalAlignment(VerticalAlignment.BOTTOM);
        } else {
            throw new IllegalArgumentException(String.valueOf(verticalAlignment) + " is not supported!");
        }
        if (Keywords.HorizontalAlignment.RIGHT.equals(horizontalAlignment)) {
            style.setAlignment(HorizontalAlignment.RIGHT);
        } else if (Keywords.HorizontalAlignment.LEFT.equals(horizontalAlignment)) {
            style.setAlignment(HorizontalAlignment.LEFT);
        } else if (Keywords.HorizontalAlignment.GENERAL.equals(horizontalAlignment)) {
            style.setAlignment(HorizontalAlignment.GENERAL);
        } else if (Keywords.HorizontalAlignment.CENTER.equals(horizontalAlignment)) {
            style.setAlignment(HorizontalAlignment.CENTER);
        } else if (Keywords.HorizontalAlignment.FILL.equals(horizontalAlignment)) {
            style.setAlignment(HorizontalAlignment.FILL);
        } else if (Keywords.HorizontalAlignment.JUSTIFY.equals(horizontalAlignment)) {
            style.setAlignment(HorizontalAlignment.JUSTIFY);
        } else if (Keywords.HorizontalAlignment.CENTER_SELECTION.equals(horizontalAlignment)) {
            style.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        }
    }

    @Override
    protected AbstractBorderDefinition createBorder() {
        return new PoiBorderDefinition(style);
    }

    @Override
    protected void assignTo(AbstractCellDefinition cell) {
        if (cell instanceof PoiCellDefinition) {
            PoiCellDefinition poiCellDefinition = (PoiCellDefinition) cell;
            poiCellDefinition.getCell().setCellStyle(style);
        } else {
            throw new IllegalArgumentException("Cell not supported: " + cell);
        }
    }

    public static XSSFColor parseColor(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("Please, provide the color in '#abcdef' hex string format");
        }


        Matcher match = Pattern.compile("#([\\dA-F]{2})([\\dA-F]{2})([\\dA-F]{2})").matcher(hex.toUpperCase());

        if (!match.matches()) {
            throw new IllegalArgumentException("Cannot parse color " + hex + ". Please, provide the color in \'#abcdef\' hex string format");
        }


        byte red = (byte) Integer.parseInt(match.group(1), 16);
        byte green = (byte) Integer.parseInt(match.group(2), 16);
        byte blue = (byte) Integer.parseInt(match.group(3), 16);

        return new XSSFColor(new byte[]{red, green, blue}, INDEXED_COLOR_MAP);
    }

    void setBorderTo(CellRangeAddress address, PoiSheetDefinition sheet) {
        RegionUtil.setBorderBottom(style.getBorderBottom(), address, sheet.getSheet());
        RegionUtil.setBorderLeft(style.getBorderLeft(), address, sheet.getSheet());
        RegionUtil.setBorderRight(style.getBorderRight(), address, sheet.getSheet());
        RegionUtil.setBorderTop(style.getBorderTop(), address, sheet.getSheet());
        RegionUtil.setBottomBorderColor(style.getBottomBorderColor(), address, sheet.getSheet());
        RegionUtil.setLeftBorderColor(style.getLeftBorderColor(), address, sheet.getSheet());
        RegionUtil.setRightBorderColor(style.getRightBorderColor(), address, sheet.getSheet());
        RegionUtil.setTopBorderColor(style.getTopBorderColor(), address, sheet.getSheet());
    }

    private final XSSFCellStyle style;
}
