/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2026 Vladimir Orany.
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
package builders.dsl.spreadsheet.builder.tck

import builders.dsl.spreadsheet.builder.api.CanDefineStyle
import builders.dsl.spreadsheet.builder.api.CellStyleDefinition
import builders.dsl.spreadsheet.builder.api.FontDefinition
import builders.dsl.spreadsheet.builder.api.Stylesheet
import groovy.transform.CompileStatic

@SuppressWarnings([
        'DuplicateStringLiteral'
])
@CompileStatic
class MyStyles implements Stylesheet {

    void declareStyles(CanDefineStyle stylable) {
        stylable.style('h1') { CellStyleDefinition style ->
            style.foreground style.whiteSmoke
            style.fill style.solidForeground
            style.font { FontDefinition font ->
                font.size 22
            }
        }
        stylable.style('h2') { CellStyleDefinition style ->
            style.base 'h1'
            style.font { FontDefinition font ->
                font.size 16
            }
        }
    }

}
