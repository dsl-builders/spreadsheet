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

import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder
import builders.dsl.spreadsheet.builder.tck.AbstractBuilderSpec
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria
import spock.lang.TempDir

class PoiStreamingExcelBuilderSpec extends AbstractBuilderSpec {

    @TempDir File tmp

    File tmpFile

    void setup() {
        tmpFile = new File(tmp, "sample${System.currentTimeMillis()}.xlsx")
    }

    @Override
    protected SpreadsheetCriteria createCriteria() {
        return PoiSpreadsheetCriteria.FACTORY.forFile(tmpFile)
    }

    @Override
    protected SpreadsheetBuilder createSpreadsheetBuilder() {
        return PoiSpreadsheetBuilder.stream(tmpFile)
    }

    @Override
    protected void openSpreadsheet() {
        open tmpFile
    }

}
