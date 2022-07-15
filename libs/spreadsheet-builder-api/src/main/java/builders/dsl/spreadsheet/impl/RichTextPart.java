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
package builders.dsl.spreadsheet.impl;

import builders.dsl.spreadsheet.builder.api.FontDefinition;

public final class RichTextPart {

    public RichTextPart(String text, FontDefinition font, int start, int end) {
        this.text = text;
        this.font = font;
        this.start = start;
        this.end = end;
    }

    public final String getText() {
        return text;
    }

    public final FontDefinition getFont() {
        return font;
    }

    public final int getStart() {
        return start;
    }

    public final int getEnd() {
        return end;
    }

    private final String text;
    private final FontDefinition font;
    private final int start;
    private final int end;
}
