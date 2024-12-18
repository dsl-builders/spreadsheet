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
package builders.dsl.spreadsheet.api.groovy

import builders.dsl.spreadsheet.api.BorderPositionProvider
import builders.dsl.spreadsheet.api.BorderStyle
import builders.dsl.spreadsheet.api.BorderStyleProvider
import builders.dsl.spreadsheet.api.Cell
import builders.dsl.spreadsheet.api.Color
import builders.dsl.spreadsheet.api.ColorProvider
import builders.dsl.spreadsheet.api.DataRow
import builders.dsl.spreadsheet.api.FontStyle
import builders.dsl.spreadsheet.api.FontStylesProvider
import builders.dsl.spreadsheet.api.ForegroundFill
import builders.dsl.spreadsheet.api.ForegroundFillProvider
import builders.dsl.spreadsheet.api.Keywords
import builders.dsl.spreadsheet.api.PageSettingsProvider
import builders.dsl.spreadsheet.api.SheetStateProvider
import builders.dsl.spreadsheet.builder.api.*
import builders.dsl.spreadsheet.query.api.*
import builders.dsl.spreadsheet.builder.api.CellDefinition
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import space.jasan.support.groovy.closure.ConsumerWithDelegate

/**
 * Main purpose of this class is to provide additional context for IDEs and static type checking.
 */
@SuppressWarnings([
        'MethodCount',
        'LineLength',
        'BuilderMethodWithSideEffects',
        'ExplicitCallToOrMethod',
        'UnusedMethodParameter',
        'NoWildcardImports',
])
@CompileStatic class SpreadsheetBuilderExtensions {

    static CellDefinition value(CellDefinition self, CharSequence sequence) {
        return self.value(sequence.stripIndent().trim())
    }

    static CanDefineStyle style(CanDefineStyle stylable, String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.CellStyleDefinition') Closure styleDefinition) {
        return stylable.style(name, ConsumerWithDelegate.create(styleDefinition))
    }

    static CellDefinition comment(CellDefinition cellDefinition, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CommentDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.CommentDefinition') Closure commentDefinition) {
        return cellDefinition.comment(ConsumerWithDelegate.create(commentDefinition))
    }

    static CellDefinition text(CellDefinition cellDefinition, String text, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = FontDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.FontDefinition') Closure fontConfiguration) {
        return cellDefinition.text(text, ConsumerWithDelegate.create(fontConfiguration))
    }

    static CellStyleDefinition font(CellStyleDefinition style, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = FontDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.FontDefinition') Closure fontConfiguration) {
        return style.font(ConsumerWithDelegate.create(fontConfiguration))
    }

    /**
     * Configures all the borders of the cell.
     * @param borderConfiguration border configuration closure
     */
    static CellStyleDefinition border(CellStyleDefinition style, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.BorderDefinition') Closure borderConfiguration) {
        return style.border(ConsumerWithDelegate.create(borderConfiguration))
    }

    /**
     * Configures one border of the cell.
     * @param location border to be configured
     * @param borderConfiguration border configuration closure
     */
    static CellStyleDefinition border(CellStyleDefinition style, Keywords.BorderSide location, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.BorderDefinition') Closure borderConfiguration) {
        return style.border(location, ConsumerWithDelegate.create(borderConfiguration))
    }

    /**
     * Configures two borders of the cell.
     * @param first first border to be configured
     * @param second second border to be configured
     * @param borderConfiguration border configuration closure
     */
    static CellStyleDefinition border(CellStyleDefinition style, Keywords.BorderSide first, Keywords.BorderSide second, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.BorderDefinition') Closure borderConfiguration) {
        return style.border(first, second, ConsumerWithDelegate.create(borderConfiguration))
    }

    /**
     * Configures three borders of the cell.
     * @param first first border to be configured
     * @param second second border to be configured
     * @param third third border to be configured
     * @param borderConfiguration border configuration closure
     */
    static CellStyleDefinition border(CellStyleDefinition style, Keywords.BorderSide first, Keywords.BorderSide second, Keywords.BorderSide third, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.BorderDefinition') Closure borderConfiguration) {
        return style.border(first, second, third, ConsumerWithDelegate.create(borderConfiguration))
    }

    /**
     * Applies a customized named style to the current element.
     *
     * @param name the name of the style
     * @param styleDefinition the definition of the style customizing the predefined style
     */
    static HasStyle style(HasStyle stylable, String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.CellStyleDefinition') Closure styleDefinition) {
        return stylable.style(name, ConsumerWithDelegate.create(styleDefinition))
    }

    /**
     * Applies a customized named style to the current element.
     *
     * @param names the names of the styles
     * @param styleDefinition the definition of the style customizing the predefined style
     */
    static HasStyle styles(HasStyle stylable, Iterable<String> names, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.CellStyleDefinition') Closure styleDefinition) {
        return stylable.styles(names, ConsumerWithDelegate.create(styleDefinition))
    }

