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

import builders.dsl.spreadsheet.api.*;

import java.util.function.Consumer;

public interface CellStyleDefinition extends ForegroundFillProvider, BorderPositionProvider, ColorProvider {

    CellStyleDefinition base(String stylename);

    CellStyleDefinition background(String hexColor);
    CellStyleDefinition background(Color color);

    CellStyleDefinition foreground(String hexColor);
    CellStyleDefinition foreground(Color color);

    CellStyleDefinition fill(ForegroundFill fill);

    CellStyleDefinition font(Consumer<FontDefinition> fontConfiguration);

    /**
     * Sets the indent of the cell in spaces.
     * @param indent the indent of the cell in spaces
     */
    CellStyleDefinition indent(int indent);

    /**
     * Enables word wrapping
     *
     * @param text keyword
     */
    CellStyleDefinition wrap(Keywords.Text text);

    /**
     * Sets the rotation from 0 to 180 (flipped).
     * @param rotation the rotation from 0 to 180 (flipped)
     */
    CellStyleDefinition rotation(int rotation);

    CellStyleDefinition format(String format);

    CellStyleDefinition align(Keywords.VerticalAlignment verticalAlignment, Keywords.HorizontalAlignment horizontalAlignment);

    /**
     * Configures all the borders of the cell.
     * @param borderConfiguration border configuration
     */
    CellStyleDefinition border(Consumer<BorderDefinition> borderConfiguration);

    /**
     * Configures one border of the cell.
     * @param location border to be configured
     * @param borderConfiguration border configuration
     */
    CellStyleDefinition border(Keywords.BorderSide location, Consumer<BorderDefinition> borderConfiguration);

    /**
     * Configures two borders of the cell.
     * @param first first border to be configured
     * @param second second border to be configured
     * @param borderConfiguration border configuration
     */
    CellStyleDefinition border(Keywords.BorderSide first, Keywords.BorderSide second, Consumer<BorderDefinition> borderConfiguration);

    /**
     * Configures three borders of the cell.
     * @param first first border to be configured
     * @param second second border to be configured
     * @param third third border to be configured
     * @param borderConfiguration border configuration
     */
    CellStyleDefinition border(Keywords.BorderSide first, Keywords.BorderSide second, Keywords.BorderSide third, Consumer<BorderDefinition> borderConfiguration);

}
