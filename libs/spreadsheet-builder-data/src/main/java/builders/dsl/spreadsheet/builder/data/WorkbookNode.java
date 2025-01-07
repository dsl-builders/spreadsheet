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
package builders.dsl.spreadsheet.builder.data;

import builders.dsl.spreadsheet.builder.api.CellStyleDefinition;
import builders.dsl.spreadsheet.builder.api.SheetDefinition;
import builders.dsl.spreadsheet.builder.api.WorkbookDefinition;

import java.util.function.Consumer;

class WorkbookNode extends AbstractNode implements WorkbookDefinition {

    @Override
    public WorkbookDefinition sheet(String name, Consumer<SheetDefinition> sheetDefinition) {
        SheetNode sheet = new SheetNode(name);
        sheetDefinition.accept(sheet);
        node.add("sheets", sheet);
        return this;
    }

    @Override
    public WorkbookDefinition style(String name, Consumer<CellStyleDefinition> styleDefinition) {
        CellStyleNode style = new CellStyleNode(name);
        styleDefinition.accept(style);
        node.add("styles", style);
        return this;
    }
}
