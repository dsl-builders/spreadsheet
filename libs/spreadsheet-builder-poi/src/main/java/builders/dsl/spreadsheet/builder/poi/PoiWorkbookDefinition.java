/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2022 Vladimir Orany.
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

import builders.dsl.spreadsheet.builder.api.WorkbookDefinition;
import builders.dsl.spreadsheet.impl.AbstractWorkbookDefinition;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class PoiWorkbookDefinition extends AbstractWorkbookDefinition implements WorkbookDefinition {

    private final Workbook workbook;

    PoiWorkbookDefinition(Workbook workbook) {
        if (!(workbook instanceof XSSFWorkbook) && !(workbook instanceof SXSSFWorkbook)) {
            throw new IllegalArgumentException("Only XSSF and SXSSF workbooks are supported");
        }
        this.workbook = workbook;
    }

    @Override
    protected PoiSheetDefinition createSheet(String name) {
        Sheet sheet = workbook.getSheet(WorkbookUtil.createSafeSheetName(name));
        return new PoiSheetDefinition(this, sheet != null ? sheet : workbook.createSheet(WorkbookUtil.createSafeSheetName(name)));
    }

    @Override
    protected PoiCellStyleDefinition createCellStyle() {
        return new PoiCellStyleDefinition(this);
    }

    Workbook getWorkbook() {
        return workbook;
    }

    void addPendingLink(String ref, PoiCellDefinition cell) {
        addPendingLink(new PoiPendingLink(cell, ref));
    }

    short parseColor(String hexColor) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    protected void doLock(Sheet sheet) {
        if (sheet instanceof XSSFSheet) {
            ((XSSFSheet) sheet).enableLocking();
        }
        if (sheet instanceof SXSSFSheet) {
            ((SXSSFSheet) sheet).enableLocking();
        }
    }

    String getReference(Cell cell) {
        if (cell instanceof XSSFCell) {
            XSSFCell xssfCell = (XSSFCell) cell;
            return xssfCell.getReference();
        }
        return cell.getAddress().formatAsString();
    }
}
