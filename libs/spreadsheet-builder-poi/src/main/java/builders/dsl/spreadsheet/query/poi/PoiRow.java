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
package builders.dsl.spreadsheet.query.poi;
import builders.dsl.spreadsheet.api.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.*;

class PoiRow implements Row {

    private final XSSFRow xssfRow;
    private final PoiSheet sheet;

    private Map<Integer, PoiCell> cells;

    PoiRow(PoiSheet sheet, XSSFRow xssfRow) {
        this.sheet = sheet;
        this.xssfRow = xssfRow;
    }

    public List<builders.dsl.spreadsheet.api.Cell> getCells() {
        if (cells == null) {
             cells = new LinkedHashMap<Integer, PoiCell>();
            for (Cell cell : xssfRow) {
                cells.put(cell.getColumnIndex() + 1, new PoiCell(this, (XSSFCell) cell));
            }
        }

        return Collections.unmodifiableList(new ArrayList<builders.dsl.spreadsheet.api.Cell>(cells.values()));
    }

    @Override
    public int getNumber() {
        return xssfRow.getRowNum() + 1;
    }

    @Override
    public PoiSheet getSheet() {
        return sheet;
    }

    protected XSSFRow getRow() {
        return xssfRow;
    }


    @Override
    public PoiRow getAbove(int howMany) {
        return aboveOrBelow(-howMany);
    }

    @Override
    public PoiRow getAbove() {
        return getAbove(1);
    }

    @Override
    public PoiRow getBelow(int howMany) {
        return aboveOrBelow(howMany);
    }

    @Override
    public PoiRow getBelow() {
        return getBelow(1);
    }

    builders.dsl.spreadsheet.api.Cell getCellByNumber(int oneBasedColumnNumber) {
        if (cells == null) {
            getCells();
        }
        return cells.get(oneBasedColumnNumber);
    }

    private PoiRow aboveOrBelow(int howMany) {
        if (xssfRow.getRowNum() + howMany < 0 || xssfRow.getRowNum() + howMany >  xssfRow.getSheet().getLastRowNum()) {
            return null;
        }
        PoiRow existing = sheet.getRowByNumber(getNumber() + howMany);
        if (existing != null) {
            return existing;
        }
        return null;
    }
}
