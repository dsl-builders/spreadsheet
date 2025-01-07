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
package builders.dsl.spreadsheet.builder.api;


import java.util.function.Consumer;

public interface HasStyle {

    /**
     * Applies a customized named style to the current element.
     *
     * @param name the name of the style
     * @param styleDefinition the definition of the style customizing the predefined style
     */
    HasStyle style(String name, Consumer<CellStyleDefinition> styleDefinition);

    /**
     * Applies a customized named style to the current element.
     *
     * @param names the names of the styles
     * @param styleDefinition the definition of the style customizing the predefined style
     */
    HasStyle styles(Iterable<String> names, Consumer<CellStyleDefinition> styleDefinition);

    HasStyle styles(Iterable<String> styles, Iterable<Consumer<CellStyleDefinition>> styleDefinitions);

    /**
     * Applies the style defined by the closure to the current element.
     * @param styleDefinition the definition of the style
     */
    HasStyle style(Consumer<CellStyleDefinition> styleDefinition);

    /**
     * Applies the named style to the current element.
     *
     * The style can be changed no longer.
     *
     * @param name the name of the style
     */
    HasStyle style(String name);

    /**
     * Applies the named style to the current element.
     *
     * The style can be changed no longer.
     *
     * @param names style names to be applied
     */
    HasStyle styles(String... names);
    /**
     * Applies the named style to the current element.
     *
     * The style can be changed no longer.
     *
     * @param names style names to be applied
     */
    HasStyle styles(Iterable<String> names);
}
