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
package builders.dsl.spreadsheet.api

import builders.dsl.spreadsheet.impl.Utils
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Tests for Cell
 */
class CellUtilSpec extends Specification {

    @Unroll
    void "parse column #column to number #index"() {
        expect:
        Utils.parseColumn(column) == index
            Utils.toColumn(index) == column
        where:
        column  | index
        'A'     | 1
        'B'     | 2
        'Z'     | 26
        'AA'    | 27
        'AB'    | 28
        'DA'    | 105
    }

}
