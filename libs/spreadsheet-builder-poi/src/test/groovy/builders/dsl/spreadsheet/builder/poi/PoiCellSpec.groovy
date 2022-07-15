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
package builders.dsl.spreadsheet.builder.poi

import builders.dsl.spreadsheet.impl.Utils
import spock.lang.Specification
import spock.lang.Unroll

class PoiCellSpec extends Specification {

    @Unroll
    void "normalize name #name to #result"() {
        expect:
            Utils.fixName(name) == result

        where:
            name        | result
            'C'         | /_C/
            'c'         | /_c/
            'R'         | /_R/
            'r'         | /_r/
            '10'        | /_10/
            '(5)'       | /_5_/
            '2.4'       | /_2.4/
            'foo'       | /foo/
            'foo bar'   | /foo_bar/
    }

}
