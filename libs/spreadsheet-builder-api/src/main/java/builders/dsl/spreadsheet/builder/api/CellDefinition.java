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
package builders.dsl.spreadsheet.builder.api;

import builders.dsl.spreadsheet.api.Keywords;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

public interface CellDefinition extends HasStyle {

    /**
     * Sets the value.
     * @param value new value
     */
    CellDefinition value(Object value);
    CellDefinition name(String name);
    CellDefinition formula(String formula);

    default CellDefinition comment(final String commentText) {
        comment(commentDefinition -> commentDefinition.text(commentText));
        return this;
    }

    CellDefinition comment(Consumer<CommentDefinition> commentDefinition);

    LinkDefinition link(Keywords.To to);

    CellDefinition colspan(int span);
    CellDefinition rowspan(int span);

    /**
     * Sets the width as multiplier of standard character width.
     *
     * The width applies on the whole column.
     *
     * @param width the width as multiplier of standard character width
     * @return dimension modifier which allows to recalculate the number set to cm or inches
     */
    DimensionModifier width(double width);

    /**
     * Sets the height of the cell in points (multiples of 20 twips).
     *
     * The height applies on the whole row.
     *
     * @param height the height of the cell in points (multiples of 20 twips)
     * @return dimension modifier which allows to recalculate the number set to cm or inches
     */
    DimensionModifier height(double height);

    /**
     * Sets that the current column should have automatic width.
     * @param auto keyword
     */
    CellDefinition width(Keywords.Auto auto);

    /**
     * Add a new text run to the cell.
     *
     * This method can be called multiple times. The value of the cell will be result of appending all the text
     * values supplied.
     *
     * @param text new text run
     */
    CellDefinition text(String text);

    /**
     * Add a new text run to the cell.
     *
     * This method can be called multiple times. The value of the cell will be result of appending all the text
     * values supplied.
     *
     * @param text new text run
     */
    CellDefinition text(String text, Consumer<FontDefinition> fontConfiguration);

    ImageCreator png(Keywords.Image image);
    ImageCreator jpeg(Keywords.Image image);
    ImageCreator pict(Keywords.Image image);
    ImageCreator emf(Keywords.Image image);
    ImageCreator wmf(Keywords.Image image);
    ImageCreator dib(Keywords.Image image);

    CellDefinition styles(Iterable<String> styles, Iterable<Consumer<CellStyleDefinition>> styleDefinitions);

    default CellDefinition style(String name) {
        return styles(Collections.singleton(name), Collections.<Consumer<CellStyleDefinition>>emptyList());
    }

    default CellDefinition styles(String... names) {
        return styles(Arrays.asList(names), Collections.<Consumer<CellStyleDefinition>>emptyList());
    }

    default CellDefinition style(Consumer<CellStyleDefinition> styleDefinition) {
        return styles(Collections.<String>emptyList(), Collections.singleton(styleDefinition));
    }

    default CellDefinition styles(Iterable<String> names) {
        return styles(names, Collections.<Consumer<CellStyleDefinition>>emptyList());
    }

    default CellDefinition style(String name, Consumer<CellStyleDefinition> styleDefinition) {
        return styles(Collections.singleton(name), Collections.singleton(styleDefinition));
    }

    default CellDefinition styles(Iterable<String> names, Consumer<CellStyleDefinition> styleDefinition) {
        return styles(names, Collections.singleton(styleDefinition));
    }

}
