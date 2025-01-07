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
package builders.dsl.spreadsheet.query.api;

import builders.dsl.spreadsheet.api.Cell;
import builders.dsl.spreadsheet.api.Row;
import builders.dsl.spreadsheet.api.Sheet;

import java.util.Collection;

public interface SpreadsheetCriteriaResult extends Iterable<Cell> {

    /**
     * Returns all cells matching the criteria.
     * @return all cells matching the criteria
     */
    Collection<Cell> getCells();

    /**
     * Returns all rows matching the criteria. If any cell criteria is present, at least one cell in the row
     * must pass the test.
     * @return all rows matching the criteria
     */
    Collection<Row> getRows();

    /**
     * Returns all sheets matching the criteria. If any row or cell criteria is present at least one row (or cell)
     * must pass the test.
     * @return all the sheets matching the criteria.
     */
    Collection<Sheet> getSheets();

    /**
     * Returns first cell matching the criteria or null.
     * @return first cell matching the criteria or null
     */
    Cell getCell();

    /**
     * Returns first row matching the criteria or null.
     * @return first row matching the criteria or null
     */
    Row getRow();

    /**
     * Returns first sheet matching the criteria or null.
     * @return first sheet matching the criteria or null
     */
    Sheet getSheet();

}
