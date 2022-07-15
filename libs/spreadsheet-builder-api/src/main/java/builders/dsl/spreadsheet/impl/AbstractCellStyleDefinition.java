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
package builders.dsl.spreadsheet.impl;

import builders.dsl.spreadsheet.api.Color;
import builders.dsl.spreadsheet.api.ForegroundFill;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.BorderDefinition;
import builders.dsl.spreadsheet.builder.api.CellStyleDefinition;
import builders.dsl.spreadsheet.builder.api.FontDefinition;
import builders.dsl.spreadsheet.builder.api.Sealable;

import java.util.function.Consumer;

public abstract class AbstractCellStyleDefinition implements CellStyleDefinition, Sealable {

    protected AbstractCellStyleDefinition(AbstractCellDefinition cell) {
        this.workbook = cell.getRow().getSheet().getWorkbook();
    }

    protected AbstractCellStyleDefinition(AbstractWorkbookDefinition workbook) {
        this.workbook = workbook;
    }

    @Override
    public final CellStyleDefinition base(String stylename) {
        checkSealed();
        workbook.getStyleDefinition(stylename).accept(this);
        return this;
    }

    public final void checkSealed() {
        if (sealed) {
            throw new IllegalStateException("The cell style is already sealed! You need to create new style. Use 'styles' method to combine multiple named styles! Create new named style if you're trying to update existing style with closure definition.");
        }

    }

    public final boolean isSealed() {
        return sealed;
    }

    @Override
    public final CellStyleDefinition background(String hexColor) {
        checkSealed();
        doBackground(hexColor);
        return this;
    }

    protected abstract void doBackground(String hexColor);

    @Override
    public final CellStyleDefinition background(Color colorPreset) {
        checkSealed();
        background(colorPreset.getHex());
        return this;
    }

    @Override
    public final CellStyleDefinition foreground(String hexColor) {
        checkSealed();
        doForeground(hexColor);
        return this;
    }

    protected abstract void doForeground(String hexColor);

    @Override
    public final CellStyleDefinition foreground(Color colorPreset) {
        checkSealed();
        foreground(colorPreset.getHex());
        return this;
    }

    @Override
    public final CellStyleDefinition fill(ForegroundFill fill) {
        checkSealed();
        doFill(fill);
        return this;
    }

    protected abstract void doFill(ForegroundFill fill);

    @Override
    public final CellStyleDefinition font(Consumer<FontDefinition> fontConfiguration) {
        checkSealed();
        if (font == null) {
            font = createFont();
        }

        fontConfiguration.accept(font);
        return this;
    }

    protected abstract FontDefinition createFont();

    @Override
    public final CellStyleDefinition indent(int indent) {
        checkSealed();
        doIndent(indent);
        return this;
    }

    protected abstract void doIndent(int indent);

    @Override
    public final CellStyleDefinition wrap(Keywords.Text text) {
        checkSealed();
        doWrapText();
        return this;
    }

    protected abstract void doWrapText();

    @Override
    public final CellStyleDefinition rotation(int rotation) {
        checkSealed();
        doRotation(rotation);
        return this;
    }

    protected abstract void doRotation(int rotation);

    @Override
    public final CellStyleDefinition format(String format) {
        checkSealed();
        doFormat(format);
        return this;
    }

    protected abstract void doFormat(String format);

    @Override
    public final CellStyleDefinition align(Keywords.VerticalAlignment verticalAlignment, Keywords.HorizontalAlignment horizontalAlignment) {
        checkSealed();
        doAlign(verticalAlignment, horizontalAlignment);
        return this;
    }

    protected abstract void doAlign(Keywords.VerticalAlignment verticalAlignment, Keywords.HorizontalAlignment horizontalAlignment);

    @Override
    public final CellStyleDefinition border(Consumer<BorderDefinition> borderConfiguration) {
        checkSealed();
        AbstractBorderDefinition poiBorder = findOrCreateBorder();
        borderConfiguration.accept(poiBorder);

        for (Keywords.BorderSide side: Keywords.BorderSide.BORDER_SIDES) {
            poiBorder.applyTo(side);
        }
        return this;
    }

    @Override
    public final CellStyleDefinition border(Keywords.BorderSide location, Consumer<BorderDefinition> borderConfiguration) {
        checkSealed();
        AbstractBorderDefinition poiBorder = findOrCreateBorder();
        borderConfiguration.accept(poiBorder);
        poiBorder.applyTo(location);
        return this;
    }

    @Override
    public final CellStyleDefinition border(Keywords.BorderSide first, Keywords.BorderSide second, Consumer<BorderDefinition> borderConfiguration) {
        checkSealed();
        AbstractBorderDefinition poiBorder = findOrCreateBorder();
        borderConfiguration.accept(poiBorder);
        poiBorder.applyTo(first);
        poiBorder.applyTo(second);
        return this;
    }

    @Override
    public final CellStyleDefinition border(Keywords.BorderSide first, Keywords.BorderSide second, Keywords.BorderSide third, Consumer<BorderDefinition> borderConfiguration) {
        checkSealed();
        AbstractBorderDefinition poiBorder = findOrCreateBorder();
        borderConfiguration.accept(poiBorder);
        poiBorder.applyTo(first);
        poiBorder.applyTo(second);
        poiBorder.applyTo(third);
        return this;
    }

    private AbstractBorderDefinition findOrCreateBorder() {
        if (border == null) {
            border = createBorder();
        }

        return border;
    }

    protected abstract AbstractBorderDefinition createBorder();

    public final void seal() {
        this.sealed = true;
    }

    protected abstract void assignTo(AbstractCellDefinition cell);

    protected final AbstractWorkbookDefinition workbook;
    private FontDefinition font;
    private AbstractBorderDefinition border;
    private boolean sealed;
}
