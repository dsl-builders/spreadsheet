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
package builders.dsl.spreadsheet.impl;

import builders.dsl.spreadsheet.builder.api.CellDefinition;
import builders.dsl.spreadsheet.builder.api.DimensionModifier;

public class HeightModifier implements DimensionModifier {

    // FIXME: there's a potential risk in this API as calling e.g. .height(12).cm().cm() would power the width twice

    public HeightModifier(CellDefinition cell, double height, double pointsPerCentimeter, double pointsPerInch) {
        this.height = height;
        this.cell = cell;
        this.pointsPerCentimeter = pointsPerCentimeter;
        this.pointsPerInch = pointsPerInch;
    }

    @Override
    public CellDefinition cm() {
        cell.height(pointsPerCentimeter * height);
        return cell;
    }

    @Override
    public CellDefinition inch() {
        cell.height(pointsPerInch * height);
        return cell;
    }

    @Override
    public CellDefinition inches() {
        return inch();
    }

    @Override
    public CellDefinition points() {
        return cell;
    }

    private double pointsPerCentimeter;
    private double pointsPerInch;
    private final CellDefinition cell;
    private final double height;
}
