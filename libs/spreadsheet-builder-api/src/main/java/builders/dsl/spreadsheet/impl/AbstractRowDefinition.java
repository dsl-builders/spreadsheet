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

import builders.dsl.spreadsheet.builder.api.CellDefinition;
import builders.dsl.spreadsheet.builder.api.CellStyleDefinition;
import builders.dsl.spreadsheet.builder.api.RowDefinition;

import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractRowDefinition implements RowDefinition {

    protected final AbstractSheetDefinition sheet;

    private List<String> styles = new ArrayList<String>();
    private List<Consumer<CellStyleDefinition>> styleDefinitions = new ArrayList<Consumer<CellStyleDefinition>>();

    private final List<Integer> startPositions = new ArrayList<Integer>();
    private int nextColNumber;
    private final Map<Integer, AbstractCellDefinition> cells = new LinkedHashMap<Integer, AbstractCellDefinition>();

    protected AbstractRowDefinition(AbstractSheetDefinition sheet) {
        this.sheet = sheet;
    }

    private AbstractCellDefinition findOrCreateCell(int zeroBasedCellNumber) {
        AbstractCellDefinition cell = cells.get(zeroBasedCellNumber + 1);

        if (cell != null) {
            return cell;
        }

        cell = createCell(zeroBasedCellNumber);

        cells.put(zeroBasedCellNumber + 1, cell);

        return cell;
    }

    protected abstract AbstractCellDefinition createCell(int zeroBasedCellNumber);

    @Override
    public final RowDefinition cell(Consumer<CellDefinition> cellDefinition) {
        AbstractCellDefinition poiCell = findOrCreateCell(nextColNumber);

        if (!styles.isEmpty() || !styleDefinitions.isEmpty()) {
            poiCell.styles(styles, styleDefinitions);
        }

        cellDefinition.accept(poiCell);

        nextColNumber += poiCell.getColspan();

        handleSpans(poiCell);

        poiCell.resolve();

        return this;
    }

    protected abstract void handleSpans(AbstractCellDefinition poiCell);

    @Override
    public final RowDefinition cell(int column, Consumer<CellDefinition> cellDefinition) {
        AbstractCellDefinition poiCell = findOrCreateCell(column - 1);

        if (!styles.isEmpty() || !styleDefinitions.isEmpty()) {
            poiCell.styles(styles, styleDefinitions);
        }

        cellDefinition.accept(poiCell);

        nextColNumber = column - 1 + poiCell.getColspan();

        handleSpans(poiCell);

        poiCell.resolve();

        return this;
    }

    @Override
    public final RowDefinition styles(Iterable<String> styles, Iterable<Consumer<CellStyleDefinition>> styleDefinitions) {
        for (String name : styles) {
            this.styles.add(name);
        }
        for (Consumer<CellStyleDefinition> style : styleDefinitions) {
            this.styleDefinitions.add(style);
        }
        return this;
    }

    AbstractSheetDefinition getSheet() {
        return sheet;
    }

    @Override
    public final RowDefinition group(Consumer<RowDefinition> insideGroupDefinition) {
        createGroup(false, insideGroupDefinition);
        return this;
    }

    @Override
    public final RowDefinition collapse(Consumer<RowDefinition> insideGroupDefinition) {
        createGroup(true, insideGroupDefinition);
        return this;
    }


    private void createGroup(boolean collapsed, Consumer<RowDefinition> insideGroupDefinition) {
        startPositions.add(nextColNumber);
        insideGroupDefinition.accept(this);

        int startPosition = startPositions.remove(startPositions.size() - 1);

        if (nextColNumber - startPosition > 0) {
            int endPosition = nextColNumber - 1;
            doCreateGroup(startPosition, endPosition, collapsed);
        }
    }

    protected abstract void doCreateGroup(int startPosition, int endPosition, boolean collapsed);

    @Override
    public String toString() {
        return "Row[" + sheet.getName() + "!" + getNumber() + "]";
    }

    protected abstract int getNumber();

    List<String> getStyles() {
        return Collections.unmodifiableList(styles);
    }
}
