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

import builders.dsl.spreadsheet.api.Color;
import builders.dsl.spreadsheet.api.FontStyle;
import builders.dsl.spreadsheet.builder.api.FontDefinition;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.EnumSet;

class PoiFontDefinition implements FontDefinition {

    PoiFontDefinition(Workbook workbook) {
        font = (XSSFFont) workbook.createFont();
    }

    PoiFontDefinition(Workbook workbook, XSSFCellStyle style) {
        font = (XSSFFont) workbook.createFont();
        style.setFont(font);
    }

    @Override
    public PoiFontDefinition color(String hexColor) {
        font.setColor(PoiCellStyleDefinition.parseColor(hexColor));
        return this;
    }

    @Override
    public PoiFontDefinition color(Color colorPreset) {
        color(colorPreset.getHex());
        return this;
    }

    @Override
    public PoiFontDefinition size(int size) {
        font.setFontHeightInPoints((short) size);
        return this;
    }

    @Override
    public PoiFontDefinition name(String name) {
        font.setFontName(name);
        return this;
    }

    @Override
    public PoiFontDefinition style(FontStyle first, FontStyle... other) {
        EnumSet<FontStyle> enumSet = EnumSet.of(first, other);
        if (enumSet.contains(FontStyle.ITALIC)){
            font.setItalic(true);
        }


        if (enumSet.contains(FontStyle.BOLD)){
            font.setBold(true);
        }


        if (enumSet.contains(FontStyle.STRIKEOUT)){
            font.setStrikeout(true);
        }


        if (enumSet.contains(FontStyle.UNDERLINE)){
            font.setUnderline(FontUnderline.SINGLE);
        }

        return this;
    }

    XSSFFont getFont() {
        return font;
    }

    private final XSSFFont font;
}
