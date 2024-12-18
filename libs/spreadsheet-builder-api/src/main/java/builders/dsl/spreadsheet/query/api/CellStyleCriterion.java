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
package builders.dsl.spreadsheet.query.api;

import builders.dsl.spreadsheet.api.*;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CellStyleCriterion extends ForegroundFillProvider, BorderPositionProvider, ColorProvider {

    CellStyleCriterion background(String hexColor);
    CellStyleCriterion background(Color color);
    CellStyleCriterion background(Predicate<Color> predicate);

    CellStyleCriterion foreground(String hexColor);
    CellStyleCriterion foreground(Color color);
    CellStyleCriterion foreground(Predicate<Color> predicate);

    CellStyleCriterion fill(ForegroundFill fill);
    CellStyleCriterion fill(Predicate<ForegroundFill> predicate);

    CellStyleCriterion indent(int indent);
    CellStyleCriterion indent(Predicate<Integer> predicate);

    CellStyleCriterion rotation(int rotation);
    CellStyleCriterion rotation(Predicate<Integer> predicate);

    CellStyleCriterion format(String format);
    CellStyleCriterion format(Predicate<String> format);

    CellStyleCriterion font(Consumer<FontCriterion> fontCriterion);

    /**
     * Configures all the borders of the cell.
     * @param borderConfiguration border configuration
     */
    CellStyleCriterion border(Consumer<BorderCriterion> borderConfiguration);

    /**
     * Configures one border of the cell.
     * @param location border to be configured
     * @param borderConfiguration border configuration
     */
    CellStyleCriterion border(Keywords.BorderSide location, Consumer<BorderCriterion> borderConfiguration);

    /**
     * Configures two borders of the cell.
     * @param first first border to be configured
     * @param second second border to be configured
     * @param borderConfiguration border configuration
     */
    CellStyleCriterion border(Keywords.BorderSide first, Keywords.BorderSide second, Consumer<BorderCriterion> borderConfiguration);

    /**
     * Configures three borders of the cell.
     * @param first first border to be configured
     * @param second second border to be configured
     * @param third third border to be configured
     * @param borderConfiguration border configuration
     */
    CellStyleCriterion border(Keywords.BorderSide first, Keywords.BorderSide second, Keywords.BorderSide third, Consumer<BorderCriterion> borderConfiguration);

    CellStyleCriterion having(Predicate<CellStyle> cellStylePredicate);

}
