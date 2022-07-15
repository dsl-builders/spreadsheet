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
package builders.dsl.spreadsheet.query.poi;

import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.api.Page;

class PoiPage implements Page {

    PoiPage(PoiSheet sheet) {
        this.printSetup = sheet.getSheet().getPrintSetup();
    }

    @Override
    public Keywords.Orientation getOrientation() {
        switch (printSetup.getOrientation()) {
            case DEFAULT:
                return null;
            case PORTRAIT:
                return Keywords.Orientation.PORTRAIT;
            case LANDSCAPE:
                return Keywords.Orientation.LANDSCAPE;
        }
        return null;
    }

    @Override
    public Keywords.Paper getPaper() {
        switch (printSetup.getPaperSizeEnum()) {
            case LETTER_PAPER:
                return Keywords.Paper.LETTER;
            case LETTER_SMALL_PAPER:
                return Keywords.Paper.LETTER_SMALL;
            case TABLOID_PAPER:
                return Keywords.Paper.TABLOID;
            case LEDGER_PAPER:
                return Keywords.Paper.LEDGER;
            case LEGAL_PAPER:
                return Keywords.Paper.LEGAL;
            case STATEMENT_PAPER:
                return Keywords.Paper.STATEMENT;
            case EXECUTIVE_PAPER:
                return Keywords.Paper.EXECUTIVE;
            case A3_PAPER:
                return Keywords.Paper.A3;
            case A4_PAPER:
                return Keywords.Paper.A4;
            case A4_SMALL_PAPER:
                return Keywords.Paper.A4_SMALL;
            case A5_PAPER:
                return Keywords.Paper.A5;
            case B4_PAPER:
                return Keywords.Paper.B4;
            case B5_PAPER:
                return Keywords.Paper.B5;
            case FOLIO_PAPER:
                return Keywords.Paper.FOLIO;
            case QUARTO_PAPER:
                return Keywords.Paper.QUARTO;
            case STANDARD_PAPER_10_14:
                return Keywords.Paper.STANDARD_10_14;
            case STANDARD_PAPER_11_17:
                return Keywords.Paper.STANDARD_11_17;
        }
        return null;
    }

    final XSSFPrintSetup getPrintSetup() {
        return printSetup;
    }

    private final XSSFPrintSetup printSetup;
}
