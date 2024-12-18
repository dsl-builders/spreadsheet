/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2024 Vladimir Orany.
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

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.FitDimension;
import builders.dsl.spreadsheet.builder.api.PageDefinition;
import org.apache.poi.ss.usermodel.PaperSize;
import org.apache.poi.ss.usermodel.PrintSetup;

class PoiPageSettingsProvider implements PageDefinition {

    PoiPageSettingsProvider(PoiSheetDefinition sheet) {
        this.printSetup = sheet.getSheet().getPrintSetup();
    }

    @Override
    public PoiPageSettingsProvider orientation(Keywords.Orientation orientation) {
        switch (orientation) {
            case PORTRAIT:
                printSetup.setLandscape(false);
                break;
            case LANDSCAPE:
                printSetup.setLandscape(true);
                break;
        }
        return this;
    }

    @Override
    public PoiPageSettingsProvider paper(Keywords.Paper paper) {
        switch (paper) {
            case LETTER:
                setPaperSize(printSetup, PaperSize.LETTER_PAPER);
                break;
            case LETTER_SMALL:
                setPaperSize(printSetup, PaperSize.LETTER_SMALL_PAPER);
                break;
            case TABLOID:
                setPaperSize(printSetup, PaperSize.TABLOID_PAPER);
                break;
            case LEDGER:
                setPaperSize(printSetup, PaperSize.LEDGER_PAPER);
                break;
            case LEGAL:
                setPaperSize(printSetup, PaperSize.LEGAL_PAPER);
                break;
            case STATEMENT:
                setPaperSize(printSetup, PaperSize.STATEMENT_PAPER);
                break;
            case EXECUTIVE:
                setPaperSize(printSetup, PaperSize.EXECUTIVE_PAPER);
                break;
            case A3:
                setPaperSize(printSetup, PaperSize.A3_PAPER);
                break;
            case A4:
                setPaperSize(printSetup, PaperSize.A4_PAPER);
                break;
            case A4_SMALL:
                setPaperSize(printSetup, PaperSize.A4_SMALL_PAPER);
                break;
            case A5:
                setPaperSize(printSetup, PaperSize.A5_PAPER);
                break;
            case B4:
                setPaperSize(printSetup, PaperSize.B4_PAPER);
                break;
            case B5:
                setPaperSize(printSetup, PaperSize.B5_PAPER);
                break;
            case FOLIO:
                setPaperSize(printSetup, PaperSize.FOLIO_PAPER);
                break;
            case QUARTO:
                setPaperSize(printSetup, PaperSize.QUARTO_PAPER);
                break;
            case STANDARD_10_14:
                setPaperSize(printSetup, PaperSize.STANDARD_PAPER_10_14);
                break;
            case STANDARD_11_17:
                setPaperSize(printSetup, PaperSize.STANDARD_PAPER_11_17);
                break;
        }
        return this;
    }

    @Override
    public FitDimension fit(Keywords.Fit widthOrHeight) {
        return new PoiFitDimension(this, widthOrHeight);
    }

    final PrintSetup getPrintSetup() {
        return printSetup;
    }

    private static void setPaperSize(PrintSetup setup, PaperSize size) {
        setup.setPaperSize((short) (size.ordinal() + 1));
    }

    private final PrintSetup printSetup;
}
