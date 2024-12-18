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
package builders.dsl.spreadsheet.builder.data;

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.PageDefinition;
import builders.dsl.spreadsheet.builder.api.RowDefinition;
import builders.dsl.spreadsheet.builder.api.SheetDefinition;

import java.util.Collections;
import java.util.function.Consumer;

class SheetNode extends AbstractNode implements SheetDefinition {

    public SheetNode(String name) {
        this.node.set("name", name);
    }

    public SheetNode() { }

    @Override
    public SheetDefinition row(Consumer<RowDefinition> rowDefinition) {
        RowNode row = new RowNode();
        rowDefinition.accept(row);
        node.add("rows", row);
        return this;
    }

    @Override
    public SheetDefinition row(int row, Consumer<RowDefinition> rowDefinition) {
        RowNode rowNode = new RowNode();
        rowNode.setNumber(row);
        rowDefinition.accept(rowNode);
        node.add("rows", rowNode);
        return this;
    }

    @Override
    public SheetDefinition freeze(int column, int row) {
        MapNode freeze = new MapNode();
        freeze.set("column", column);
        freeze.set("row", row);
        node.set("freeze", freeze);
        return this;
    }

    @Override
    public SheetDefinition group(Consumer<SheetDefinition> insideGroupDefinition) {
        SheetNode groupNode = new SheetNode();
        insideGroupDefinition.accept(groupNode);
        node.add("rows", Collections.singletonMap("group", groupNode.getContent().get("rows")));
        return this;
    }

    @Override
    public SheetDefinition collapse(Consumer<SheetDefinition> insideGroupDefinition) {
        SheetNode collapse = new SheetNode();
        insideGroupDefinition.accept(collapse);
        node.add("rows", Collections.singletonMap("collapse", collapse.getContent().get("rows")));
        return this;
    }

    @Override
    public SheetDefinition state(Keywords.SheetState state) {
        node.set("state", state);
        return this;
    }

    @Override
    public SheetDefinition password(String password) {
        node.set("password", password);
        return this;
    }

    @Override
    public SheetDefinition filter(Keywords.Auto auto) {
        node.set("filter", true);
        return this;
    }

    @Override
    public SheetDefinition page(Consumer<PageDefinition> pageDefinition) {
        PageNode page = new PageNode();
        pageDefinition.accept(page);
        node.set("page", page);
        return this;
    }

}
