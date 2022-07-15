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
package builders.dsl.spreadsheet.builder.data;

import builders.dsl.spreadsheet.api.BorderStyle;
import builders.dsl.spreadsheet.api.Color;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.BorderDefinition;

import java.util.List;

class BorderNode extends AbstractNode implements BorderDefinition {

    @Override
    public BorderDefinition style(BorderStyle style) {
        node.set("style", style);
        return this;
    }

    @Override
    public BorderDefinition color(String hexColor) {
        node.set("color", hexColor);
        return this;
    }

    @Override
    public BorderDefinition color(Color color) {
        node.set("color", color.getHex());
        return this;
    }


    public void setSide(List<Keywords.BorderSide> sides) {
        node.set("side", sides);
    }

}
