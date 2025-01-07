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
package builders.dsl.spreadsheet.query.simple;

import builders.dsl.spreadsheet.api.*;
import builders.dsl.spreadsheet.query.api.BorderCriterion;
import java.util.function.Predicate;

final class SimpleBorderCriterion implements BorderCriterion {

    private final SimpleCellCriterion parent;
    private final Keywords.BorderSide side;

    SimpleBorderCriterion(SimpleCellCriterion parent, Keywords.BorderSide side) {
        this.parent = parent;
        this.side = side;
    }

    @Override
    public SimpleBorderCriterion style(final BorderStyle borderStyle) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Border border = style.getBorder(side);
            return border != null && borderStyle.equals(border.getStyle());
        });
        return this;
    }

    @Override
    public SimpleBorderCriterion style(final Predicate<BorderStyle> predicate) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Border border = style.getBorder(side);
            return border != null && predicate.test(border.getStyle());
        });
        return this;
    }

    @Override
    public SimpleBorderCriterion color(String hexColor) {
        color(new Color(hexColor));
        return this;
    }

    @Override
    public SimpleBorderCriterion color(final Color color) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Border border = style.getBorder(side);
            return border != null && color.equals(border.getColor());
        });
        return this;
    }

    @Override
    public SimpleBorderCriterion color(final Predicate<Color> predicate) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Border border = style.getBorder(side);
            return border != null && predicate.test(border.getColor());
        });
        return this;
    }

    @Override
    public BorderCriterion having(final Predicate<Border> borderPredicate) {
        parent.addCondition(o -> {
            CellStyle style = o.getStyle();
            if (style == null) {
                return false;
            }
            Border border = style.getBorder(side);
            return border != null && borderPredicate.test(border);
        });
        return this;
    }
}
