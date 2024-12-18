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
package builders.dsl.spreadsheet.builder.api;


import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.api.SheetStateProvider;
import builders.dsl.spreadsheet.impl.Utils;

import java.util.function.Consumer;

public interface SheetDefinition extends SheetStateProvider {

    /**
     * Crates new empty row.
     */
    default SheetDefinition row() {
        return row(r ->{});
    }

    /**
     * Creates new row in the spreadsheet.
     * @param rowDefinition definition of the content of the row
     */
    SheetDefinition row(Consumer<RowDefinition> rowDefinition);

    /**
     * Creates new row in the spreadsheet.
     * @param row row number (1 based - the same as is shown in the file)
     * @param rowDefinition definition of the content of the row
     */
    SheetDefinition row(int row, Consumer<RowDefinition> rowDefinition);

    /**
     * Freeze some column or row or both.
     * @param column last freeze column
     * @param row last freeze row
     */
    SheetDefinition freeze(int column, int row);

    /**
     * Freeze some column or row or both.
     * @param column last freeze column
     * @param row last freeze row
     */
    default SheetDefinition freeze(String column, int row) {
        return freeze(Utils.parseColumn(column), row);
    }

    SheetDefinition group(Consumer<SheetDefinition> insideGroupDefinition);
    SheetDefinition collapse(Consumer<SheetDefinition> insideGroupDefinition);

    SheetDefinition state(Keywords.SheetState state);

    SheetDefinition password(String password);

    SheetDefinition filter(Keywords.Auto auto);

    /**
     * Configures the basic page settings.
     * @param pageDefinition definition of the page settings
     */
    SheetDefinition page(Consumer<PageDefinition> pageDefinition);


}
