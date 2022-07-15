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
package builders.dsl.spreadsheet.query.simple;

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.api.Page;
import builders.dsl.spreadsheet.query.api.PageCriterion;
import java.util.function.Predicate;

public class SimplePageCriterion implements PageCriterion {

    private final SimpleWorkbookCriterion workbookCriterion;

    SimplePageCriterion(SimpleWorkbookCriterion workbookCriterion) {
        this.workbookCriterion = workbookCriterion;
    }

    @Override
    public SimplePageCriterion orientation(final Keywords.Orientation orientation) {
        workbookCriterion.addCondition(o -> orientation.equals(o.getPage().getOrientation()));
        return this;
    }

    @Override
    public SimplePageCriterion paper(final Keywords.Paper paper) {
        workbookCriterion.addCondition(o -> paper.equals(o.getPage().getPaper()));
        return this;
    }

    @Override
    public PageCriterion having(final Predicate<Page> pagePredicate) {
        workbookCriterion.addCondition(o -> pagePredicate.test(o.getPage()));
        return this;
    }
}
