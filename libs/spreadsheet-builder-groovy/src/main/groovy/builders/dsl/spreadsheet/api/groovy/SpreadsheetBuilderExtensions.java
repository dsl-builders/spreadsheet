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
package builders.dsl.spreadsheet.api.groovy;

import builders.dsl.spreadsheet.api.BorderPositionProvider;
import builders.dsl.spreadsheet.api.BorderStyle;
import builders.dsl.spreadsheet.api.BorderStyleProvider;
import builders.dsl.spreadsheet.api.Cell;
import builders.dsl.spreadsheet.api.Color;
import builders.dsl.spreadsheet.api.ColorProvider;
import builders.dsl.spreadsheet.api.DataRow;
import builders.dsl.spreadsheet.api.FontStyle;
import builders.dsl.spreadsheet.api.FontStylesProvider;
import builders.dsl.spreadsheet.api.ForegroundFill;
import builders.dsl.spreadsheet.api.ForegroundFillProvider;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.api.PageSettingsProvider;
import builders.dsl.spreadsheet.api.SheetStateProvider;
import builders.dsl.spreadsheet.builder.api.BorderDefinition;
import builders.dsl.spreadsheet.builder.api.CanDefineStyle;
import builders.dsl.spreadsheet.builder.api.CellDefinition;
import builders.dsl.spreadsheet.builder.api.CellStyleDefinition;
import builders.dsl.spreadsheet.builder.api.CommentDefinition;
import builders.dsl.spreadsheet.builder.api.DimensionModifier;
import builders.dsl.spreadsheet.builder.api.FontDefinition;
import builders.dsl.spreadsheet.builder.api.HasStyle;
import builders.dsl.spreadsheet.builder.api.PageDefinition;
import builders.dsl.spreadsheet.builder.api.RowDefinition;
import builders.dsl.spreadsheet.builder.api.SheetDefinition;
import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder;
import builders.dsl.spreadsheet.builder.api.WorkbookDefinition;
import builders.dsl.spreadsheet.query.api.BorderCriterion;
import builders.dsl.spreadsheet.query.api.CellCriterion;
import builders.dsl.spreadsheet.query.api.CellStyleCriterion;
import builders.dsl.spreadsheet.query.api.FontCriterion;
import builders.dsl.spreadsheet.query.api.PageCriterion;
import builders.dsl.spreadsheet.query.api.RowCriterion;
import builders.dsl.spreadsheet.query.api.SheetCriterion;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteriaResult;
import builders.dsl.spreadsheet.query.api.WorkbookCriterion;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.FromString;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import space.jasan.support.groovy.closure.ConsumerWithDelegate;

import java.io.FileNotFoundException;

/**
 * Main purpose of this class is to provide additional context for IDEs and static type checking.
 */
@SuppressWarnings("unused")
public class SpreadsheetBuilderExtensions {

    private SpreadsheetBuilderExtensions() {
    }

    public static CellDefinition value(CellDefinition self, CharSequence sequence) {
        return self.value(StringGroovyMethods.stripIndent(sequence).trim());
    }

    public static CanDefineStyle style(CanDefineStyle stylable, String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.CellStyleDefinition") Closure<?> styleDefinition) {
        return stylable.style(name, ConsumerWithDelegate.create(styleDefinition));
    }

    public static CellDefinition comment(CellDefinition cellDefinition, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CommentDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.CommentDefinition") Closure<?> commentDefinition) {
        return cellDefinition.comment(ConsumerWithDelegate.create(commentDefinition));
    }

    public static CellDefinition text(CellDefinition cellDefinition, String text, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = FontDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.FontDefinition") Closure<?> fontConfiguration) {
        return cellDefinition.text(text, ConsumerWithDelegate.create(fontConfiguration));
    }

    public static CellStyleDefinition font(CellStyleDefinition style, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = FontDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.FontDefinition") Closure<?> fontConfiguration) {
        return style.font(ConsumerWithDelegate.create(fontConfiguration));
    }

    /**
     * Configures all the borders of the cell.
     *
     * @param borderConfiguration border configuration closure
     */
    public static CellStyleDefinition border(CellStyleDefinition style, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.BorderDefinition") Closure<?> borderConfiguration) {
        return style.border(ConsumerWithDelegate.create(borderConfiguration));
    }

    /**
     * Configures one border of the cell.
     *
     * @param location            border to be configured
     * @param borderConfiguration border configuration closure
     */
    public static CellStyleDefinition border(CellStyleDefinition style, Keywords.BorderSide location, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.BorderDefinition") Closure<?> borderConfiguration) {
        return style.border(location, ConsumerWithDelegate.create(borderConfiguration));
    }

    /**
     * Configures two borders of the cell.
     *
     * @param first               first border to be configured
     * @param second              second border to be configured
     * @param borderConfiguration border configuration closure
     */
    public static CellStyleDefinition border(CellStyleDefinition style, Keywords.BorderSide first, Keywords.BorderSide second, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.BorderDefinition") Closure<?> borderConfiguration) {
        return style.border(first, second, ConsumerWithDelegate.create(borderConfiguration));
    }

    /**
     * Configures three borders of the cell.
     *
     * @param first               first border to be configured
     * @param second              second border to be configured
     * @param third               third border to be configured
     * @param borderConfiguration border configuration closure
     */
    public static CellStyleDefinition border(CellStyleDefinition style, Keywords.BorderSide first, Keywords.BorderSide second, Keywords.BorderSide third, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.BorderDefinition") Closure<?> borderConfiguration) {
        return style.border(first, second, third, ConsumerWithDelegate.create(borderConfiguration));
    }

    /**
     * Applies a customized named style to the current element.
     *
     * @param name            the name of the style
     * @param styleDefinition the definition of the style customizing the predefined style
     */
    public static HasStyle style(HasStyle stylable, String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.CellStyleDefinition") Closure<?> styleDefinition) {
        return stylable.style(name, ConsumerWithDelegate.create(styleDefinition));
    }

