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

import builders.dsl.spreadsheet.builder.api.RowDefinition;
import builders.dsl.spreadsheet.impl.AbstractCellDefinition;
import builders.dsl.spreadsheet.impl.AbstractRowDefinition;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

class PoiRowDefinition extends AbstractRowDefinition implements RowDefinition {

    private final Row xssfRow;

    PoiRowDefinition(PoiSheetDefinition sheet, Row xssfRow) {
        super(sheet);
        this.xssfRow = xssfRow;
    }

    @Override
    protected AbstractCellDefinition createCell(int zeroBasedCellNumber) {
        Cell cell = xssfRow.getCell(zeroBasedCellNumber);

        if (cell == null) {
            cell = xssfRow.createCell(zeroBasedCellNumber);
        }

        return new PoiCellDefinition(this, cell);
    }

    @Override
    protected void handleSpans(AbstractCellDefinition cell) {
        if (cell instanceof PoiCellDefinition) {
            PoiCellDefinition poiCell = (PoiCellDefinition) cell;
            if (poiCell.getColspan() > 1 || poiCell.getRowspan() > 1) {
                xssfRow.getSheet().addMergedRegion(poiCell.getCellRangeAddress());
            }
        } else {
            throw new IllegalArgumentException("Unsupported cell: " + cell);
        }
    }

    @Override
    public int getNumber() {
        return xssfRow.getRowNum() + 1;
    }

    public PoiSheetDefinition getSheet() {
        return (PoiSheetDefinition) sheet;
    }

    protected Row getRow() {
        return xssfRow;
    }


    protected void doCreateGroup(int startPosition, int endPosition, boolean collapsed) {
        getSheet().getSheet().groupColumn(startPosition, endPosition);
        if (collapsed) {
            getSheet().getSheet().setColumnGroupCollapsed(endPosition, true);
        }
    }

}
