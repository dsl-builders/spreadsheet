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

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.CellDefinition;
import builders.dsl.spreadsheet.builder.api.CellStyleDefinition;
import builders.dsl.spreadsheet.builder.api.CommentDefinition;
import builders.dsl.spreadsheet.builder.api.DimensionModifier;
import builders.dsl.spreadsheet.builder.api.FontDefinition;
import builders.dsl.spreadsheet.builder.api.ImageCreator;
import builders.dsl.spreadsheet.builder.api.LinkDefinition;

import java.util.Date;
import java.util.function.Consumer;

class CellNode extends AbstractNode implements CellDefinition {

    @Override
    public CellDefinition value(Object value) {
        if (value instanceof Date) {
            node.set("value", ((Date) value).toInstant().toString());
            return this;
        }
        node.set("value", value);
        return this;
    }

    @Override
    public CellDefinition name(String name) {
        node.set("name", name);
        return this;
    }

    @Override
    public CellDefinition formula(String formula) {
        node.set("formula", formula);
        return this;
    }

    @Override
    public CellDefinition comment(Consumer<CommentDefinition> commentDefinition) {
        CommentNode commentNode = new CommentNode();
        commentDefinition.accept(commentNode);
        node.set("comment", commentNode);
        return this;
    }

    @Override
    public LinkDefinition link(Keywords.To to) {
        return new LinkHelper(this);
    }

    @Override
    public CellDefinition colspan(int span) {
        node.set("colspan", span);
        return this;
    }

    @Override
    public CellDefinition rowspan(int span) {
        node.set("rowspan", span);
        return this;
    }

    @Override
    public DimensionModifier width(double width) {
        node.set("width", width);
        return new LiteralDimensionModifier(this, width, "width");
    }

    @Override
    public DimensionModifier height(double height) {
        node.set("height", height);
        return new LiteralDimensionModifier(this, height, "height");
    }

    @Override
    public CellDefinition width(Keywords.Auto auto) {
        node.set("width", "auto");
        return this;
    }

    @Override
    public CellDefinition text(String text) {
        node.add("text", text);
        return this;
    }

    @Override
    public CellDefinition text(String text, Consumer<FontDefinition> fontConfiguration) {
        FontNode fontNode = new FontNode();
        fontConfiguration.accept(fontNode);

        MapNode mapNode = new MapNode();
        mapNode.set("content", text);
        mapNode.set("font", fontNode);

        node.add("text", mapNode);

        return this;
    }

    @Override
    public ImageCreator png(Keywords.Image image) {
        return new ImageHelper(this, "png");
    }

    @Override
    public ImageCreator jpeg(Keywords.Image image) {
        return new ImageHelper(this, "jpeg");
    }

    @Override
    public ImageCreator pict(Keywords.Image image) {
        return new ImageHelper(this, "pict");
    }

    @Override
    public ImageCreator emf(Keywords.Image image) {
        return new ImageHelper(this, "emf");
    }

    @Override
    public ImageCreator wmf(Keywords.Image image) {
        return new ImageHelper(this, "wmf");
    }

    @Override
    public ImageCreator dib(Keywords.Image image) {
        return new ImageHelper(this, "dib");
    }

    @Override
    public CellDefinition styles(Iterable<String> styles, Iterable<Consumer<CellStyleDefinition>> styleDefinitions) {
        for (String style : styles) {
            node.add("styles", style);
        }
        for (Consumer<CellStyleDefinition> styleDefinition : styleDefinitions) {
            CellStyleNode cellStyleNode = new CellStyleNode();
            styleDefinition.accept(cellStyleNode);
            node.add("styles", cellStyleNode);
        }
        return this;
    }

    public void setColumn(int column) {
        node.set("column", column);
    }
}