    /**
     * Applies a customized named style to the current element.
     *
     * @param names           the names of the styles
     * @param styleDefinition the definition of the style customizing the predefined style
     */
    public static HasStyle styles(HasStyle stylable, Iterable<String> names, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.CellStyleDefinition") Closure<?> styleDefinition) {
        return stylable.styles(names, ConsumerWithDelegate.create(styleDefinition));
    }

    /**
     * Applies the style defined by the closure to the current element.
     *
     * @param styleDefinition the definition of the style
     */
    public static HasStyle style(HasStyle stylable, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.CellStyleDefinition") Closure<?> styleDefinition) {
        return stylable.style(ConsumerWithDelegate.create(styleDefinition));
    }

    public static RowDefinition cell(RowDefinition row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.CellDefinition") Closure<?> cellDefinition) {
        return row.cell(ConsumerWithDelegate.create(cellDefinition));
    }

    public static RowDefinition cell(RowDefinition row, int column, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.CellDefinition") Closure<?> cellDefinition) {
        return row.cell(column, ConsumerWithDelegate.create(cellDefinition));
    }

    public static RowDefinition cell(RowDefinition row, String column, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.CellDefinition") Closure<?> cellDefinition) {
        return row.cell(column, ConsumerWithDelegate.create(cellDefinition));
    }

    public static RowDefinition group(RowDefinition row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.RowDefinition") Closure<?> insideGroupDefinition) {
        return row.group(ConsumerWithDelegate.create(insideGroupDefinition));
    }

    public static RowDefinition collapse(RowDefinition row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.RowDefinition") Closure<?> insideGroupDefinition) {
        return row.collapse(ConsumerWithDelegate.create(insideGroupDefinition));
    }

    /**
     * Creates new row in the spreadsheet.
     *
     * @param rowDefinition closure defining the content of the row
     */
    public static SheetDefinition row(SheetDefinition sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.RowDefinition") Closure<?> rowDefinition) {
        return sheet.row(ConsumerWithDelegate.create(rowDefinition));
    }

    /**
     * Creates new row in the spreadsheet.
     *
     * @param row           row number (1 based - the same as is shown in the file)
     * @param rowDefinition closure defining the content of the row
     */
    public static SheetDefinition row(SheetDefinition sheet, int row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.RowDefinition") Closure<?> rowDefinition) {
        return sheet.row(row, ConsumerWithDelegate.create(rowDefinition));
    }

    public static SheetDefinition group(SheetDefinition sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.SheetDefinition") Closure<?> insideGroupDefinition) {
        return sheet.group(ConsumerWithDelegate.create(insideGroupDefinition));
    }

    public static SheetDefinition collapse(SheetDefinition sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.SheetDefinition") Closure<?> insideGroupDefinition) {
        return sheet.collapse(ConsumerWithDelegate.create(insideGroupDefinition));
    }

    /**
     * Configures the basic page settings.
     *
     * @param pageDefinition closure defining the page settings
     */
    public static SheetDefinition page(SheetDefinition sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PageDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.PageDefinition") Closure<?> pageDefinition) {
        return sheet.page(ConsumerWithDelegate.create(pageDefinition));
    }

    public static SheetDefinition state(SheetDefinition self, Keywords.SheetState state) {
        return self.state(state);
    }

    public static SheetCriterion state(SheetCriterion self, Keywords.SheetState state) {
        return self.state(state);
    }

    public static void build(SpreadsheetBuilder builder, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.WorkbookDefinition") Closure<?> workbookDefinition) {
        builder.build(ConsumerWithDelegate.create(workbookDefinition));
    }

    public static WorkbookDefinition sheet(WorkbookDefinition workbook, String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetDefinition.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.builder.api.SheetDefinition") Closure<?> sheetDefinition) {
        return workbook.sheet(name, ConsumerWithDelegate.create(sheetDefinition));
    }

    public static CellCriterion style(CellCriterion cell, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.CellStyleCriterion") Closure<?> styleCriterion) {
        return cell.style(ConsumerWithDelegate.create(styleCriterion));
    }

    public static CellCriterion or(CellCriterion cell, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.CellCriterion") Closure<?> sheetCriterion) {
        return cell.or(ConsumerWithDelegate.create(sheetCriterion));
    }

    public static CellStyleCriterion font(CellStyleCriterion style, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = FontCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.FontCriterion") Closure<?> fontCriterion) {
        return style.font(ConsumerWithDelegate.create(fontCriterion));
    }

    /**
     * Configures all the borders of the cell.
     *
     * @param borderConfiguration border configuration closure
     */
    public static CellStyleCriterion border(CellStyleCriterion style, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.BorderCriterion") Closure<?> borderConfiguration) {
        return style.border(ConsumerWithDelegate.create(borderConfiguration));
    }

    /**
     * Configures one border of the cell.
     *
     * @param location            border to be configured
     * @param borderConfiguration border configuration closure
     */
    public static CellStyleCriterion border(CellStyleCriterion style, Keywords.BorderSide location, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.BorderCriterion") Closure<?> borderConfiguration) {
        return style.border(location, ConsumerWithDelegate.create(borderConfiguration));
    }

    /**
     * Configures two borders of the cell.
     *
     * @param first               first border to be configured
     * @param second              second border to be configured
     * @param borderConfiguration border configuration closure
     */
    public static CellStyleCriterion border(CellStyleCriterion style, Keywords.BorderSide first, Keywords.BorderSide second, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.BorderCriterion") Closure<?> borderConfiguration) {
        return style.border(first, second, ConsumerWithDelegate.create(borderConfiguration));
    }

    /**
     * Configures three borders of the cell.
     *
     * @param first               first border to be configured
     * @param second              second border to be configured
     * @param third               third border to be configured
     * @param borderConfiguration border configuration closure
     */
    public static CellStyleCriterion border(CellStyleCriterion style, Keywords.BorderSide first, Keywords.BorderSide second, Keywords.BorderSide third, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.BorderCriterion") Closure<?> borderConfiguration) {
        return style.border(first, second, third, ConsumerWithDelegate.create(borderConfiguration));
    }

