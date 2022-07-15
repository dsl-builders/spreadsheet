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
import builders.dsl.spreadsheet.api.ForegroundFill;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.BorderDefinition;
import builders.dsl.spreadsheet.builder.api.CellStyleDefinition;
import builders.dsl.spreadsheet.builder.api.FontDefinition;

import java.util.Arrays;
import java.util.function.Consumer;

class CellStyleNode extends AbstractNode implements CellStyleDefinition {

    public CellStyleNode() { }

    public CellStyleNode(String name) {
        node.set("name", name);
    }

    @Override
    public CellStyleDefinition base(String stylename) {
        node.set("base", stylename);
        return this;
    }

    @Override
    public CellStyleDefinition background(String hexColor) {
        node.set("background", hexColor);
        return this;
    }

    @Override
    public CellStyleDefinition background(Color color) {
        node.set("background", color.getHex());
        return this;
    }

    @Override
    public CellStyleDefinition foreground(String hexColor) {
        node.set("foreground", hexColor);
        return this;
    }

    @Override
    public CellStyleDefinition foreground(Color color) {
        node.set("foreground", color.getHex());
        return this;
    }

    @Override
    public CellStyleDefinition fill(ForegroundFill fill) {
        node.set("fill", fill.name());
        return this;
    }

    @Override
    public CellStyleDefinition font(Consumer<FontDefinition> fontConfiguration) {
        FontNode font = new FontNode();
        fontConfiguration.accept(font);
        node.set("font", font);
        return this;
    }

    @Override
    public CellStyleDefinition indent(int indent) {
        node.set("indent", indent);
        return this;
    }

    @Override
    public CellStyleDefinition wrap(Keywords.Text text) {
        node.set("wrap", true);
        return this;
    }

    @Override
    public CellStyleDefinition rotation(int rotation) {
        node.set("rotation", rotation);
        return this;
    }

    @Override
    public CellStyleDefinition format(String format) {
        node.set("format", format);
        return this;
    }

    @Override
    public CellStyleDefinition align(Keywords.VerticalAlignment verticalAlignment, Keywords.HorizontalAlignment horizontalAlignment) {
        MapNode alignment = new MapNode();
        if (verticalAlignment != null) {
            alignment.set("vertical", verticalAlignment);
        }
        if (horizontalAlignment != null) {
            alignment.set("horizontal", horizontalAlignment);
        }
        node.set("align", alignment);
        return this;
    }

    @Override
    public CellStyleDefinition border(Consumer<BorderDefinition> borderConfiguration) {
        return borders(borderConfiguration);
    }

    @Override
    public CellStyleDefinition border(Keywords.BorderSide location, Consumer<BorderDefinition> borderConfiguration) {
        return borders(borderConfiguration, location);
    }

    @Override
    public CellStyleDefinition border(Keywords.BorderSide first, Keywords.BorderSide second, Consumer<BorderDefinition> borderConfiguration) {
        return borders(borderConfiguration, first, second);
    }

    @Override
    public CellStyleDefinition border(Keywords.BorderSide first, Keywords.BorderSide second, Keywords.BorderSide third, Consumer<BorderDefinition> borderConfiguration) {
        return borders(borderConfiguration, first, second, third);
    }

    private CellStyleDefinition borders(Consumer<BorderDefinition> borderDefinition, Keywords.BorderSide... borders) {
        BorderNode borderNode = new BorderNode();
        borderDefinition.accept(borderNode);
        if (borders.length > 0) {
            borderNode.setSide(Arrays.asList(borders));
        }
        node.add("borders", borderNode);
        return this;
    }
}
