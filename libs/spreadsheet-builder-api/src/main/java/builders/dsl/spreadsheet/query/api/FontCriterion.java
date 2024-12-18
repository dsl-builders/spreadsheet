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

import java.util.EnumSet;
import java.util.function.Predicate;

public interface FontCriterion extends FontStylesProvider, ColorProvider {

    FontCriterion color(String hexColor);
    FontCriterion color(Color color);
    FontCriterion color(Predicate<Color> predicate);

    FontCriterion size(int size);
    FontCriterion size(Predicate<Integer> predicate);

    FontCriterion name(String name);
    FontCriterion name(Predicate<String> predicate);

    FontCriterion style(FontStyle first, FontStyle... other);
    FontCriterion style(Predicate<EnumSet<FontStyle>> predicate);

    FontCriterion having(Predicate<Font> fontPredicate);
}
