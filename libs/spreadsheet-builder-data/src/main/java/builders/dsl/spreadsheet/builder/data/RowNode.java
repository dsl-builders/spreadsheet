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
package builders.dsl.spreadsheet.builder.data;

import builders.dsl.spreadsheet.builder.api.CellDefinition;
import builders.dsl.spreadsheet.builder.api.CellStyleDefinition;
import builders.dsl.spreadsheet.builder.api.RowDefinition;

import java.util.Collections;
import java.util.function.Consumer;

class RowNode extends AbstractNode implements RowDefinition {

    @Override
    public RowDefinition cell(Consumer<CellDefinition> cellDefinition) {
        CellNode cellNode =  new CellNode();
        cellDefinition.accept(cellNode);
        node.add("cells", cellNode);
        return this;
    }

    @Override
    public RowDefinition cell(int column, Consumer<CellDefinition> cellDefinition) {
        CellNode cellNode =  new CellNode();
        cellNode.setColumn(column);
        cellDefinition.accept(cellNode);
        node.add("cells", cellNode);
        return this;
    }

    @Override
    public RowDefinition group(Consumer<RowDefinition> insideGroupDefinition) {
        RowNode rowNode = new RowNode();
        insideGroupDefinition.accept(rowNode);
        node.add("cells", Collections.singletonMap("group", rowNode.getContent().get("cells")));
        return this;
    }

    @Override
    public RowDefinition collapse(Consumer<RowDefinition> insideGroupDefinition) {
        RowNode rowNode = new RowNode();
        insideGroupDefinition.accept(rowNode);
        node.add("cells", Collections.singletonMap("collapse", rowNode.getContent().get("cells")));
        return this;
    }

    @Override
    public RowDefinition styles(Iterable<String> styles, Iterable<Consumer<CellStyleDefinition>> styleDefinitions) {
        for (String style : styles) {
            node.add("styles", style);
        }
        for (Consumer<CellStyleDefinition> styleDefinition : styleDefinitions) {
            CellStyleNode cellStyleNode = new CellStyleNode();
            styleDefinition.accept(cellStyleNode);
            node.add("styles", cellStyleNode);
        }
        return this;
    }

    public void setNumber(int number) {
        node.set("number", number);
    }
}
