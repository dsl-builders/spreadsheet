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
package builders.dsl.spreadsheet.query.api;

import builders.dsl.spreadsheet.api.Cell;
import builders.dsl.spreadsheet.api.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CellCriterion extends Predicate<Cell> {

    CellCriterion date(Date value);
    CellCriterion date(Predicate<Date> predicate);

    CellCriterion localDate(LocalDate value);
    CellCriterion localDate(Predicate<LocalDate> predicate);

    CellCriterion localDateTime(LocalDateTime value);
    CellCriterion localDateTime(Predicate<LocalDateTime> predicate);

    CellCriterion localTime(LocalTime value);
    CellCriterion localTime(Predicate<LocalTime> predicate);

    
    CellCriterion number(Double value);
    CellCriterion number(Predicate<Double> predicate);

    CellCriterion string(String value);
    CellCriterion string(Predicate<String> predicate);

    CellCriterion value(Object value);
    CellCriterion bool(Boolean value);

    CellCriterion style(Consumer<CellStyleCriterion> styleCriterion);

    CellCriterion rowspan(int span);
    CellCriterion rowspan(Predicate<Integer> predicate);
    CellCriterion colspan(int span);
    CellCriterion colspan(Predicate<Integer> predicate);


    CellCriterion name(String name);
    CellCriterion name(Predicate<String> predicate);

    CellCriterion comment(String comment);
    CellCriterion comment(Predicate<Comment> predicate);

    CellCriterion or(Consumer<CellCriterion> sheetCriterion);
    CellCriterion having(Predicate<Cell> cellPredicate);

}
