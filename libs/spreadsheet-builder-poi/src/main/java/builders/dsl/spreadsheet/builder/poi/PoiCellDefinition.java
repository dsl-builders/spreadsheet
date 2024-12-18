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

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.*;
import builders.dsl.spreadsheet.impl.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

class PoiCellDefinition extends AbstractCellDefinition {

    PoiCellDefinition(PoiRowDefinition row, Cell cell) {
        super(row);
        this.cell = Objects.requireNonNull(cell, "Cell");
    }

    @Override
    public PoiCellDefinition value(Object value) {
        if (value == null) {
            cell.setBlank();
            return this;
        }

        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
            return this;
        }

        if (value instanceof Date) {
            cell.setCellValue((Date) value);
            return this;
        }

        if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
            return this;
        }
        
        if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
            return this;
        }
        
        if (value instanceof LocalTime) {
            cell.setCellValue(LocalDateTime.of(LocalDate.of(1900, 1, 1), (LocalTime) value));
            return this;
        }

        if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
            return this;
        }
        
        if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
            return this;
        }

        cell.setCellValue(value.toString());
        return this;
    }

    @Override
    protected AbstractPendingFormula createPendingFormula(String formula) {
        return new PoiPendingFormula(this, formula);
    }

    @Override
    protected AbstractCellStyleDefinition createCellStyle() {
        return new PoiCellStyleDefinition(this);
    }

    @Override
    protected void assignStyle(CellStyleDefinition cellStyle) {
        if (cellStyle instanceof PoiCellStyleDefinition) {
            PoiCellStyleDefinition style = (PoiCellStyleDefinition) cellStyle;
            style.assignTo(this);
        } else {
            throw new IllegalArgumentException("Unsupported style: " + cellStyle);
        }
    }

    @Override
    protected void doName(String name) {
        Name theName = cell.getRow().getSheet().getWorkbook().createName();
        theName.setNameName(name);
        theName.setRefersToFormula(generateRefersToFormula());
    }

    @Override
    protected LinkDefinition createLinkDefinition() {
        return new PoiLinkDefinition(this.getRow().getSheet().getWorkbook(), this);
    }

    @Override
    protected FontDefinition createFontDefinition() {
        return new PoiFontDefinition(this.getRow().getSheet().getWorkbook().getWorkbook());
    }

    @Override
    protected void applyComment(DefaultCommentDefinition comment) {
        applyTo(comment, cell);
    }

    private String generateRefersToFormula() {
        return "'" + cell.getSheet().getSheetName().replaceAll("'", "\\'") + "'!" + getRow().getSheet().getWorkbook().getReference(cell);
    }

    @Override
    public DimensionModifier width(double width) {
        getRow().getSheet().getSheet().setColumnWidth(cell.getColumnIndex(), (int) Math.round(width * 255D));
        return new WidthModifier(this, width, WIDTH_POINTS_PER_CM, WIDTH_POINTS_PER_INCH);
    }

    @Override
    public DimensionModifier height(double height) {
        getRow().getRow().setHeightInPoints((float) height);
        return new HeightModifier(this, height, HEIGHT_POINTS_PER_CM, HEIGHT_POINTS_PER_INCH);
    }

    @Override
    public PoiCellDefinition width(Keywords.Auto auto) {
        getRow().getSheet().addAutoColumn(cell.getColumnIndex());
        return this;
    }

    @Override
    public ImageCreator png(Keywords.Image image) {
        return createImageConfigurer(Workbook.PICTURE_TYPE_PNG);
    }

    @Override
    public ImageCreator jpeg(Keywords.Image image) {
        return createImageConfigurer(Workbook.PICTURE_TYPE_JPEG);
    }

    @Override
    public ImageCreator pict(Keywords.Image image) {
        return createImageConfigurer(Workbook.PICTURE_TYPE_JPEG);
    }

    @Override
    public ImageCreator emf(Keywords.Image image) {
        return createImageConfigurer(Workbook.PICTURE_TYPE_EMF);
    }

    @Override
    public ImageCreator wmf(Keywords.Image image) {
        return createImageConfigurer(Workbook.PICTURE_TYPE_WMF);
    }

    @Override
    public ImageCreator dib(Keywords.Image image) {
        return createImageConfigurer(Workbook.PICTURE_TYPE_DIB);
    }

    private ImageCreator createImageConfigurer(int fileType) {
        return new PoiImageCreator(this, fileType);
    }

    protected Cell getCell() {
        return cell;
    }

    @Override
    public void resolve() {
        if (richTextParts != null && richTextParts.size() > 0) {

            List<String> texts = new ArrayList<String>();

            for(RichTextPart part: richTextParts) {
                texts.add(part.getText());
            }

            Workbook wb = getRow().getSheet().getWorkbook().getWorkbook();
            CreationHelper factory = wb.getCreationHelper();
            RichTextString text = factory.createRichTextString(Utils.join(texts, ""));

            for (RichTextPart richTextPart : richTextParts) {
                if (richTextPart.getText() != null && richTextPart.getText().length() > 0 && richTextPart.getFont() != null) {
                    text.applyFont(richTextPart.getStart(), richTextPart.getEnd(), ((PoiFontDefinition)richTextPart.getFont()).getFont());
                }
            }

            cell.setCellValue(text);
        }

        if ((getColspan() > 1 || getRowspan() > 1) && cellStyle != null && cellStyle instanceof PoiCellStyleDefinition) {
            ((PoiCellStyleDefinition) cellStyle).setBorderTo(getCellRangeAddress(), getRow().getSheet());
            // XXX: setting border messes up the style of the cell
            ((PoiCellStyleDefinition) cellStyle).assignTo(this);
        }

    }

    CellRangeAddress getCellRangeAddress() {
        return new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex() + getRowspan() - 1, cell.getColumnIndex(), cell.getColumnIndex() + getColspan() - 1);
    }

    @Override
    public String toString() {
        return "Cell[" + getRow().getSheet().getName() + "!" + getRow().getSheet().getWorkbook().getReference(cell) + getRow().getNumber() + "]=" + cell.toString();
    }

    private static void applyTo(DefaultCommentDefinition comment, Cell cell) {
        if (comment.getText() == null) {
            throw new IllegalStateException("Comment text has not been set!");
        }

        Comment xssfComment = cell.getCellComment();

        Workbook wb = cell.getRow().getSheet().getWorkbook();
        CreationHelper factory = wb.getCreationHelper();

        if (xssfComment == null) {

            Drawing<?> drawing = cell.getRow().getSheet().createDrawingPatriarch();

            ClientAnchor anchor = factory.createClientAnchor();
            anchor.setCol1(cell.getColumnIndex());
            anchor.setCol2(cell.getColumnIndex() + comment.getWidth());
            anchor.setRow1(cell.getRow().getRowNum());
            anchor.setRow2(cell.getRow().getRowNum() + comment.getHeight());

            // Create the comment and set the text+author
            xssfComment = drawing.createCellComment(anchor);
        }

        xssfComment.setString(factory.createRichTextString(comment.getText()));

        if (comment.getAuthor() != null) {
            xssfComment.setAuthor(comment.getAuthor());
        }


        // Assign the comment to the cell
        cell.setCellComment(xssfComment);
    }

    public PoiRowDefinition getRow() {
        return (PoiRowDefinition) super.getRow();
    }

    private static final double WIDTH_POINTS_PER_CM = 4.6666666666666666666667;
    private static final double WIDTH_POINTS_PER_INCH = 12;
    private static final double HEIGHT_POINTS_PER_CM = 28;
    private static final double HEIGHT_POINTS_PER_INCH = 72;

    private final Cell cell;
}
