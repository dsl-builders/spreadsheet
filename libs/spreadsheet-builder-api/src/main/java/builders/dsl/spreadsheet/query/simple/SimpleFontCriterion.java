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
package builders.dsl.spreadsheet.query.simple;

import builders.dsl.spreadsheet.api.*;
import builders.dsl.spreadsheet.query.api.FontCriterion;
import java.util.function.Predicate;

import java.util.EnumSet;

final class SimpleFontCriterion implements FontCriterion {

    private final SimpleCellCriterion parent;

    SimpleFontCriterion(SimpleCellCriterion parent) {
        this.parent = parent;
    }

    @Override
    public SimpleFontCriterion color(String hexColor) {
        color(new Color(hexColor));
        return this;
    }

    @Override
    public SimpleFontCriterion color(final Color color) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Font font = style.getFont();
            return font != null && color.equals(font.getColor());
        });
        return this;
    }

    @Override
    public SimpleFontCriterion color(final Predicate<Color> conition) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Font font = style.getFont();
            return font != null && conition.test(font.getColor());
        });
        return this;
    }

    @Override
    public SimpleFontCriterion size(final int size) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Font font = style.getFont();
            return font != null && size == font.getSize();
        });
        return this;
    }

    @Override
    public SimpleFontCriterion size(final Predicate<Integer> predicate) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Font font = style.getFont();
            return font != null && predicate.test(font.getSize());
        });
        return this;
    }

    @Override
    public SimpleFontCriterion name(final String name) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Font font = style.getFont();
            return font != null && name.equals(font.getName());
        });
        return this;
    }

    @Override
    public SimpleFontCriterion name(final Predicate<String> predicate) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Font font = style.getFont();
            return font != null && predicate.test(font.getName());
        });
        return this;
    }

    @Override
    public SimpleFontCriterion style(final FontStyle first, final FontStyle... other) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Font font = style.getFont();
            if (font == null) {
                return false;
            }

            EnumSet<FontStyle> wanted = EnumSet.of(first, other);
            EnumSet<FontStyle> actual = font.getStyles();

            for (FontStyle fs : wanted) {
                if (!actual.contains(fs)) {
                    return false;
                }
            }

            return true;
        });
        return this;
    }

    @Override
    public SimpleFontCriterion style(final Predicate<EnumSet<FontStyle>> predicate) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Font font = style.getFont();
            return font != null && predicate.test(font.getStyles());
        });
        return this;
    }

    @Override
    public FontCriterion having(final Predicate<Font> fontPredicate) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Font font = style.getFont();
            return font != null && fontPredicate.test(font);
        });
        return this;
    }
}
