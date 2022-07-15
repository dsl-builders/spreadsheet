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

import builders.dsl.spreadsheet.api.Border;
import builders.dsl.spreadsheet.api.BorderStyle;
import builders.dsl.spreadsheet.api.BorderStyleProvider;
import builders.dsl.spreadsheet.api.Color;

import java.util.function.Predicate;

public interface BorderCriterion extends BorderStyleProvider {

    BorderCriterion style(BorderStyle style);
    BorderCriterion style(Predicate<BorderStyle> predicate);

    BorderCriterion color(String hexColor);
    BorderCriterion color(Color color);
    BorderCriterion color(Predicate<Color> predicate);

    BorderCriterion having(Predicate<Border> borderPredicate);

}
