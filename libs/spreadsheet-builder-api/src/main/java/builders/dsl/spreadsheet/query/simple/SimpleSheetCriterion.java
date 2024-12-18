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

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.api.Row;
import builders.dsl.spreadsheet.api.Sheet;
import builders.dsl.spreadsheet.query.api.PageCriterion;
import java.util.function.Predicate;
import builders.dsl.spreadsheet.query.api.RowCriterion;
import builders.dsl.spreadsheet.query.api.SheetCriterion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

final class SimpleSheetCriterion extends AbstractCriterion<Row, SheetCriterion> implements SheetCriterion {

    private final Collection<SimpleRowCriterion> criteria = new ArrayList<>();
    private final SimpleWorkbookCriterion parent;

    SimpleSheetCriterion(SimpleWorkbookCriterion parent) {
        this.parent = parent;
    }

    private SimpleSheetCriterion(boolean disjoint, SimpleWorkbookCriterion parent) {
        super(disjoint);
        this.parent = parent;
    }

    @Override
    public SimpleSheetCriterion row(Consumer<RowCriterion> rowCriterion) {
        SimpleRowCriterion criterion = new SimpleRowCriterion(this);
        rowCriterion.accept(criterion);
        criteria.add(criterion);
        return this;
    }

    @Override
    public SimpleSheetCriterion row(int row, Consumer<RowCriterion> rowCriterion) {
        row(row);
        row(rowCriterion);
        return this;
    }

    @Override
    public SimpleSheetCriterion page(Consumer<PageCriterion> pageCriterion) {
        SimplePageCriterion criterion = new SimplePageCriterion(parent);
        pageCriterion.accept(criterion);
        return this;
    }

    @Override
    public SimpleSheetCriterion row(final int row) {
        addCondition(o -> o.getNumber() == row);
        return this;
    }

    @Override
    public SimpleSheetCriterion or(Consumer<SheetCriterion> sheetCriterion) {
        return (SimpleSheetCriterion) super.or(sheetCriterion);
    }

    @Override
    public SheetCriterion having(Predicate<Sheet> sheetPredicate) {
        parent.addCondition(sheetPredicate);
        return this;
    }

    @Override
    public SheetCriterion row(final int from, final int to) {
        addCondition(o -> o.getNumber() >= from && o.getNumber() <= to);
        return this;
    }

    @Override
    public SheetCriterion row(int from, int to, Consumer<RowCriterion> rowCriterion) {
        return null;
    }

    Collection<SimpleRowCriterion> getCriteria() {
        return Collections.unmodifiableCollection(criteria);
    }

    @Override
    SheetCriterion newDisjointCriterionInstance() {
        return new SimpleSheetCriterion(true, parent);
    }

    @Override
    public SheetCriterion state(Keywords.SheetState state) {
        switch (state) {
            case LOCKED:
                parent.addCondition(Sheet::isLocked);
                return this;
            case VISIBLE:
                parent.addCondition(Sheet::isVisible);
                return this;
            case HIDDEN:
                parent.addCondition(Sheet::isHidden);
                return this;
            case VERY_HIDDEN:
                parent.addCondition(Sheet::isVeryHidden);
                return this;
        }
        throw new IllegalStateException("Unknown sheet state: " + state);
    }
}
