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

import builders.dsl.spreadsheet.api.BorderStyle;
import builders.dsl.spreadsheet.api.Color;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.BorderDefinition;

public abstract class AbstractBorderDefinition implements BorderDefinition {

    @Override
    public final BorderDefinition style(BorderStyle style) {
        borderStyle = style;
        return this;
    }

    @Override
    public final BorderDefinition color(Color colorPreset) {
        color(colorPreset.getHex());
        return this;
    }

    protected abstract void applyTo(Keywords.BorderSide location);

    protected BorderStyle borderStyle;
}
