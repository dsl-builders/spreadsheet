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
package builders.dsl.spreadsheet.query.simple;

import builders.dsl.spreadsheet.api.Cell;
import builders.dsl.spreadsheet.api.Row;
import builders.dsl.spreadsheet.impl.Utils;
import builders.dsl.spreadsheet.query.api.CellCriterion;
import java.util.function.Predicate;
import builders.dsl.spreadsheet.query.api.RowCriterion;

import java.util.function.Consumer;

final class SimpleRowCriterion extends AbstractCriterion<Cell, RowCriterion> implements RowCriterion {

    private final SimpleSheetCriterion parent;

    SimpleRowCriterion(SimpleSheetCriterion parent) {
        this.parent = parent;
    }

    private SimpleRowCriterion(SimpleSheetCriterion parent, boolean disjoint) {
        super(disjoint);
        this.parent = parent;
    }

    @Override
    public RowCriterion cell(final int from, final int to) {
        addCondition(o -> o.getColumn() >= from && o.getColumn() <= to);
        return this;
    }

    @Override
    public RowCriterion cell(String from, String to) {
        cell(Utils.parseColumn(from), Utils.parseColumn(to));
        return this;
    }

    @Override
    public RowCriterion cell(int from, int to, Consumer<CellCriterion> cellCriterion) {
        cell(from, to);
        cell(cellCriterion);
        return this;
    }

    @Override
    public RowCriterion cell(String from, String to, Consumer<CellCriterion> cellCriterion) {
        cell(from, to);
        cell(cellCriterion);
        return this;
    }

    @Override
    public SimpleRowCriterion cell(final int column) {
        addCondition(o -> o.getColumn() == column);
        return this;
    }

    @Override
    public SimpleRowCriterion cell(final String column) {
        addCondition(o -> o.getColumnAsString().equals(column));
        return this;
    }

    @Override
    public SimpleRowCriterion cell(Consumer<CellCriterion> cellCriterion) {
        SimpleCellCriterion criterion = new SimpleCellCriterion();
        cellCriterion.accept(criterion);
        addCondition(criterion);
        return this;
    }

    @Override
    public SimpleRowCriterion cell(int column, Consumer<CellCriterion> cellCriterion) {
        cell(column);
        cell(cellCriterion);
        return this;
    }

    @Override
    public SimpleRowCriterion cell(String column, Consumer<CellCriterion> cellCriterion) {
        cell(column);
        cell(cellCriterion);
        return this;
    }

    @Override
    public SimpleRowCriterion or(Consumer<RowCriterion> sheetCriterion) {
        return (SimpleRowCriterion) super.or(sheetCriterion);
    }

    @Override
    public RowCriterion having(Predicate<Row> rowPredicate) {
        parent.addCondition(rowPredicate);
        return this;
    }

    @Override
    RowCriterion newDisjointCriterionInstance() {
        return new SimpleRowCriterion(parent, true);
    }
}
