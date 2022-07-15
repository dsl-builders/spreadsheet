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

import builders.dsl.spreadsheet.api.Font;
import builders.dsl.spreadsheet.api.FontStyle;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.xssf.usermodel.XSSFFont;
import builders.dsl.spreadsheet.api.Color;

import java.util.EnumSet;

class PoiFont implements Font {

    PoiFont(XSSFFont xssfFont) {
        this.font = xssfFont;
    }

    @Override
    public Color getColor() {
        if (font.getXSSFColor() == null) {
            return null;
        }
        if (font.getXSSFColor().getRGB() == null) {
            return null;
        }
        return new Color(font.getXSSFColor().getRGB());
    }

    @Override
    public int getSize() {
        return font.getFontHeightInPoints();
    }

    @Override
    public String getName() {
        return font.getFontName();
    }

    @Override
    public EnumSet<FontStyle> getStyles() {
        EnumSet<FontStyle> set = EnumSet.noneOf(FontStyle.class);

        if (font.getItalic()) {
            set.add(FontStyle.ITALIC);
        }


        if (font.getBold()) {
            set.add(FontStyle.BOLD);
        }


        if (font.getStrikeout()) {
            set.add(FontStyle.STRIKEOUT);
        }


        if (font.getUnderline() != FontUnderline.NONE.getByteValue()) {
            set.add(FontStyle.UNDERLINE);
        }

        return set;
    }

    XSSFFont getFont() {
        return font;
    }

    void setFont(XSSFFont font) {
        this.font = font;
    }

    private XSSFFont font;
}