    /**
     * Applies the style defined by the closure to the current element.
     * @param styleDefinition the definition of the style
     */
    static HasStyle style(HasStyle stylable, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.CellStyleDefinition') Closure styleDefinition) {
        return stylable.style(ConsumerWithDelegate.create(styleDefinition))
    }

    static RowDefinition cell(RowDefinition row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.CellDefinition') Closure cellDefinition) {
        return row.cell(ConsumerWithDelegate.create(cellDefinition))
    }
    static RowDefinition cell(RowDefinition row, int column, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.CellDefinition') Closure cellDefinition) {
        return row.cell(column, ConsumerWithDelegate.create(cellDefinition))
    }
    static RowDefinition cell(RowDefinition row, String column, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.CellDefinition') Closure cellDefinition) {
        return row.cell(column, ConsumerWithDelegate.create(cellDefinition))
    }
    static RowDefinition group(RowDefinition row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.RowDefinition') Closure insideGroupDefinition) {
        return row.group(ConsumerWithDelegate.create(insideGroupDefinition))
    }
    static RowDefinition collapse(RowDefinition row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.RowDefinition') Closure insideGroupDefinition) {
        return row.collapse(ConsumerWithDelegate.create(insideGroupDefinition))
    }

    /**
     * Creates new row in the spreadsheet.
     * @param rowDefinition closure defining the content of the row
     */
    static SheetDefinition row(SheetDefinition sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.RowDefinition') Closure rowDefinition) {
        return sheet.row(ConsumerWithDelegate.create(rowDefinition))
    }

    /**
     * Creates new row in the spreadsheet.
     * @param row row number (1 based - the same as is shown in the file)
     * @param rowDefinition closure defining the content of the row
     */
    static SheetDefinition row(SheetDefinition sheet, int row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.RowDefinition') Closure rowDefinition) {
        return sheet.row(row, ConsumerWithDelegate.create(rowDefinition))
    }

    static SheetDefinition group(SheetDefinition sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.SheetDefinition') Closure insideGroupDefinition) {
        return sheet.group(ConsumerWithDelegate.create(insideGroupDefinition))
    }
    static SheetDefinition collapse(SheetDefinition sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.SheetDefinition') Closure insideGroupDefinition) {
        return sheet.collapse(ConsumerWithDelegate.create(insideGroupDefinition))
    }

    /**
     * Configures the basic page settings.
     * @param pageDefinition closure defining the page settings
     */
    static SheetDefinition page(SheetDefinition sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PageDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.PageDefinition') Closure pageDefinition) {
        return sheet.page(ConsumerWithDelegate.create(pageDefinition))
    }

    static SheetDefinition state(SheetDefinition self, Keywords.SheetState state) {
        return self.state(state)
    }

    static SheetCriterion state(SheetCriterion self, Keywords.SheetState state) {
        return self.state(state)
    }

