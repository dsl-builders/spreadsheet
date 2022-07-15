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

import builders.dsl.spreadsheet.api.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;

class PoiCellStyle implements CellStyle {
    PoiCellStyle(XSSFCellStyle style) {
        this.style = style;
    }

    @Override
    public Color getForeground() {
        if (style.getFillForegroundColorColor() == null) {
            return null;
        }
        if (style.getFillForegroundColorColor().getRGB() == null) {
            return null;
        }
        return new Color(style.getFillForegroundColorColor().getRGB());
    }

    @Override
    public Color getBackground() {
        if (style.getFillBackgroundColorColor() == null) {
            return null;
        }
        if (style.getFillBackgroundColorColor().getRGB() == null) {
            return null;
        }
        return new Color(style.getFillBackgroundColorColor().getRGB());
    }

    @Override
    public ForegroundFill getFill() {
        switch (style.getFillPattern()) {
            case NO_FILL:
                return ForegroundFill.NO_FILL;
            case SOLID_FOREGROUND:
                return ForegroundFill.SOLID_FOREGROUND;
            case FINE_DOTS:
                return ForegroundFill.FINE_DOTS;
            case ALT_BARS:
                return ForegroundFill.ALT_BARS;
            case SPARSE_DOTS:
                return ForegroundFill.SPARSE_DOTS;
            case THICK_HORZ_BANDS:
                return ForegroundFill.THICK_HORZ_BANDS;
            case THICK_VERT_BANDS:
                return ForegroundFill.THICK_VERT_BANDS;
            case THICK_BACKWARD_DIAG:
                return ForegroundFill.THICK_BACKWARD_DIAG;
            case THICK_FORWARD_DIAG:
                return ForegroundFill.THICK_FORWARD_DIAG;
            case BIG_SPOTS:
                return ForegroundFill.BIG_SPOTS;
            case BRICKS:
                return ForegroundFill.BRICKS;
            case THIN_HORZ_BANDS:
                return ForegroundFill.THIN_HORZ_BANDS;
            case THIN_VERT_BANDS:
                return ForegroundFill.THIN_VERT_BANDS;
            case THIN_BACKWARD_DIAG:
                return ForegroundFill.THIN_BACKWARD_DIAG;
            case THIN_FORWARD_DIAG:
                return ForegroundFill.THIN_FORWARD_DIAG;
            case SQUARES:
                return ForegroundFill.SQUARES;
            case DIAMONDS:
                return ForegroundFill.DIAMONDS;
        }
        return null;
    }

    @Override
    public int getIndent() {
        return style.getIndention();
    }

    @Override
    public int getRotation() {
        return style.getRotation();
    }

    @Override
    public String getFormat() {
        return style.getDataFormatString();
    }

    @Override
    public Font getFont() {
        return style.getFont() != null ? new PoiFont(style.getFont()) : null;
    }

    @Override
    public Border getBorder(Keywords.BorderSide borderSide) {
        if (Keywords.BorderSide.TOP.equals(borderSide)) {
            return new PoiBorder(style.getBorderColor(XSSFCellBorder.BorderSide.TOP), style.getBorderTop());
        } else if (Keywords.BorderSide.BOTTOM.equals(borderSide)) {
            return new PoiBorder(style.getBorderColor(XSSFCellBorder.BorderSide.BOTTOM), style.getBorderBottom());
        } else if (Keywords.BorderSide.LEFT.equals(borderSide)) {
            return new PoiBorder(style.getBorderColor(XSSFCellBorder.BorderSide.LEFT), style.getBorderLeft());
        } else if (Keywords.BorderSide.RIGHT.equals(borderSide)) {
            return new PoiBorder(style.getBorderColor(XSSFCellBorder.BorderSide.RIGHT), style.getBorderRight());
        }
        return new PoiBorder(null, null);
    }

    private final XSSFCellStyle style;
}
