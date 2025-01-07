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
package builders.dsl.spreadsheet.query.simple;

import builders.dsl.spreadsheet.api.Cell;
import builders.dsl.spreadsheet.api.Comment;
import builders.dsl.spreadsheet.query.api.CellCriterion;
import builders.dsl.spreadsheet.query.api.CellStyleCriterion;
import java.util.function.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;

final class SimpleCellCriterion extends AbstractCriterion<Cell, CellCriterion> implements CellCriterion {

    SimpleCellCriterion() {}

    private SimpleCellCriterion(boolean disjoint) {
        super(disjoint);
    }

    @Override
    public SimpleCellCriterion date(final Date value) {
        addValueCondition(value, Date.class);
        return this;
    }

    @Override
    public SimpleCellCriterion date(final Predicate<Date> predicate) {
        addValueCondition(predicate, Date.class);
        return this;
    }
    
    @Override
    public CellCriterion localDate(LocalDate value) {
        addValueCondition(value, LocalDate.class);
        return this;
    }

    @Override
    public CellCriterion localDate(Predicate<LocalDate> predicate) {
        addValueCondition(predicate, LocalDate.class);
        return this;
    }

    @Override
    public CellCriterion localDateTime(LocalDateTime value) {
        addValueCondition(value, LocalDateTime.class);
        return this;
    }

    @Override
    public CellCriterion localDateTime(Predicate<LocalDateTime> predicate) {
        addValueCondition(predicate, LocalDateTime.class);
        return this;
    }

    @Override
    public CellCriterion localTime(LocalTime value) {
        addValueCondition(value, LocalTime.class);
        return this;
    }

    @Override
    public CellCriterion localTime(Predicate<LocalTime> predicate) {
        addValueCondition(predicate, LocalTime.class);
        return this;
    }

    @Override
    public SimpleCellCriterion number(Double value) {
        addValueCondition(value, Double.class);
        return this;
    }

    @Override
    public SimpleCellCriterion number(Predicate<Double> predicate) {
        addValueCondition(predicate, Double.class);
        return this;
    }

    @Override
    public SimpleCellCriterion string(String value) {
        addValueCondition(value, String.class);
        return this;
    }

    @Override
    public SimpleCellCriterion string(Predicate<String> predicate) {
        addValueCondition(predicate, String.class);
        return this;
    }

    @Override
    public SimpleCellCriterion value(Object value) {
        if (value == null) {
            string("");
            return this;
        }
        if (value instanceof Date) {
            date((Date) value);
            return this;
        }
        if (value instanceof Calendar) {
            date(((Calendar) value).getTime());
            return this;
        }
        if (value instanceof Number) {
            number(((Number) value).doubleValue());
            return this;
        }
        if (value instanceof Boolean) {
            bool((Boolean) value);
        }
        string(value.toString());
        return this;
    }

    @Override
    public SimpleCellCriterion name(final String name) {
        addCondition(o -> name.equals(o.getName()));
        return this;
    }

    @Override
    public SimpleCellCriterion comment(final String comment) {
        addCondition(o -> comment.equals(o.getComment().getText()));
        return this;
    }

    @Override
    public SimpleCellCriterion bool(Boolean value) {
        addValueCondition(value, Boolean.class);
        return this;
    }


    @Override
    public SimpleCellCriterion style(Consumer<CellStyleCriterion> styleCriterion) {
        SimpleCellStyleCriterion criterion = new SimpleCellStyleCriterion(this);
        styleCriterion.accept(criterion);
        // no need to add criteria, they are added by the style criterion itself
        return this;
    }

    @Override
    public SimpleCellCriterion rowspan(final int span) {
        addCondition(o -> span == o.getRowspan());
        return this;
    }

    @Override
    public SimpleCellCriterion rowspan(final Predicate<Integer> predicate) {
        addCondition(o -> predicate.test(o.getRowspan()));
        return this;
    }

    @Override
    public SimpleCellCriterion colspan(final int span) {
        addCondition(o -> span == o.getColspan());
        return this;
    }

    @Override
    public SimpleCellCriterion colspan(final Predicate<Integer> predicate) {
        addCondition(o -> predicate.test(o.getColspan()));
        return this;
    }

    @Override
    public SimpleCellCriterion name(final Predicate<String> predicate) {
        addCondition(o -> predicate.test(o.getName()));
        return this;
    }

    @Override
    public SimpleCellCriterion comment(final Predicate<Comment> predicate) {
        addCondition(o -> predicate.test(o.getComment()));
        return this;
    }

    private <T> void addValueCondition(final T value, final Class<T> type) {
        addCondition(o -> {
            try {
                return value.equals(o.read(type));
            } catch (Exception e) {
                return false;
            }
        });
    }

    private <T> void addValueCondition(final Predicate<T> predicate, final Class<T> type) {
        addCondition(o -> {
            try {
                return predicate.test(o.read(type));
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Override
    public SimpleCellCriterion or(Consumer<CellCriterion> sheetCriterion) {
        return (SimpleCellCriterion) super.or(sheetCriterion);
    }

    @Override
    public CellCriterion having(Predicate<Cell> cellPredicate) {
        addCondition(cellPredicate);
        return this;
    }

    @Override
    CellCriterion newDisjointCriterionInstance() {
        return new SimpleCellCriterion(true);
    }
}