    static void build(SpreadsheetBuilder builder, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookDefinition) @ClosureParams(value = FromString, options = 'builders.dsl.spreadsheet.builder.api.WorkbookDefinition') Closure workbookDefinition) {
        builder.build(ConsumerWithDelegate.create(workbookDefinition))
    }

    static WorkbookDefinition sheet(WorkbookDefinition workbook, String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetDefinition) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.builder.api.SheetDefinition') Closure sheetDefinition) {
        return workbook.sheet(name, ConsumerWithDelegate.create(sheetDefinition))
    }

    static CellCriterion style(CellCriterion cell, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellStyleCriterion) @ClosureParams(value = FromString, options = 'builders.dsl.spreadsheet.query.api.CellStyleCriterion') Closure styleCriterion) {
        return cell.style(ConsumerWithDelegate.create(styleCriterion))
    }
    static CellCriterion or(CellCriterion cell, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.CellCriterion') Closure sheetCriterion) {
        return cell.or(ConsumerWithDelegate.create(sheetCriterion))
    }

    static CellStyleCriterion font(CellStyleCriterion style, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = FontCriterion) @ClosureParams(value = FromString, options = 'builders.dsl.spreadsheet.query.api.FontCriterion') Closure fontCriterion) {
        return style.font(ConsumerWithDelegate.create(fontCriterion))
    }

    /**
     * Configures all the borders of the cell.
     * @param borderConfiguration border configuration closure
     */
    static CellStyleCriterion border(CellStyleCriterion style, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.BorderCriterion') Closure borderConfiguration) {
        return style.border(ConsumerWithDelegate.create(borderConfiguration))
    }

    /**
     * Configures one border of the cell.
     * @param location border to be configured
     * @param borderConfiguration border configuration closure
     */
    static CellStyleCriterion border(CellStyleCriterion style, Keywords.BorderSide location, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.BorderCriterion') Closure borderConfiguration) {
        return style.border(location, ConsumerWithDelegate.create(borderConfiguration))
    }

    /**
     * Configures two borders of the cell.
     * @param first first border to be configured
     * @param second second border to be configured
     * @param borderConfiguration border configuration closure
     */
    static CellStyleCriterion border(CellStyleCriterion style, Keywords.BorderSide first, Keywords.BorderSide second, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.BorderCriterion') Closure borderConfiguration) {
        return style.border(first, second, ConsumerWithDelegate.create(borderConfiguration))
    }

    /**
     * Configures three borders of the cell.
     * @param first first border to be configured
     * @param second second border to be configured
     * @param third third border to be configured
     * @param borderConfiguration border configuration closure
     */
    static CellStyleCriterion border(CellStyleCriterion style, Keywords.BorderSide first, Keywords.BorderSide second, Keywords.BorderSide third, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = BorderCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.BorderCriterion') Closure borderConfiguration) {
        return style.border(first, second, third, ConsumerWithDelegate.create(borderConfiguration))
    }

    static RowCriterion cell(RowCriterion row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.CellCriterion') Closure cellCriterion) {
        return row.cell(ConsumerWithDelegate.create(cellCriterion))
    }
    static RowCriterion cell(RowCriterion row, int column, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.CellCriterion') Closure cellCriterion) {
        return row.cell(column, ConsumerWithDelegate.create(cellCriterion))
    }
    static RowCriterion cell(RowCriterion row, String column, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.CellCriterion') Closure cellCriterion) {
        return row.cell(column, ConsumerWithDelegate.create(cellCriterion))
    }
    static RowCriterion cell(RowCriterion row, int from, int to, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.CellCriterion') Closure cellCriterion) {
        return row.cell(from, to, ConsumerWithDelegate.create(cellCriterion))
    }
    static RowCriterion cell(RowCriterion row, String from, String to, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CellCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.CellCriterion') Closure cellCriterion) {
        return row.cell(from, to, ConsumerWithDelegate.create(cellCriterion))
    }

    static RowCriterion or(RowCriterion row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.RowCriterion') Closure rowCriterion) {
        return row.or(ConsumerWithDelegate.create(rowCriterion))
    }

    static SheetCriterion row(SheetCriterion sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.RowCriterion') Closure rowCriterion) {
        return sheet.row(ConsumerWithDelegate.create(rowCriterion))
    }
    static SheetCriterion row(SheetCriterion sheet, int row, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RowCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.RowCriterion') Closure rowCriterion) {
        return sheet.row(row, ConsumerWithDelegate.create(rowCriterion))
    }
    static SheetCriterion page(SheetCriterion sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PageCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.PageCriterion') Closure pageCriterion) {
        return sheet.page(ConsumerWithDelegate.create(pageCriterion))
    }
    static SheetCriterion or(SheetCriterion sheet, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.SheetCriterion') Closure sheetCriterion) {
        return sheet.or(ConsumerWithDelegate.create(sheetCriterion))
    }

    static boolean asBoolean(SpreadsheetCriteriaResult result) {
        return result.sheets || result.rows || result.cells
    }

    static SpreadsheetCriteriaResult query(SpreadsheetCriteria criteria, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookCriterion) @ClosureParams(value = FromString, options = 'builders.dsl.spreadsheet.query.api.WorkbookCriterion') Closure workbookCriterion) throws FileNotFoundException {
        return criteria.query(ConsumerWithDelegate.create(workbookCriterion))
    }
    static Cell find(SpreadsheetCriteria criteria, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookCriterion) @ClosureParams(value = FromString, options = 'builders.dsl.spreadsheet.query.api.WorkbookCriterion') Closure workbookCriterion) throws FileNotFoundException {
        return criteria.find(ConsumerWithDelegate.create(workbookCriterion))
    }
    static boolean exists(SpreadsheetCriteria criteria, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookCriterion) @ClosureParams(value = FromString, options = 'builders.dsl.spreadsheet.query.api.WorkbookCriterion') Closure workbookCriterion) throws FileNotFoundException {
        return criteria.exists(ConsumerWithDelegate.create(workbookCriterion))
    }

    static WorkbookCriterion sheet(WorkbookCriterion workbook, String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.SheetCriterion') Closure sheetCriterion) {
        return workbook.sheet(name, ConsumerWithDelegate.create(sheetCriterion))
    }
    static WorkbookCriterion sheet(WorkbookCriterion workbook, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SheetCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.SheetCriterion') Closure sheetCriterion) {
        return workbook.sheet(ConsumerWithDelegate.create(sheetCriterion))
    }
    static WorkbookCriterion or(WorkbookCriterion workbook, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = WorkbookCriterion) @ClosureParams(value=FromString, options = 'builders.dsl.spreadsheet.query.api.WorkbookCriterion') Closure workbookCriterion) {
        return workbook.or(ConsumerWithDelegate.create(workbookCriterion))
    }

    static Cell getAt(DataRow self, String name) {
        return self.get(name)
    }

    /**
     * Converts the dimension to centimeters.
     *
     * This feature is currently experimental.
     */
    static CellDefinition getCm(DimensionModifier self) {
        return self.cm()
    }

    /**
     * Converts the dimension to inches.
     *
     * This feature is currently experimental.
     */
    static CellDefinition getInch(DimensionModifier self) {
        return self.inch()
    }

    /**
     * Converts the dimension to inches.
     *
     * This feature is currently experimental.
     */
    static CellDefinition getInches(DimensionModifier self) {
        return self.inches()
    }

    /**
     * Keeps the dimesion in points.
     *
     * This feature is currently experimental.
     */
    static CellDefinition getPoints(DimensionModifier self) {
        return self.points()
    }

    static BorderStyle getNone(BorderStyleProvider self) {
        return BorderStyle.NONE
    }

    static BorderStyle getThin(BorderStyleProvider self) {
        return BorderStyle.THIN
    }

    static BorderStyle getMedium(BorderStyleProvider self) {
        return BorderStyle.MEDIUM
    }

    static BorderStyle getDashed(BorderStyleProvider self) {
        return BorderStyle.DASHED
    }

    static BorderStyle getDotted(BorderStyleProvider self) {
        return BorderStyle.DOTTED
    }

    static BorderStyle getThick(BorderStyleProvider self) {
        return BorderStyle.THICK
    }

    static BorderStyle getDouble(BorderStyleProvider self) {
        return BorderStyle.DOUBLE
    }

    static BorderStyle getHair(BorderStyleProvider self) {
        return BorderStyle.HAIR
    }

    static BorderStyle getMediumDashed(BorderStyleProvider self) {
        return BorderStyle.MEDIUM_DASHED
    }

    static BorderStyle getDashDot(BorderStyleProvider self) {
        return BorderStyle.DASH_DOT
    }

    static BorderStyle getMediumDashDot(BorderStyleProvider self) {
        return BorderStyle.MEDIUM_DASH_DOT
    }

    static BorderStyle getDashDotDot(BorderStyleProvider self) {
        return BorderStyle.DASH_DOT_DOT
    }

    static BorderStyle getMediumDashDotDot(BorderStyleProvider self) {
        return BorderStyle.MEDIUM_DASH_DOT_DOT
    }

    static BorderStyle getSlantedDashDot(BorderStyleProvider self) {
        return BorderStyle.SLANTED_DASH_DOT
    }

    static Keywords.Auto getAuto(CellDefinition self) {
        return Keywords.Auto.AUTO
    }

    static Keywords.To getTo(CellDefinition self) {
        return Keywords.To.TO
    }

    static Keywords.Image getImage(CellDefinition self) {
        return Keywords.Image.IMAGE
    }

    static ForegroundFill getNoFill(ForegroundFillProvider self) {
        return ForegroundFill.NO_FILL
    }

    static ForegroundFill getSolidForeground(ForegroundFillProvider self) {
        return ForegroundFill.SOLID_FOREGROUND
    }

    static ForegroundFill getFineDots(ForegroundFillProvider self) {
        return ForegroundFill.FINE_DOTS
    }

    static ForegroundFill getAltBars(ForegroundFillProvider self) {
        return ForegroundFill.ALT_BARS
    }

    static ForegroundFill getSparseDots(ForegroundFillProvider self) {
        return ForegroundFill.SPARSE_DOTS
    }

    static ForegroundFill getThickHorizontalBands(ForegroundFillProvider self) {
        return ForegroundFill.THICK_HORZ_BANDS
    }

    static ForegroundFill getThickVerticalBands(ForegroundFillProvider self) {
        return ForegroundFill.THICK_VERT_BANDS
    }

    static ForegroundFill getThickBackwardDiagonals(ForegroundFillProvider self) {
        return ForegroundFill.THICK_BACKWARD_DIAG
    }

    static ForegroundFill getThickForwardDiagonals(ForegroundFillProvider self) {
        return ForegroundFill.THICK_FORWARD_DIAG
    }

    static ForegroundFill getBigSpots(ForegroundFillProvider self) {
        return ForegroundFill.BIG_SPOTS
    }

    static ForegroundFill getBricks(ForegroundFillProvider self) {
        return ForegroundFill.BRICKS
    }

    static ForegroundFill getThinHorizontalBands(ForegroundFillProvider self) {
        return ForegroundFill.THIN_HORZ_BANDS
    }

    static ForegroundFill getThinVerticalBands(ForegroundFillProvider self) {
        return ForegroundFill.THIN_VERT_BANDS
    }

    static ForegroundFill getThinBackwardDiagonals(ForegroundFillProvider self) {
        return ForegroundFill.THIN_BACKWARD_DIAG
    }

    static ForegroundFill getThinForwardDiagonals(ForegroundFillProvider self) {
        return ForegroundFill.THICK_FORWARD_DIAG
    }

    static ForegroundFill getSquares(ForegroundFillProvider self) {
        return ForegroundFill.SQUARES
    }

    static ForegroundFill getDiamonds(ForegroundFillProvider self) {
        return ForegroundFill.DIAMONDS
    }

    static Keywords.BorderSideAndHorizontalAlignment getLeft(CellStyleDefinition self) {
        return Keywords.BorderSideAndHorizontalAlignment.LEFT
    }

    static Keywords.BorderSideAndHorizontalAlignment getRight(CellStyleDefinition self) {
        return Keywords.BorderSideAndHorizontalAlignment.RIGHT
    }

    static Keywords.BorderSideAndVerticalAlignment getTop(CellStyleDefinition self) {
        return Keywords.BorderSideAndVerticalAlignment.TOP
    }

    static Keywords.BorderSideAndVerticalAlignment getBottom(CellStyleDefinition self) {
        return Keywords.BorderSideAndVerticalAlignment.BOTTOM
    }

    static Keywords.VerticalAndHorizontalAlignment getCenter(CellStyleDefinition self) {
        return Keywords.VerticalAndHorizontalAlignment.CENTER
    }

    static Keywords.VerticalAndHorizontalAlignment getJustify(CellStyleDefinition self) {
        return Keywords.VerticalAndHorizontalAlignment.JUSTIFY
    }

    static Keywords.PureVerticalAlignment getDistributed(CellStyleDefinition self) {
        return Keywords.PureVerticalAlignment.DISTRIBUTED
    }

    static Keywords.Text getText(CellStyleDefinition self) {
        return Keywords.Text.WRAP
    }

    static Keywords.Orientation getPortrait(PageSettingsProvider self) {
        return Keywords.Orientation.PORTRAIT
    }

    static Keywords.Orientation getLandscape(PageSettingsProvider self) {
        return Keywords.Orientation.LANDSCAPE
    }

    static Keywords.Fit getWidth(PageSettingsProvider self) {
        return Keywords.Fit.WIDTH
    }

    static Keywords.Fit getHeight(PageSettingsProvider self) {
        return Keywords.Fit.HEIGHT
    }

    static Keywords.To getTo(PageSettingsProvider self) {
        return Keywords.To.TO
    }

    static Keywords.Paper getLetter(PageSettingsProvider self) {
        return Keywords.Paper.LETTER
    }

    static Keywords.Paper getLetterSmall(PageSettingsProvider self) {
        return Keywords.Paper.LETTER_SMALL
    }

    static Keywords.Paper getTabloid(PageSettingsProvider self) {
        return Keywords.Paper.TABLOID
    }

    static Keywords.Paper getLedger(PageSettingsProvider self) {
        return Keywords.Paper.LEDGER
    }

    static Keywords.Paper getLegal(PageSettingsProvider self) {
        return Keywords.Paper.LEGAL
    }

    static Keywords.Paper getStatement(PageSettingsProvider self) {
        return Keywords.Paper.STATEMENT
    }

    static Keywords.Paper getExecutive(PageSettingsProvider self) {
        return Keywords.Paper.EXECUTIVE
    }

    static Keywords.Paper getA3(PageSettingsProvider self) {
        return Keywords.Paper.A3
    }

    static Keywords.Paper getA4(PageSettingsProvider self) {
        return Keywords.Paper.A4
    }

    static Keywords.Paper getA4Small(PageSettingsProvider self) {
        return Keywords.Paper.A4_SMALL
    }

    static Keywords.Paper getA5(PageSettingsProvider self) {
        return Keywords.Paper.A5
    }

    static Keywords.Paper getB4(PageSettingsProvider self) {
        return Keywords.Paper.B4
    }

    static Keywords.Paper getB5(PageSettingsProvider self) {
        return Keywords.Paper.B5
    }

    static Keywords.Paper getFolio(PageSettingsProvider self) {
        return Keywords.Paper.FOLIO
    }

    static Keywords.Paper getQuarto(PageSettingsProvider self) {
        return Keywords.Paper.QUARTO
    }

    static Keywords.Paper getStandard10x14(PageSettingsProvider self) {
        return Keywords.Paper.STANDARD_10_14
    }

    static Keywords.Paper getStandard11x17(PageSettingsProvider self) {
        return Keywords.Paper.STANDARD_11_17
    }

    static FontStyle getItalic(FontStylesProvider self) {
        return FontStyle.ITALIC
    }

    static FontStyle getBold(FontStylesProvider self) {
        return FontStyle.BOLD
    }

    static FontStyle getStrikeout(FontStylesProvider self) {
        return FontStyle.STRIKEOUT
    }

    static FontStyle getUnderline(FontStylesProvider self) {
        return FontStyle.UNDERLINE
    }

    static Keywords.BorderSideAndHorizontalAlignment getLeft(BorderPositionProvider self) {
        return Keywords.BorderSideAndHorizontalAlignment.LEFT
    }

    static Keywords.BorderSideAndHorizontalAlignment getRight(BorderPositionProvider self) {
        return Keywords.BorderSideAndHorizontalAlignment.RIGHT
    }

    static Keywords.BorderSideAndVerticalAlignment getTop(BorderPositionProvider self) {
        return Keywords.BorderSideAndVerticalAlignment.TOP
    }

    static Keywords.BorderSideAndVerticalAlignment getBottom(BorderPositionProvider self) {
        return Keywords.BorderSideAndVerticalAlignment.BOTTOM
    }

    static Keywords.PureHorizontalAlignment getGeneral(CellStyleDefinition self) {
        return Keywords.PureHorizontalAlignment.GENERAL
    }
    static Keywords.PureHorizontalAlignment getFill(CellStyleDefinition self) {
        return Keywords.PureHorizontalAlignment.FILL
    }
    static Keywords.PureHorizontalAlignment getCenterSelection(CellStyleDefinition self) {
        return Keywords.PureHorizontalAlignment.CENTER_SELECTION
    }
    static Keywords.Auto getAuto(SheetDefinition self) {
        return Keywords.Auto.AUTO
    }

    static Color getAliceBlue(ColorProvider colorProvider) { return Color.aliceBlue }
    static Color getAntiqueWhite(ColorProvider colorProvider) { return Color.antiqueWhite }
    static Color getAqua(ColorProvider colorProvider) { return Color.aqua }
    static Color getAquamarine(ColorProvider colorProvider) { return Color.aquamarine }
    static Color getAzure(ColorProvider colorProvider) { return Color.azure }
    static Color getBeige(ColorProvider colorProvider) { return Color.beige }
    static Color getBisque(ColorProvider colorProvider) { return Color.bisque }
    static Color getBlack(ColorProvider colorProvider) { return Color.black }
    static Color getBlanchedAlmond(ColorProvider colorProvider) { return Color.blanchedAlmond }
    static Color getBlue(ColorProvider colorProvider) { return Color.blue }
    static Color getBlueViolet(ColorProvider colorProvider) { return Color.blueViolet }
    static Color getBrown(ColorProvider colorProvider) { return Color.brown }
    static Color getBurlyWood(ColorProvider colorProvider) { return Color.burlyWood }
    static Color getCadetBlue(ColorProvider colorProvider) { return Color.cadetBlue }
    static Color getChartreuse(ColorProvider colorProvider) { return Color.chartreuse }
    static Color getChocolate(ColorProvider colorProvider) { return Color.chocolate }
    static Color getCoral(ColorProvider colorProvider) { return Color.coral }
    static Color getCornflowerBlue(ColorProvider colorProvider) { return Color.cornflowerBlue }
    static Color getCornsilk(ColorProvider colorProvider) { return Color.cornsilk }
    static Color getCrimson(ColorProvider colorProvider) { return Color.crimson }
    static Color getCyan(ColorProvider colorProvider) { return Color.cyan }
    static Color getDarkBlue(ColorProvider colorProvider) { return Color.darkBlue }
    static Color getDarkCyan(ColorProvider colorProvider) { return Color.darkCyan }
    static Color getDarkGoldenRod(ColorProvider colorProvider) { return Color.darkGoldenRod }
    static Color getDarkGray(ColorProvider colorProvider) { return Color.darkGray }
    static Color getDarkGreen(ColorProvider colorProvider) { return Color.darkGreen }
    static Color getDarkKhaki(ColorProvider colorProvider) { return Color.darkKhaki }
    static Color getDarkMagenta(ColorProvider colorProvider) { return Color.darkMagenta }
    static Color getDarkOliveGreen(ColorProvider colorProvider) { return Color.darkOliveGreen }
    static Color getDarkOrange(ColorProvider colorProvider) { return Color.darkOrange }
    static Color getDarkOrchid(ColorProvider colorProvider) { return Color.darkOrchid }
    static Color getDarkRed(ColorProvider colorProvider) { return Color.darkRed }
    static Color getDarkSalmon(ColorProvider colorProvider) { return Color.darkSalmon }
    static Color getDarkSeaGreen(ColorProvider colorProvider) { return Color.darkSeaGreen }
    static Color getDarkSlateBlue(ColorProvider colorProvider) { return Color.darkSlateBlue }
    static Color getDarkSlateGray(ColorProvider colorProvider) { return Color.darkSlateGray }
    static Color getDarkTurquoise(ColorProvider colorProvider) { return Color.darkTurquoise }
    static Color getDarkViolet(ColorProvider colorProvider) { return Color.darkViolet }
    static Color getDeepPink(ColorProvider colorProvider) { return Color.deepPink }
    static Color getDeepSkyBlue(ColorProvider colorProvider) { return Color.deepSkyBlue }
    static Color getDimGray(ColorProvider colorProvider) { return Color.dimGray }
    static Color getDodgerBlue(ColorProvider colorProvider) { return Color.dodgerBlue }
    static Color getFireBrick(ColorProvider colorProvider) { return Color.fireBrick }
    static Color getFloralWhite(ColorProvider colorProvider) { return Color.floralWhite }
    static Color getForestGreen(ColorProvider colorProvider) { return Color.forestGreen }
    static Color getFuchsia(ColorProvider colorProvider) { return Color.fuchsia }
    static Color getGainsboro(ColorProvider colorProvider) { return Color.gainsboro }
    static Color getGhostWhite(ColorProvider colorProvider) { return Color.ghostWhite }
    static Color getGold(ColorProvider colorProvider) { return Color.gold }
    static Color getGoldenRod(ColorProvider colorProvider) { return Color.goldenRod }
    static Color getGray(ColorProvider colorProvider) { return Color.gray }
    static Color getGreen(ColorProvider colorProvider) { return Color.green }
    static Color getGreenYellow(ColorProvider colorProvider) { return Color.greenYellow }
    static Color getHoneyDew(ColorProvider colorProvider) { return Color.honeyDew }
    static Color getHotPink(ColorProvider colorProvider) { return Color.hotPink }
    static Color getIndianRed(ColorProvider colorProvider) { return Color.indianRed  }
    static Color getIndigo(ColorProvider colorProvider) { return Color.indigo  }
    static Color getIvory(ColorProvider colorProvider) { return Color.ivory }
    static Color getKhaki(ColorProvider colorProvider) { return Color.khaki }
    static Color getLavender(ColorProvider colorProvider) { return Color.lavender }
    static Color getLavenderBlush(ColorProvider colorProvider) { return Color.lavenderBlush }
    static Color getLawnGreen(ColorProvider colorProvider) { return Color.lawnGreen }
    static Color getLemonChiffon(ColorProvider colorProvider) { return Color.lemonChiffon }
    static Color getLightBlue(ColorProvider colorProvider) { return Color.lightBlue }
    static Color getLightCoral(ColorProvider colorProvider) { return Color.lightCoral }
    static Color getLightCyan(ColorProvider colorProvider) { return Color.lightCyan }
    static Color getLightGoldenRodYellow(ColorProvider colorProvider) { return Color.lightGoldenRodYellow }
    static Color getLightGray(ColorProvider colorProvider) { return Color.lightGray }
    static Color getLightGreen(ColorProvider colorProvider) { return Color.lightGreen }
    static Color getLightPink(ColorProvider colorProvider) { return Color.lightPink }
    static Color getLightSalmon(ColorProvider colorProvider) { return Color.lightSalmon }
    static Color getLightSeaGreen(ColorProvider colorProvider) { return Color.lightSeaGreen }
    static Color getLightSkyBlue(ColorProvider colorProvider) { return Color.lightSkyBlue }
    static Color getLightSlateGray(ColorProvider colorProvider) { return Color.lightSlateGray }
    static Color getLightSteelBlue(ColorProvider colorProvider) { return Color.lightSteelBlue }
    static Color getLightYellow(ColorProvider colorProvider) { return Color.lightYellow }
    static Color getLime(ColorProvider colorProvider) { return Color.lime }
    static Color getLimeGreen(ColorProvider colorProvider) { return Color.limeGreen }
    static Color getLinen(ColorProvider colorProvider) { return Color.linen }
    static Color getMagenta(ColorProvider colorProvider) { return Color.magenta }
    static Color getMaroon(ColorProvider colorProvider) { return Color.maroon }
    static Color getMediumAquaMarine(ColorProvider colorProvider) { return Color.mediumAquaMarine }
    static Color getMediumBlue(ColorProvider colorProvider) { return Color.mediumBlue }
    static Color getMediumOrchid(ColorProvider colorProvider) { return Color.mediumOrchid }
    static Color getMediumPurple(ColorProvider colorProvider) { return Color.mediumPurple }
    static Color getMediumSeaGreen(ColorProvider colorProvider) { return Color.mediumSeaGreen }
    static Color getMediumSlateBlue(ColorProvider colorProvider) { return Color.mediumSlateBlue }
    static Color getMediumSpringGreen(ColorProvider colorProvider) { return Color.mediumSpringGreen }
    static Color getMediumTurquoise(ColorProvider colorProvider) { return Color.mediumTurquoise }
    static Color getMediumVioletRed(ColorProvider colorProvider) { return Color.mediumVioletRed }
    static Color getMidnightBlue(ColorProvider colorProvider) { return Color.midnightBlue }
    static Color getMintCream(ColorProvider colorProvider) { return Color.mintCream }
    static Color getMistyRose(ColorProvider colorProvider) { return Color.mistyRose }
    static Color getMoccasin(ColorProvider colorProvider) { return Color.moccasin }
    static Color getNavajoWhite(ColorProvider colorProvider) { return Color.navajoWhite }
    static Color getNavy(ColorProvider colorProvider) { return Color.navy }
    static Color getOldLace(ColorProvider colorProvider) { return Color.oldLace }
    static Color getOlive(ColorProvider colorProvider) { return Color.olive }
    static Color getOliveDrab(ColorProvider colorProvider) { return Color.oliveDrab }
    static Color getOrange(ColorProvider colorProvider) { return Color.orange }
    static Color getOrangeRed(ColorProvider colorProvider) { return Color.orangeRed }
    static Color getOrchid(ColorProvider colorProvider) { return Color.orchid }
    static Color getPaleGoldenRod(ColorProvider colorProvider) { return Color.paleGoldenRod }
    static Color getPaleGreen(ColorProvider colorProvider) { return Color.paleGreen }
    static Color getPaleTurquoise(ColorProvider colorProvider) { return Color.paleTurquoise }
    static Color getPaleVioletRed(ColorProvider colorProvider) { return Color.paleVioletRed }
    static Color getPapayaWhip(ColorProvider colorProvider) { return Color.papayaWhip }
    static Color getPeachPuff(ColorProvider colorProvider) { return Color.peachPuff }
    static Color getPeru(ColorProvider colorProvider) { return Color.peru }
    static Color getPink(ColorProvider colorProvider) { return Color.pink }
    static Color getPlum(ColorProvider colorProvider) { return Color.plum }
    static Color getPowderBlue(ColorProvider colorProvider) { return Color.powderBlue }
    static Color getPurple(ColorProvider colorProvider) { return Color.purple }
    static Color getRebeccaPurple(ColorProvider colorProvider) { return Color.rebeccaPurple }
    static Color getRed(ColorProvider colorProvider) { return Color.red }
    static Color getRosyBrown(ColorProvider colorProvider) { return Color.rosyBrown }
    static Color getRoyalBlue(ColorProvider colorProvider) { return Color.royalBlue }
    static Color getSaddleBrown(ColorProvider colorProvider) { return Color.saddleBrown }
    static Color getSalmon(ColorProvider colorProvider) { return Color.salmon }
    static Color getSandyBrown(ColorProvider colorProvider) { return Color.sandyBrown }
    static Color getSeaGreen(ColorProvider colorProvider) { return Color.seaGreen }
    static Color getSeaShell(ColorProvider colorProvider) { return Color.seaShell }
    static Color getSienna(ColorProvider colorProvider) { return Color.sienna }
    static Color getSilver(ColorProvider colorProvider) { return Color.silver }
    static Color getSkyBlue(ColorProvider colorProvider) { return Color.skyBlue }
    static Color getSlateBlue(ColorProvider colorProvider) { return Color.slateBlue }
    static Color getSlateGray(ColorProvider colorProvider) { return Color.slateGray }
    static Color getSnow(ColorProvider colorProvider) { return Color.snow }
    static Color getSpringGreen(ColorProvider colorProvider) { return Color.springGreen }
    static Color getSteelBlue(ColorProvider colorProvider) { return Color.steelBlue }
    static Color getTan(ColorProvider colorProvider) { return Color.tan }
    static Color getTeal(ColorProvider colorProvider) { return Color.teal }
    static Color getThistle(ColorProvider colorProvider) { return Color.thistle }
    static Color getTomato(ColorProvider colorProvider) { return Color.tomato }
    static Color getTurquoise(ColorProvider colorProvider) { return Color.turquoise }
    static Color getViolet(ColorProvider colorProvider) { return Color.violet }
    static Color getWheat(ColorProvider colorProvider) { return Color.wheat }
    static Color getWhite(ColorProvider colorProvider) { return Color.white }
    static Color getWhiteSmoke(ColorProvider colorProvider) { return Color.whiteSmoke }
    static Color getYellow(ColorProvider colorProvider) { return Color.yellow }
    static Color getYellowGreen(ColorProvider colorProvider) { return Color.yellowGreen }
    static Keywords.SheetState getLocked(SheetStateProvider self) { return Keywords.SheetState.LOCKED }
    static Keywords.SheetState getVisible(SheetStateProvider self) { return Keywords.SheetState.VISIBLE }
    static Keywords.SheetState getHidden(SheetStateProvider self) { return Keywords.SheetState.HIDDEN }
    static Keywords.SheetState getVeryHidden(SheetStateProvider self) { return Keywords.SheetState.VERY_HIDDEN }

}
