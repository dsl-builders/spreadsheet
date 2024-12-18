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
package builders.dsl.spreadsheet.builder.api;

import builders.dsl.spreadsheet.impl.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

public interface RowDefinition extends HasStyle {

    default RowDefinition cell() {
        return cell((Object) null);
    }

    default RowDefinition cell(Object value) {
        return cell(c -> c.value(value));
    }

    RowDefinition cell(Consumer<CellDefinition> cellDefinition);
    RowDefinition cell(int column, Consumer<CellDefinition> cellDefinition);

    default RowDefinition cell(String column, Consumer<CellDefinition> cellDefinition) {
        return cell(Utils.parseColumn(column), cellDefinition);
    }

    RowDefinition group(Consumer<RowDefinition> insideGroupDefinition);
    RowDefinition collapse(Consumer<RowDefinition> insideGroupDefinition);

    RowDefinition styles(Iterable<String> styles, Iterable<Consumer<CellStyleDefinition>> styleDefinitions);

    default RowDefinition style(Consumer<CellStyleDefinition> styleDefinition) {
        return styles(Collections.emptyList(), Collections.singleton(styleDefinition));
    }

    default RowDefinition style(String name) {
        return styles(Collections.singleton(name), Collections.emptyList());
    }

    default RowDefinition style(String name, Consumer<CellStyleDefinition> styleDefinition) {
        return styles(Collections.singleton(name), styleDefinition);
    }

    default RowDefinition styles(Iterable<String> names, Consumer<CellStyleDefinition> styleDefinition) {
        return styles(names, Collections.singleton(styleDefinition));
    }

    default RowDefinition styles(String... names) {
        return styles(Arrays.asList(names));
    }

    default RowDefinition styles(Iterable<String> names) {
        return styles(names, Collections.emptyList());
    }

}
