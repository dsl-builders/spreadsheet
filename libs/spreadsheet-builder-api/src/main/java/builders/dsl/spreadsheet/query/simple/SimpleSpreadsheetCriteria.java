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
package builders.dsl.spreadsheet.query.simple;

import builders.dsl.spreadsheet.api.Cell;
import builders.dsl.spreadsheet.api.Workbook;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteriaResult;
import builders.dsl.spreadsheet.query.api.WorkbookCriterion;

import java.util.Iterator;
import java.util.function.Consumer;

public final class SimpleSpreadsheetCriteria implements SpreadsheetCriteria {

    private final Workbook workbook;

    public static SpreadsheetCriteria forWorkbook(Workbook workbook) {
        return new SimpleSpreadsheetCriteria(workbook);
    }

    private SimpleSpreadsheetCriteria(Workbook workbook) {
        this.workbook = workbook;
    }

    private SpreadsheetCriteriaResult queryInternal(final int max, Consumer<WorkbookCriterion>  workbookCriterion) {
        return new SimpleSpreadsheetCriteriaResult(workbook, workbookCriterion, max);
    }

    @Override
    public SpreadsheetCriteriaResult all() {
        return queryInternal(Integer.MAX_VALUE, w -> {});
    }

    @Override
    public SpreadsheetCriteriaResult query(Consumer<WorkbookCriterion> workbookCriterion) {
        return queryInternal(Integer.MAX_VALUE, workbookCriterion);
    }

    @Override
    public Cell find(Consumer<WorkbookCriterion> workbookCriterion) {
        SpreadsheetCriteriaResult cells = queryInternal(1, workbookCriterion);
        Iterator<Cell> cellIterator = cells.iterator();
        if (cellIterator.hasNext()) {
            return cellIterator.next();
        }
        return null;
    }

    @Override
    public boolean exists(Consumer<WorkbookCriterion> workbookCriterion) {
        return find(workbookCriterion) != null;
    }


}
