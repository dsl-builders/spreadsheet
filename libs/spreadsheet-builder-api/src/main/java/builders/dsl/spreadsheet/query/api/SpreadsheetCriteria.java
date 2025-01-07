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

import java.io.FileNotFoundException;
import java.util.function.Consumer;

/**
 * Cell matcher uses the builder like syntax to find cells within the workbook.
 * Not all the constructs are be supported at the moment.
 * Check the documentation for the list of all supported features.
 */
public interface SpreadsheetCriteria {

    SpreadsheetCriteriaResult all();
    SpreadsheetCriteriaResult query(Consumer<WorkbookCriterion> workbookCriterion) throws FileNotFoundException;
    Cell find(Consumer<WorkbookCriterion> workbookCriterion) throws FileNotFoundException;
    boolean exists(Consumer<WorkbookCriterion> workbookCriterion) throws FileNotFoundException;

}
