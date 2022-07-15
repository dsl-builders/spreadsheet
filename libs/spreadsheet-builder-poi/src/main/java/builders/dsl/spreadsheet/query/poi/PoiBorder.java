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

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import builders.dsl.spreadsheet.api.Border;
import builders.dsl.spreadsheet.api.Color;

/**
 * Represents the current border configuration of the cell style.
 */
class PoiBorder implements Border {
    PoiBorder(XSSFColor xssfColor, BorderStyle borderStyle) {

        this.borderStyle = borderStyle;
        this.xssfColor = xssfColor;
    }

    @Override
    public Color getColor() {
        if (xssfColor == null) {
            return null;
        }

        return new Color(xssfColor.getRGB());
    }

    @Override
    public builders.dsl.spreadsheet.api.BorderStyle getStyle() {
        switch (borderStyle) {
            case NONE:
                return builders.dsl.spreadsheet.api.BorderStyle.NONE;
            case THIN:
                return builders.dsl.spreadsheet.api.BorderStyle.THIN;
            case MEDIUM:
                return builders.dsl.spreadsheet.api.BorderStyle.MEDIUM;
            case DASHED:
                return builders.dsl.spreadsheet.api.BorderStyle.DASHED;
            case DOTTED:
                return builders.dsl.spreadsheet.api.BorderStyle.DOTTED;
            case THICK:
                return builders.dsl.spreadsheet.api.BorderStyle.THICK;
            case DOUBLE:
                return builders.dsl.spreadsheet.api.BorderStyle.DOUBLE;
            case HAIR:
                return builders.dsl.spreadsheet.api.BorderStyle.HAIR;
            case MEDIUM_DASHED:
                return builders.dsl.spreadsheet.api.BorderStyle.MEDIUM_DASHED;
            case DASH_DOT:
                return builders.dsl.spreadsheet.api.BorderStyle.DASH_DOT;
            case MEDIUM_DASH_DOT:
                return builders.dsl.spreadsheet.api.BorderStyle.MEDIUM_DASH_DOT;
            case DASH_DOT_DOT:
                return builders.dsl.spreadsheet.api.BorderStyle.DASH_DOT_DOT;
            case MEDIUM_DASH_DOT_DOT:
                return builders.dsl.spreadsheet.api.BorderStyle.MEDIUM_DASH_DOT_DOT;
            case SLANTED_DASH_DOT:
                return builders.dsl.spreadsheet.api.BorderStyle.SLANTED_DASH_DOT;
        }
        return null;
    }

    private XSSFColor xssfColor;
    private BorderStyle borderStyle;
}
