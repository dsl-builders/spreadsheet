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
package builders.dsl.spreadsheet.builder.data;

import builders.dsl.spreadsheet.api.Color;
import builders.dsl.spreadsheet.api.FontStyle;
import builders.dsl.spreadsheet.builder.api.FontDefinition;

class FontNode extends AbstractNode implements FontDefinition {
    
    @Override
    public FontDefinition color(String hexColor) {
        node.set("color", hexColor);
        return this;
    }

    @Override
    public FontDefinition color(Color color) {
        node.set("color", color.getHex());
        return this;
    }

    @Override
    public FontDefinition size(int size) {
        node.set("size", size);
        return this;
    }

    @Override
    public FontDefinition name(String name) {
        node.set("name", name);
        return this;
    }

    @Override
    public FontDefinition style(FontStyle first, FontStyle... other) {
        node.add("styles", first.name());
        for(FontStyle style : other) {
            node.add("styles", style.name());
        }
        return this;
    }
    
}