    public static RowCriterion cell(RowCriterion row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.CellCriterion") Closure<?> cellCriterion) {
        return row.cell(ConsumerWithDelegate.create(cellCriterion));
    }

    public static RowCriterion cell(RowCriterion row, int column, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.CellCriterion") Closure<?> cellCriterion) {
        return row.cell(column, ConsumerWithDelegate.create(cellCriterion));
    }

    public static RowCriterion cell(RowCriterion row, String column, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.CellCriterion") Closure<?> cellCriterion) {
        return row.cell(column, ConsumerWithDelegate.create(cellCriterion));
    }

    public static RowCriterion cell(RowCriterion row, int from, int to, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.CellCriterion") Closure<?> cellCriterion) {
        return row.cell(from, to, ConsumerWithDelegate.create(cellCriterion));
    }

    public static RowCriterion cell(RowCriterion row, String from, String to, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.CellCriterion") Closure<?> cellCriterion) {
        return row.cell(from, to, ConsumerWithDelegate.create(cellCriterion));
    }

    public static RowCriterion or(RowCriterion row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.RowCriterion") Closure<?> rowCriterion) {
        return row.or(ConsumerWithDelegate.create(rowCriterion));
    }

    public static SheetCriterion row(SheetCriterion sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.RowCriterion") Closure<?> rowCriterion) {
        return sheet.row(ConsumerWithDelegate.create(rowCriterion));
    }

    public static SheetCriterion row(SheetCriterion sheet, int row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.RowCriterion") Closure<?> rowCriterion) {
        return sheet.row(row, ConsumerWithDelegate.create(rowCriterion));
    }

    public static SheetCriterion page(SheetCriterion sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PageCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.PageCriterion") Closure<?> pageCriterion) {
        return sheet.page(ConsumerWithDelegate.create(pageCriterion));
    }

    public static SheetCriterion or(SheetCriterion sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.SheetCriterion") Closure<?> sheetCriterion) {
        return sheet.or(ConsumerWithDelegate.create(sheetCriterion));
    }

    public static boolean asBoolean(SpreadsheetCriteriaResult result) {
        return DefaultGroovyMethods.asBoolean(result.getSheets()) || DefaultGroovyMethods.asBoolean(result.getRows()) || DefaultGroovyMethods.asBoolean(result.getCells());
    }

    public static SpreadsheetCriteriaResult query(SpreadsheetCriteria criteria, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.WorkbookCriterion") Closure<?> workbookCriterion) throws FileNotFoundException {
        return criteria.query(ConsumerWithDelegate.create(workbookCriterion));
    }

    public static Cell find(SpreadsheetCriteria criteria, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.WorkbookCriterion") Closure<?> workbookCriterion) throws FileNotFoundException {
        return criteria.find(ConsumerWithDelegate.create(workbookCriterion));
    }

    public static boolean exists(SpreadsheetCriteria criteria, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.WorkbookCriterion") Closure<?> workbookCriterion) throws FileNotFoundException {
        return criteria.exists(ConsumerWithDelegate.create(workbookCriterion));
    }

    public static WorkbookCriterion sheet(WorkbookCriterion workbook, String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.SheetCriterion") Closure<?> sheetCriterion) {
        return workbook.sheet(name, ConsumerWithDelegate.create(sheetCriterion));
    }

    public static WorkbookCriterion sheet(WorkbookCriterion workbook, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.SheetCriterion") Closure<?> sheetCriterion) {
        return workbook.sheet(ConsumerWithDelegate.create(sheetCriterion));
    }

