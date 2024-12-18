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
package builders.dsl.spreadsheet.builder.api.tck.java8;

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.CanDefineStyle;
import builders.dsl.spreadsheet.builder.api.Stylesheet;

import static builders.dsl.spreadsheet.api.Color.*;

public class MyStyles implements Stylesheet {

    public void declareStyles(CanDefineStyle stylable) {
        stylable.style("h1", s -> {
            s.foreground(whiteSmoke)
            .fill(Keywords.solidForeground)
            .font(f -> {
                f.size(22);
            });
        });
        stylable.style("h2", s -> {
            s.base("h1");
            s.font(f -> {
                f.size(16);
            });
        });
    }
}