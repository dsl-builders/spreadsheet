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

import java.util.function.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

abstract class AbstractCriterion<T, C extends Predicate<T>> implements Predicate<T> {

    private final List<Predicate<T>> predicates = new ArrayList<>();
    private final boolean disjoint;

    AbstractCriterion() {
        this(false);
    }

    AbstractCriterion(boolean disjoint) {
        this.disjoint = disjoint;
    }

    @Override
    public boolean test(T o) {
        if (disjoint) {
            return passesAnyCondition(o);
        }
        return passesAllConditions(o);
    }

    abstract C newDisjointCriterionInstance();

    public AbstractCriterion<T, C> or(Consumer<C> sheetCriterion) {
        C criterion = newDisjointCriterionInstance();
        sheetCriterion.accept(criterion);
        addCondition(criterion);
        return this;
    }

    void addCondition(Predicate<T> predicate) {
        predicates.add(predicate);
    }

    private boolean passesAnyCondition(T object) {
        if (predicates.isEmpty()) {
            return true;
        }
        for (Predicate<T> predicate : predicates) {
            if (predicate.test(object)) {
                return true;
            }
        }
        return false;
    }

    private boolean passesAllConditions(T object) {
        if (predicates.isEmpty()) {
            return true;
        }
        for (Predicate<T> predicate : predicates) {
            if (!predicate.test(object)) {
                return false;
            }
        }
        return true;
    }

}