    public static WorkbookCriterion or(WorkbookCriterion workbook, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookCriterion.class) @ClosureParams(value = FromString.class, options = "builders.dsl.spreadsheet.query.api.WorkbookCriterion") Closure<?> workbookCriterion) {
        return workbook.or(ConsumerWithDelegate.create(workbookCriterion));
    }

    public static Cell getAt(DataRow self, String name) {
        return self.get(name);
    }

    /**
     * Converts the dimension to centimeters.
     * <p>
     * This feature is currently experimental.
     */
    public static CellDefinition getCm(DimensionModifier self) {
        return self.cm();
    }

    /**
     * Converts the dimension to inches.
     * <p>
     * This feature is currently experimental.
     */
    public static CellDefinition getInch(DimensionModifier self) {
        return self.inch();
    }

    /**
     * Converts the dimension to inches.
     * <p>
     * This feature is currently experimental.
     */
    public static CellDefinition getInches(DimensionModifier self) {
        return self.inches();
    }

    /**
     * Keeps the dimesion in points.
     * <p>
     * This feature is currently experimental.
     */
    public static CellDefinition getPoints(DimensionModifier self) {
        return self.points();
    }

    @SuppressWarnings("unused")
    public static BorderStyle getNone(BorderStyleProvider ignored) {
        return BorderStyle.NONE;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getThin(BorderStyleProvider ignored) {
        return BorderStyle.THIN;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getMedium(BorderStyleProvider ignored) {
        return BorderStyle.MEDIUM;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getDashed(BorderStyleProvider ignored) {
        return BorderStyle.DASHED;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getDotted(BorderStyleProvider ignored) {
        return BorderStyle.DOTTED;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getThick(BorderStyleProvider ignored) {
        return BorderStyle.THICK;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getDouble(BorderStyleProvider ignored) {
        return BorderStyle.DOUBLE;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getHair(BorderStyleProvider ignored) {
        return BorderStyle.HAIR;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getMediumDashed(BorderStyleProvider ignored) {
        return BorderStyle.MEDIUM_DASHED;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getDashDot(BorderStyleProvider ignored) {
        return BorderStyle.DASH_DOT;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getMediumDashDot(BorderStyleProvider ignored) {
        return BorderStyle.MEDIUM_DASH_DOT;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getDashDotDot(BorderStyleProvider ignored) {
        return BorderStyle.DASH_DOT_DOT;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getMediumDashDotDot(BorderStyleProvider ignored) {
        return BorderStyle.MEDIUM_DASH_DOT_DOT;
    }

    @SuppressWarnings("unused")
    public static BorderStyle getSlantedDashDot(BorderStyleProvider ignored) {
        return BorderStyle.SLANTED_DASH_DOT;
    }

    @SuppressWarnings("unused")
    public static Keywords.Auto getAuto(CellDefinition ignored) {
        return Keywords.Auto.AUTO;
    }

    @SuppressWarnings("unused")
    public static Keywords.To getTo(CellDefinition ignored) {
        return Keywords.To.TO;
    }

    @SuppressWarnings("unused")
    public static Keywords.Image getImage(CellDefinition ignored) {
        return Keywords.Image.IMAGE;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getNoFill(ForegroundFillProvider ignored) {
        return ForegroundFill.NO_FILL;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getSolidForeground(ForegroundFillProvider ignored) {
        return ForegroundFill.SOLID_FOREGROUND;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getFineDots(ForegroundFillProvider ignored) {
        return ForegroundFill.FINE_DOTS;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getAltBars(ForegroundFillProvider ignored) {
        return ForegroundFill.ALT_BARS;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getSparseDots(ForegroundFillProvider ignored) {
        return ForegroundFill.SPARSE_DOTS;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getThickHorizontalBands(ForegroundFillProvider ignored) {
        return ForegroundFill.THICK_HORZ_BANDS;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getThickVerticalBands(ForegroundFillProvider ignored) {
        return ForegroundFill.THICK_VERT_BANDS;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getThickBackwardDiagonals(ForegroundFillProvider ignored) {
        return ForegroundFill.THICK_BACKWARD_DIAG;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getThickForwardDiagonals(ForegroundFillProvider ignored) {
        return ForegroundFill.THICK_FORWARD_DIAG;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getBigSpots(ForegroundFillProvider ignored) {
        return ForegroundFill.BIG_SPOTS;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getBricks(ForegroundFillProvider ignored) {
        return ForegroundFill.BRICKS;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getThinHorizontalBands(ForegroundFillProvider ignored) {
        return ForegroundFill.THIN_HORZ_BANDS;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getThinVerticalBands(ForegroundFillProvider ignored) {
        return ForegroundFill.THIN_VERT_BANDS;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getThinBackwardDiagonals(ForegroundFillProvider ignored) {
        return ForegroundFill.THIN_BACKWARD_DIAG;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getThinForwardDiagonals(ForegroundFillProvider ignored) {
        return ForegroundFill.THICK_FORWARD_DIAG;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getSquares(ForegroundFillProvider ignored) {
        return ForegroundFill.SQUARES;
    }

    @SuppressWarnings("unused")
    public static ForegroundFill getDiamonds(ForegroundFillProvider ignored) {
        return ForegroundFill.DIAMONDS;
    }

    @SuppressWarnings("unused")
    public static Keywords.BorderSideAndHorizontalAlignment getLeft(CellStyleDefinition ignored) {
        return Keywords.BorderSideAndHorizontalAlignment.LEFT;
    }

    @SuppressWarnings("unused")
    public static Keywords.BorderSideAndHorizontalAlignment getRight(CellStyleDefinition ignored) {
        return Keywords.BorderSideAndHorizontalAlignment.RIGHT;
    }

    @SuppressWarnings("unused")
    public static Keywords.BorderSideAndVerticalAlignment getTop(CellStyleDefinition ignored) {
        return Keywords.BorderSideAndVerticalAlignment.TOP;
    }

    @SuppressWarnings("unused")
    public static Keywords.BorderSideAndVerticalAlignment getBottom(CellStyleDefinition ignored) {
        return Keywords.BorderSideAndVerticalAlignment.BOTTOM;
    }

    @SuppressWarnings("unused")
    public static Keywords.VerticalAndHorizontalAlignment getCenter(CellStyleDefinition ignored) {
        return Keywords.VerticalAndHorizontalAlignment.CENTER;
    }

    @SuppressWarnings("unused")
    public static Keywords.VerticalAndHorizontalAlignment getJustify(CellStyleDefinition ignored) {
        return Keywords.VerticalAndHorizontalAlignment.JUSTIFY;
    }

    @SuppressWarnings("unused")
    public static Keywords.PureVerticalAlignment getDistributed(CellStyleDefinition ignored) {
        return Keywords.PureVerticalAlignment.DISTRIBUTED;
    }

    @SuppressWarnings("unused")
    public static Keywords.Text getText(CellStyleDefinition ignored) {
        return Keywords.Text.WRAP;
    }

    @SuppressWarnings("unused")
    public static Keywords.Orientation getPortrait(PageSettingsProvider ignored) {
        return Keywords.Orientation.PORTRAIT;
    }

    @SuppressWarnings("unused")
    public static Keywords.Orientation getLandscape(PageSettingsProvider ignored) {
        return Keywords.Orientation.LANDSCAPE;
    }

    @SuppressWarnings("unused")
    public static Keywords.Fit getWidth(PageSettingsProvider ignored) {
        return Keywords.Fit.WIDTH;
    }

    @SuppressWarnings("unused")
    public static Keywords.Fit getHeight(PageSettingsProvider ignored) {
        return Keywords.Fit.HEIGHT;
    }

    @SuppressWarnings("unused")
    public static Keywords.To getTo(PageSettingsProvider ignored) {
        return Keywords.To.TO;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getLetter(PageSettingsProvider ignored) {
        return Keywords.Paper.LETTER;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getLetterSmall(PageSettingsProvider ignored) {
        return Keywords.Paper.LETTER_SMALL;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getTabloid(PageSettingsProvider ignored) {
        return Keywords.Paper.TABLOID;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getLedger(PageSettingsProvider ignored) {
        return Keywords.Paper.LEDGER;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getLegal(PageSettingsProvider ignored) {
        return Keywords.Paper.LEGAL;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getStatement(PageSettingsProvider ignored) {
        return Keywords.Paper.STATEMENT;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getExecutive(PageSettingsProvider ignored) {
        return Keywords.Paper.EXECUTIVE;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getA3(PageSettingsProvider ignored) {
        return Keywords.Paper.A3;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getA4(PageSettingsProvider ignored) {
        return Keywords.Paper.A4;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getA4Small(PageSettingsProvider ignored) {
        return Keywords.Paper.A4_SMALL;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getA5(PageSettingsProvider ignored) {
        return Keywords.Paper.A5;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getB4(PageSettingsProvider ignored) {
        return Keywords.Paper.B4;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getB5(PageSettingsProvider ignored) {
        return Keywords.Paper.B5;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getFolio(PageSettingsProvider ignored) {
        return Keywords.Paper.FOLIO;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getQuarto(PageSettingsProvider ignored) {
        return Keywords.Paper.QUARTO;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getStandard10x14(PageSettingsProvider ignored) {
        return Keywords.Paper.STANDARD_10_14;
    }

    @SuppressWarnings("unused")
    public static Keywords.Paper getStandard11x17(PageSettingsProvider ignored) {
        return Keywords.Paper.STANDARD_11_17;
    }

    @SuppressWarnings("unused")
    public static FontStyle getItalic(FontStylesProvider ignored) {
        return FontStyle.ITALIC;
    }

    @SuppressWarnings("unused")
    public static FontStyle getBold(FontStylesProvider ignored) {
        return FontStyle.BOLD;
    }

    @SuppressWarnings("unused")
    public static FontStyle getStrikeout(FontStylesProvider ignored) {
        return FontStyle.STRIKEOUT;
    }

    @SuppressWarnings("unused")
    public static FontStyle getUnderline(FontStylesProvider ignored) {
        return FontStyle.UNDERLINE;
    }

    @SuppressWarnings("unused")
    public static Keywords.BorderSideAndHorizontalAlignment getLeft(BorderPositionProvider ignored) {
        return Keywords.BorderSideAndHorizontalAlignment.LEFT;
    }

    @SuppressWarnings("unused")
    public static Keywords.BorderSideAndHorizontalAlignment getRight(BorderPositionProvider ignored) {
        return Keywords.BorderSideAndHorizontalAlignment.RIGHT;
    }

    @SuppressWarnings("unused")
    public static Keywords.BorderSideAndVerticalAlignment getTop(BorderPositionProvider ignored) {
        return Keywords.BorderSideAndVerticalAlignment.TOP;
    }

    @SuppressWarnings("unused")
    public static Keywords.BorderSideAndVerticalAlignment getBottom(BorderPositionProvider ignored) {
        return Keywords.BorderSideAndVerticalAlignment.BOTTOM;
    }

    @SuppressWarnings("unused")
    public static Keywords.PureHorizontalAlignment getGeneral(CellStyleDefinition ignored) {
        return Keywords.PureHorizontalAlignment.GENERAL;
    }

    @SuppressWarnings("unused")
    public static Keywords.PureHorizontalAlignment getFill(CellStyleDefinition ignored) {
        return Keywords.PureHorizontalAlignment.FILL;
    }

    @SuppressWarnings("unused")
    public static Keywords.PureHorizontalAlignment getCenterSelection(CellStyleDefinition ignored) {
        return Keywords.PureHorizontalAlignment.CENTER_SELECTION;
    }

    @SuppressWarnings("unused")
    public static Keywords.Auto getAuto(SheetDefinition ignored) {
        return Keywords.Auto.AUTO;
    }

    @SuppressWarnings("unused")
    public static Color getAliceBlue(ColorProvider ignored) {
        return Color.aliceBlue;
    }

    @SuppressWarnings("unused")
    public static Color getAntiqueWhite(ColorProvider ignored) {
        return Color.antiqueWhite;
    }

    @SuppressWarnings("unused")
    public static Color getAqua(ColorProvider ignored) {
        return Color.aqua;
    }

    @SuppressWarnings("unused")
    public static Color getAquamarine(ColorProvider ignored) {
        return Color.aquamarine;
    }

    @SuppressWarnings("unused")
    public static Color getAzure(ColorProvider ignored) {
        return Color.azure;
    }

    @SuppressWarnings("unused")
    public static Color getBeige(ColorProvider ignored) {
        return Color.beige;
    }

    @SuppressWarnings("unused")
    public static Color getBisque(ColorProvider ignored) {
        return Color.bisque;
    }

    @SuppressWarnings("unused")
    public static Color getBlack(ColorProvider ignored) {
        return Color.black;
    }

    @SuppressWarnings("unused")
    public static Color getBlanchedAlmond(ColorProvider ignored) {
        return Color.blanchedAlmond;
    }

    @SuppressWarnings("unused")
    public static Color getBlue(ColorProvider ignored) {
        return Color.blue;
    }

    @SuppressWarnings("unused")
    public static Color getBlueViolet(ColorProvider ignored) {
        return Color.blueViolet;
    }

    @SuppressWarnings("unused")
    public static Color getBrown(ColorProvider ignored) {
        return Color.brown;
    }

    @SuppressWarnings("unused")
    public static Color getBurlyWood(ColorProvider ignored) {
        return Color.burlyWood;
    }

    @SuppressWarnings("unused")
    public static Color getCadetBlue(ColorProvider ignored) {
        return Color.cadetBlue;
    }

    @SuppressWarnings("unused")
    public static Color getChartreuse(ColorProvider ignored) {
        return Color.chartreuse;
    }

    @SuppressWarnings("unused")
    public static Color getChocolate(ColorProvider ignored) {
        return Color.chocolate;
    }

    @SuppressWarnings("unused")
    public static Color getCoral(ColorProvider ignored) {
        return Color.coral;
    }

    @SuppressWarnings("unused")
    public static Color getCornflowerBlue(ColorProvider ignored) {
        return Color.cornflowerBlue;
    }

    @SuppressWarnings("unused")
    public static Color getCornsilk(ColorProvider ignored) {
        return Color.cornsilk;
    }

    @SuppressWarnings("unused")
    public static Color getCrimson(ColorProvider ignored) {
        return Color.crimson;
    }

    @SuppressWarnings("unused")
    public static Color getCyan(ColorProvider ignored) {
        return Color.cyan;
    }

    @SuppressWarnings("unused")
    public static Color getDarkBlue(ColorProvider ignored) {
        return Color.darkBlue;
    }

    @SuppressWarnings("unused")
    public static Color getDarkCyan(ColorProvider ignored) {
        return Color.darkCyan;
    }

    @SuppressWarnings("unused")
    public static Color getDarkGoldenRod(ColorProvider ignored) {
        return Color.darkGoldenRod;
    }

    @SuppressWarnings("unused")
    public static Color getDarkGray(ColorProvider ignored) {
        return Color.darkGray;
    }

    @SuppressWarnings("unused")
    public static Color getDarkGreen(ColorProvider ignored) {
        return Color.darkGreen;
    }

    @SuppressWarnings("unused")
    public static Color getDarkKhaki(ColorProvider ignored) {
        return Color.darkKhaki;
    }

    @SuppressWarnings("unused")
    public static Color getDarkMagenta(ColorProvider ignored) {
        return Color.darkMagenta;
    }

    @SuppressWarnings("unused")
    public static Color getDarkOliveGreen(ColorProvider ignored) {
        return Color.darkOliveGreen;
    }

    @SuppressWarnings("unused")
    public static Color getDarkOrange(ColorProvider ignored) {
        return Color.darkOrange;
    }

    @SuppressWarnings("unused")
    public static Color getDarkOrchid(ColorProvider ignored) {
        return Color.darkOrchid;
    }

    @SuppressWarnings("unused")
    public static Color getDarkRed(ColorProvider ignored) {
        return Color.darkRed;
    }

    @SuppressWarnings("unused")
    public static Color getDarkSalmon(ColorProvider ignored) {
        return Color.darkSalmon;
    }

    @SuppressWarnings("unused")
    public static Color getDarkSeaGreen(ColorProvider ignored) {
        return Color.darkSeaGreen;
    }

    @SuppressWarnings("unused")
    public static Color getDarkSlateBlue(ColorProvider ignored) {
        return Color.darkSlateBlue;
    }

    @SuppressWarnings("unused")
    public static Color getDarkSlateGray(ColorProvider ignored) {
        return Color.darkSlateGray;
    }

    @SuppressWarnings("unused")
    public static Color getDarkTurquoise(ColorProvider ignored) {
        return Color.darkTurquoise;
    }

    @SuppressWarnings("unused")
    public static Color getDarkViolet(ColorProvider ignored) {
        return Color.darkViolet;
    }

    @SuppressWarnings("unused")
    public static Color getDeepPink(ColorProvider ignored) {
        return Color.deepPink;
    }

    @SuppressWarnings("unused")
    public static Color getDeepSkyBlue(ColorProvider ignored) {
        return Color.deepSkyBlue;
    }

    @SuppressWarnings("unused")
    public static Color getDimGray(ColorProvider ignored) {
        return Color.dimGray;
    }

    @SuppressWarnings("unused")
    public static Color getDodgerBlue(ColorProvider ignored) {
        return Color.dodgerBlue;
    }

    @SuppressWarnings("unused")
    public static Color getFireBrick(ColorProvider ignored) {
        return Color.fireBrick;
    }

    @SuppressWarnings("unused")
    public static Color getFloralWhite(ColorProvider ignored) {
        return Color.floralWhite;
    }

    @SuppressWarnings("unused")
    public static Color getForestGreen(ColorProvider ignored) {
        return Color.forestGreen;
    }

    @SuppressWarnings("unused")
    public static Color getFuchsia(ColorProvider ignored) {
        return Color.fuchsia;
    }

    @SuppressWarnings("unused")
    public static Color getGainsboro(ColorProvider ignored) {
        return Color.gainsboro;
    }

    @SuppressWarnings("unused")
    public static Color getGhostWhite(ColorProvider ignored) {
        return Color.ghostWhite;
    }

    @SuppressWarnings("unused")
    public static Color getGold(ColorProvider ignored) {
        return Color.gold;
    }

    @SuppressWarnings("unused")
    public static Color getGoldenRod(ColorProvider ignored) {
        return Color.goldenRod;
    }

    @SuppressWarnings("unused")
    public static Color getGray(ColorProvider ignored) {
        return Color.gray;
    }

    @SuppressWarnings("unused")
    public static Color getGreen(ColorProvider ignored) {
        return Color.green;
    }

    @SuppressWarnings("unused")
    public static Color getGreenYellow(ColorProvider ignored) {
        return Color.greenYellow;
    }

    @SuppressWarnings("unused")
    public static Color getHoneyDew(ColorProvider ignored) {
        return Color.honeyDew;
    }

    @SuppressWarnings("unused")
    public static Color getHotPink(ColorProvider ignored) {
        return Color.hotPink;
    }

    @SuppressWarnings("unused")
    public static Color getIndianRed(ColorProvider ignored) {
        return Color.indianRed;
    }

    @SuppressWarnings("unused")
    public static Color getIndigo(ColorProvider ignored) {
        return Color.indigo;
    }

    @SuppressWarnings("unused")
    public static Color getIvory(ColorProvider ignored) {
        return Color.ivory;
    }

    @SuppressWarnings("unused")
    public static Color getKhaki(ColorProvider ignored) {
        return Color.khaki;
    }

    @SuppressWarnings("unused")
    public static Color getLavender(ColorProvider ignored) {
        return Color.lavender;
    }

    @SuppressWarnings("unused")
    public static Color getLavenderBlush(ColorProvider ignored) {
        return Color.lavenderBlush;
    }

    @SuppressWarnings("unused")
    public static Color getLawnGreen(ColorProvider ignored) {
        return Color.lawnGreen;
    }

    @SuppressWarnings("unused")
    public static Color getLemonChiffon(ColorProvider ignored) {
        return Color.lemonChiffon;
    }

    @SuppressWarnings("unused")
    public static Color getLightBlue(ColorProvider ignored) {
        return Color.lightBlue;
    }

    @SuppressWarnings("unused")
    public static Color getLightCoral(ColorProvider ignored) {
        return Color.lightCoral;
    }

    @SuppressWarnings("unused")
    public static Color getLightCyan(ColorProvider ignored) {
        return Color.lightCyan;
    }

    @SuppressWarnings("unused")
    public static Color getLightGoldenRodYellow(ColorProvider ignored) {
        return Color.lightGoldenRodYellow;
    }

    @SuppressWarnings("unused")
    public static Color getLightGray(ColorProvider ignored) {
        return Color.lightGray;
    }

    @SuppressWarnings("unused")
    public static Color getLightGreen(ColorProvider ignored) {
        return Color.lightGreen;
    }

    @SuppressWarnings("unused")
    public static Color getLightPink(ColorProvider ignored) {
        return Color.lightPink;
    }

    @SuppressWarnings("unused")
    public static Color getLightSalmon(ColorProvider ignored) {
        return Color.lightSalmon;
    }

    @SuppressWarnings("unused")
    public static Color getLightSeaGreen(ColorProvider ignored) {
        return Color.lightSeaGreen;
    }

    @SuppressWarnings("unused")
    public static Color getLightSkyBlue(ColorProvider ignored) {
        return Color.lightSkyBlue;
    }

    @SuppressWarnings("unused")
    public static Color getLightSlateGray(ColorProvider ignored) {
        return Color.lightSlateGray;
    }

    @SuppressWarnings("unused")
    public static Color getLightSteelBlue(ColorProvider ignored) {
        return Color.lightSteelBlue;
    }

    @SuppressWarnings("unused")
    public static Color getLightYellow(ColorProvider ignored) {
        return Color.lightYellow;
    }

    @SuppressWarnings("unused")
    public static Color getLime(ColorProvider ignored) {
        return Color.lime;
    }

    @SuppressWarnings("unused")
    public static Color getLimeGreen(ColorProvider ignored) {
        return Color.limeGreen;
    }

    @SuppressWarnings("unused")
    public static Color getLinen(ColorProvider ignored) {
        return Color.linen;
    }

    @SuppressWarnings("unused")
    public static Color getMagenta(ColorProvider ignored) {
        return Color.magenta;
    }

    @SuppressWarnings("unused")
    public static Color getMaroon(ColorProvider ignored) {
        return Color.maroon;
    }

    @SuppressWarnings("unused")
    public static Color getMediumAquaMarine(ColorProvider ignored) {
        return Color.mediumAquaMarine;
    }

    @SuppressWarnings("unused")
    public static Color getMediumBlue(ColorProvider ignored) {
        return Color.mediumBlue;
    }

    @SuppressWarnings("unused")
    public static Color getMediumOrchid(ColorProvider ignored) {
        return Color.mediumOrchid;
    }

    @SuppressWarnings("unused")
    public static Color getMediumPurple(ColorProvider ignored) {
        return Color.mediumPurple;
    }

    @SuppressWarnings("unused")
    public static Color getMediumSeaGreen(ColorProvider ignored) {
        return Color.mediumSeaGreen;
    }

    @SuppressWarnings("unused")
    public static Color getMediumSlateBlue(ColorProvider ignored) {
        return Color.mediumSlateBlue;
    }

    @SuppressWarnings("unused")
    public static Color getMediumSpringGreen(ColorProvider ignored) {
        return Color.mediumSpringGreen;
    }

    @SuppressWarnings("unused")
    public static Color getMediumTurquoise(ColorProvider ignored) {
        return Color.mediumTurquoise;
    }

    @SuppressWarnings("unused")
    public static Color getMediumVioletRed(ColorProvider ignored) {
        return Color.mediumVioletRed;
    }

    @SuppressWarnings("unused")
    public static Color getMidnightBlue(ColorProvider ignored) {
        return Color.midnightBlue;
    }

    @SuppressWarnings("unused")
    public static Color getMintCream(ColorProvider ignored) {
        return Color.mintCream;
    }

    @SuppressWarnings("unused")
    public static Color getMistyRose(ColorProvider ignored) {
        return Color.mistyRose;
    }

    @SuppressWarnings("unused")
    public static Color getMoccasin(ColorProvider ignored) {
        return Color.moccasin;
    }

    @SuppressWarnings("unused")
    public static Color getNavajoWhite(ColorProvider ignored) {
        return Color.navajoWhite;
    }

    @SuppressWarnings("unused")
    public static Color getNavy(ColorProvider ignored) {
        return Color.navy;
    }

    @SuppressWarnings("unused")
    public static Color getOldLace(ColorProvider ignored) {
        return Color.oldLace;
    }

    @SuppressWarnings("unused")
    public static Color getOlive(ColorProvider ignored) {
        return Color.olive;
    }

    @SuppressWarnings("unused")
    public static Color getOliveDrab(ColorProvider ignored) {
        return Color.oliveDrab;
    }

    @SuppressWarnings("unused")
    public static Color getOrange(ColorProvider ignored) {
        return Color.orange;
    }

    @SuppressWarnings("unused")
    public static Color getOrangeRed(ColorProvider ignored) {
        return Color.orangeRed;
    }

    @SuppressWarnings("unused")
    public static Color getOrchid(ColorProvider ignored) {
        return Color.orchid;
    }

    @SuppressWarnings("unused")
    public static Color getPaleGoldenRod(ColorProvider ignored) {
        return Color.paleGoldenRod;
    }

    @SuppressWarnings("unused")
    public static Color getPaleGreen(ColorProvider ignored) {
        return Color.paleGreen;
    }

    @SuppressWarnings("unused")
    public static Color getPaleTurquoise(ColorProvider ignored) {
        return Color.paleTurquoise;
    }

    @SuppressWarnings("unused")
    public static Color getPaleVioletRed(ColorProvider ignored) {
        return Color.paleVioletRed;
    }

    @SuppressWarnings("unused")
    public static Color getPapayaWhip(ColorProvider ignored) {
        return Color.papayaWhip;
    }

    @SuppressWarnings("unused")
    public static Color getPeachPuff(ColorProvider ignored) {
        return Color.peachPuff;
    }

    @SuppressWarnings("unused")
    public static Color getPeru(ColorProvider ignored) {
        return Color.peru;
    }

    @SuppressWarnings("unused")
    public static Color getPink(ColorProvider ignored) {
        return Color.pink;
    }

    @SuppressWarnings("unused")
    public static Color getPlum(ColorProvider ignored) {
        return Color.plum;
    }

    @SuppressWarnings("unused")
    public static Color getPowderBlue(ColorProvider ignored) {
        return Color.powderBlue;
    }

    @SuppressWarnings("unused")
    public static Color getPurple(ColorProvider ignored) {
        return Color.purple;
    }

    @SuppressWarnings("unused")
    public static Color getRebeccaPurple(ColorProvider ignored) {
        return Color.rebeccaPurple;
    }

    @SuppressWarnings("unused")
    public static Color getRed(ColorProvider ignored) {
        return Color.red;
    }

    @SuppressWarnings("unused")
    public static Color getRosyBrown(ColorProvider ignored) {
        return Color.rosyBrown;
    }

    @SuppressWarnings("unused")
    public static Color getRoyalBlue(ColorProvider ignored) {
        return Color.royalBlue;
    }

    @SuppressWarnings("unused")
    public static Color getSaddleBrown(ColorProvider ignored) {
        return Color.saddleBrown;
    }

    @SuppressWarnings("unused")
    public static Color getSalmon(ColorProvider ignored) {
        return Color.salmon;
    }

    @SuppressWarnings("unused")
    public static Color getSandyBrown(ColorProvider ignored) {
        return Color.sandyBrown;
    }

    @SuppressWarnings("unused")
    public static Color getSeaGreen(ColorProvider ignored) {
        return Color.seaGreen;
    }

    @SuppressWarnings("unused")
    public static Color getSeaShell(ColorProvider ignored) {
        return Color.seaShell;
    }

    @SuppressWarnings("unused")
    public static Color getSienna(ColorProvider ignored) {
        return Color.sienna;
    }

    @SuppressWarnings("unused")
    public static Color getSilver(ColorProvider ignored) {
        return Color.silver;
    }

    @SuppressWarnings("unused")
    public static Color getSkyBlue(ColorProvider ignored) {
        return Color.skyBlue;
    }

    @SuppressWarnings("unused")
    public static Color getSlateBlue(ColorProvider ignored) {
        return Color.slateBlue;
    }

    @SuppressWarnings("unused")
    public static Color getSlateGray(ColorProvider ignored) {
        return Color.slateGray;
    }

    @SuppressWarnings("unused")
    public static Color getSnow(ColorProvider ignored) {
        return Color.snow;
    }

    @SuppressWarnings("unused")
    public static Color getSpringGreen(ColorProvider ignored) {
        return Color.springGreen;
    }

    @SuppressWarnings("unused")
    public static Color getSteelBlue(ColorProvider ignored) {
        return Color.steelBlue;
    }

    @SuppressWarnings("unused")
    public static Color getTan(ColorProvider ignored) {
        return Color.tan;
    }

    @SuppressWarnings("unused")
    public static Color getTeal(ColorProvider ignored) {
        return Color.teal;
    }

    @SuppressWarnings("unused")
    public static Color getThistle(ColorProvider ignored) {
        return Color.thistle;
    }

    @SuppressWarnings("unused")
    public static Color getTomato(ColorProvider ignored) {
        return Color.tomato;
    }

    @SuppressWarnings("unused")
    public static Color getTurquoise(ColorProvider ignored) {
        return Color.turquoise;
    }

    @SuppressWarnings("unused")
    public static Color getViolet(ColorProvider ignored) {
        return Color.violet;
    }

    @SuppressWarnings("unused")
    public static Color getWheat(ColorProvider ignored) {
        return Color.wheat;
    }

    @SuppressWarnings("unused")
    public static Color getWhite(ColorProvider ignored) {
        return Color.white;
    }

    @SuppressWarnings("unused")
    public static Color getWhiteSmoke(ColorProvider ignored) {
        return Color.whiteSmoke;
    }

    @SuppressWarnings("unused")
    public static Color getYellow(ColorProvider ignored) {
        return Color.yellow;
    }

    @SuppressWarnings("unused")
    public static Color getYellowGreen(ColorProvider ignored) {
        return Color.yellowGreen;
    }

    @SuppressWarnings("unused")
    public static Keywords.SheetState getLocked(SheetStateProvider ignored) {
        return Keywords.SheetState.LOCKED;
    }

    @SuppressWarnings("unused")
    public static Keywords.SheetState getVisible(SheetStateProvider ignored) {
        return Keywords.SheetState.VISIBLE;
    }

    @SuppressWarnings("unused")
    public static Keywords.SheetState getHidden(SheetStateProvider ignored) {
        return Keywords.SheetState.HIDDEN;
    }

    @SuppressWarnings("unused")
    public static Keywords.SheetState getVeryHidden(SheetStateProvider ignored) {
        return Keywords.SheetState.VERY_HIDDEN;
    }

}
