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

import builders.dsl.spreadsheet.impl.AbstractPendingLink;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Pending link is a link which needs to be resolved at the end of the build when all the named references are known.
 */
class PoiPendingLink extends AbstractPendingLink {

    PoiPendingLink(PoiCellDefinition cell, final String name) {
        super(cell, name);
    }

    public void resolve() {
        Workbook workbook = getPoiCell().getCell().getRow().getSheet().getWorkbook();
        Name xssfName = workbook.getName(getName());

        if (xssfName == null) {
            throw new IllegalArgumentException("Name " + getName() + " does not exist!");
        }


        Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.DOCUMENT);
        link.setAddress(xssfName.getRefersToFormula());

        getPoiCell().getCell().setHyperlink(link);
    }

    private PoiCellDefinition getPoiCell() {
        return (PoiCellDefinition) getCell();
    }

}
