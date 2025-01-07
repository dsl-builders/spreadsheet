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

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.api.Row;
import builders.dsl.spreadsheet.api.Sheet;
import builders.dsl.spreadsheet.api.SheetStateProvider;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface SheetCriterion extends Predicate<Row>, SheetStateProvider {

    SheetCriterion row(Consumer<RowCriterion> rowCriterion);
    SheetCriterion row(int row);
    SheetCriterion row(int from, int to);
    SheetCriterion row(int row, Consumer<RowCriterion> rowCriterion);
    SheetCriterion row(int from, int to, Consumer<RowCriterion> rowCriterion);
    SheetCriterion page(Consumer<PageCriterion> pageCriterion);
    SheetCriterion or(Consumer<SheetCriterion> sheetCriterion);
    SheetCriterion having(Predicate<Sheet> sheetPredicate);
    SheetCriterion state(Keywords.SheetState state);

}
