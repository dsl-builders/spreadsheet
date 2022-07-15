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

import builders.dsl.spreadsheet.api.Comment;
import builders.dsl.spreadsheet.builder.api.CommentDefinition;

public final class DefaultCommentDefinition implements CommentDefinition, Comment {

    @Override
    public CommentDefinition author(String author) {
        this.author = author;
        return this;
    }

    @Override
    public CommentDefinition text(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        this.text = text;
        return this;
    }

    public void width(int widthInCells) {
        assert widthInCells > 0;
        this.width = widthInCells;
    }

    public void height(int heightInCells) {
        assert heightInCells > 0;
        this.height = heightInCells;
    }


    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getText() {
        return text;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private String author;
    private String text;
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
}
