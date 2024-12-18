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
package builders.dsl.spreadsheet.builder.poi

import org.apache.poi.xssf.usermodel.XSSFColor
import spock.lang.Specification
import spock.lang.Unroll

class PoiCellStyleSpec extends Specification {

    @Unroll
    void "parse #hex to #r,#g,#b"() {
        when:
        XSSFColor color = PoiCellStyleDefinition.parseColor(hex)

        then:
        color.RGB == [r, g, b] as byte[]

        where:
        hex         | r     | g     | b
        '#000000'   |    0  |    0  |   0
        '#aabbcc'   |  -86  |  -69  | -52
        '#ffffff'   |   -1  |   -1  |  -1
    }

}
