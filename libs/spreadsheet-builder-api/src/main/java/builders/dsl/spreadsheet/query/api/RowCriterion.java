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

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface RowCriterion extends Predicate<Cell> {

    RowCriterion cell(int column);
    RowCriterion cell(int from, int to);
    RowCriterion cell(String column);
    RowCriterion cell(String from, String to);

    RowCriterion cell(Consumer<CellCriterion> cellCriterion);
    RowCriterion cell(int column, Consumer<CellCriterion> cellCriterion);
    RowCriterion cell(int from, int to, Consumer<CellCriterion> cellCriterion);
    RowCriterion cell(String column, Consumer<CellCriterion> cellCriterion);
    RowCriterion cell(String from, String to, Consumer<CellCriterion> cellCriterion);
    RowCriterion or(Consumer<RowCriterion> rowCriterion);
    RowCriterion having(Predicate<Row> rowPredicate);
}
