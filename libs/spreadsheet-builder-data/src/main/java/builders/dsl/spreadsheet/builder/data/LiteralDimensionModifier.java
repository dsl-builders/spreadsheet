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
package builders.dsl.spreadsheet.builder.data;

import builders.dsl.spreadsheet.builder.api.CellDefinition;
import builders.dsl.spreadsheet.builder.api.DimensionModifier;

public class LiteralDimensionModifier implements DimensionModifier {

    private final CellNode cell;
    private final double dimension;
    private final String widthOrHeight;

    public LiteralDimensionModifier(CellNode cell, double dimension, String widthOrHeight) {
        this.cell = cell;
        this.dimension = dimension;
        this.widthOrHeight = widthOrHeight;
    }

    @Override
    public CellDefinition cm() {
        cell.node.set(widthOrHeight, Math.round(dimension) + " cm");
        return cell;
    }

    @Override
    public CellDefinition inch() {
        cell.node.set(widthOrHeight, Math.round(dimension) + " inch");
        return cell;
    }

    @Override
    public CellDefinition inches() {
        return inch();
    }

    @Override
    public CellDefinition points() {
        cell.node.set(widthOrHeight, dimension);
        return cell;
    }

}
