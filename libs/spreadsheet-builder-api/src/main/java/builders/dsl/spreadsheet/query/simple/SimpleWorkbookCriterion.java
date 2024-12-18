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

import builders.dsl.spreadsheet.api.Sheet;
import builders.dsl.spreadsheet.query.api.SheetCriterion;
import builders.dsl.spreadsheet.query.api.WorkbookCriterion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

final class SimpleWorkbookCriterion extends AbstractCriterion<Sheet, WorkbookCriterion> implements WorkbookCriterion {

    private final Collection<SimpleSheetCriterion> criteria = new ArrayList<>();

    private SimpleWorkbookCriterion(boolean disjoint) {
        super(disjoint);
    }

    SimpleWorkbookCriterion() {}


    @Override
    public SimpleWorkbookCriterion sheet(final String name) {
        addCondition(o -> o.getName().equals(name));
        return this;
    }

    @Override
    public SimpleWorkbookCriterion sheet(final String name, Consumer<SheetCriterion> sheetCriterion) {
        sheet(name);
        sheet(sheetCriterion);
        return this;
    }

    @Override
    public SimpleWorkbookCriterion sheet(Consumer<SheetCriterion> sheetCriterion) {
        SimpleSheetCriterion sheet = new SimpleSheetCriterion(this);
        sheetCriterion.accept(sheet);
        criteria.add(sheet);
        return this;
    }

    @Override
    public SimpleWorkbookCriterion or(Consumer<WorkbookCriterion> sheetCriterion) {
        return (SimpleWorkbookCriterion) super.or(sheetCriterion);
    }

    Collection<SimpleSheetCriterion> getCriteria() {
        return Collections.unmodifiableCollection(criteria);
    }

    @Override
    WorkbookCriterion newDisjointCriterionInstance() {
        return new SimpleWorkbookCriterion(true);
    }
}
