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
package builders.dsl.spreadsheet.builder.poi;

import builders.dsl.spreadsheet.impl.AbstractPendingFormula;
import builders.dsl.spreadsheet.impl.Utils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Name;

/**
 * Pending formula is a formula definition which needs to be resolved at the end of the build where all named references
 * are know.
 */
class PoiPendingFormula extends AbstractPendingFormula {

    PoiPendingFormula(PoiCellDefinition cell, String formula) {
        super(cell, formula);
    }

    protected void doResolve(String expandedFormula) {
        getPoiCell().getCell().setCellFormula(expandedFormula);
        getPoiCell().getCell().setCellType(CellType.FORMULA);
    }


    protected String findRefersToFormula(final String name) {
        if (!name.equals(Utils.fixName(name))) {
            throw new IllegalArgumentException("Name " + name + " is not valid Excel name! Suggestion: " + Utils.fixName(name));
        }

        Name nameFound = getPoiCell().getCell().getSheet().getWorkbook().getName(name);
        if (nameFound == null) {
            throw new IllegalArgumentException("Named cell \'" + name + "\' cannot be found!");
        }

        return nameFound.getRefersToFormula();
    }

    private PoiCellDefinition getPoiCell() {
        return (PoiCellDefinition) getCell();
    }

}
