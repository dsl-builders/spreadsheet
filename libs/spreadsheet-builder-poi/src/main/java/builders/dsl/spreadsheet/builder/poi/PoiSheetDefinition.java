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

import builders.dsl.spreadsheet.builder.api.PageDefinition;
import builders.dsl.spreadsheet.builder.api.SheetDefinition;
import builders.dsl.spreadsheet.impl.AbstractSheetDefinition;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.util.Optional;

class PoiSheetDefinition extends AbstractSheetDefinition implements SheetDefinition {

    private static final int WIDTH_ARROW_BUTTON = 2 * 255;
    public static final int MAX_COLUMN_WIDTH = 255 * 256;

    private final Sheet sheet;

    PoiSheetDefinition(PoiWorkbookDefinition workbook, Sheet sheet) {
        super(workbook);
        this.sheet = sheet;
    }

    @Override protected PoiRowDefinition createRow(int zeroBasedRowNumber) {
        Row row = sheet.getRow(zeroBasedRowNumber);

        if (row == null) {
            row = sheet.createRow(zeroBasedRowNumber);
        }

        return new PoiRowDefinition(this, row);
    }

    @Override
    protected PageDefinition createPageDefinition() {
        return new PoiPageSettingsProvider(this);
    }

    @Override
    protected void applyRowGroup(int startPosition, int endPosition, boolean collapsed) {
        sheet.groupRow(startPosition, endPosition);
        if (collapsed) {
            sheet.setRowGroupCollapsed(endPosition, true);
        }
    }

    @Override
    public String getName() {
        return sheet.getSheetName();
    }

    @Override
    public PoiWorkbookDefinition getWorkbook() {
        return (PoiWorkbookDefinition) super.getWorkbook();
    }

    @Override
    protected void doFreeze(int column, int row) {
        sheet.createFreezePane(column, row);
    }

    @Override
    protected void doLock() {
        getWorkbook().doLock(sheet);
    }

    @Override
    protected void doHide() {
        setVisibility(SheetVisibility.HIDDEN);
    }

    private void setVisibility(SheetVisibility visibility) {
        sheet.getWorkbook().setSheetVisibility(sheet.getWorkbook().getSheetIndex(sheet), visibility);
    }

    @Override
    protected void doHideCompletely() {
        setVisibility(SheetVisibility.VERY_HIDDEN);
    }

    @Override
    protected void doShow() {
        setVisibility(SheetVisibility.VISIBLE);
    }

    @Override
    protected void doPassword(String password) {
        sheet.protectSheet(password);
    }

    protected Sheet getSheet() {
        return sheet;
    }

    @Override
    public void addAutoColumn(int i) {
        if (getSheet() instanceof SXSSFSheet) {
            ((SXSSFSheet) getSheet()).trackColumnForAutoSizing(i);
        } else {
            super.addAutoColumn(i);
        }
    }

    protected void processAutoColumns() {
        if (!(getSheet() instanceof SXSSFSheet)) {
            for (Integer index : autoColumns) {
                sheet.autoSizeColumn(index);
                if (automaticFilter) {
                    sheet.setColumnWidth(index, Math.min(sheet.getColumnWidth(index) + WIDTH_ARROW_BUTTON, MAX_COLUMN_WIDTH));
                }
            }
        }
    }

    protected void processAutomaticFilter() {
        if (automaticFilter && sheet.getLastRowNum() > 0) {
            Row firstOrLastRow = Optional.ofNullable(sheet.getRow(sheet.getFirstRowNum())).orElse(sheet.getRow(sheet.getLastRowNum()));
            sheet.setAutoFilter(new CellRangeAddress(
                    sheet.getFirstRowNum(),
                    sheet.getLastRowNum(),
                    firstOrLastRow.getFirstCellNum(),
                    firstOrLastRow.getLastCellNum() - 1
            ));
        }
    }
}
