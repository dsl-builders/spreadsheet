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
package builders.dsl.spreadsheet.builder.data

import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder
import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder
import builders.dsl.spreadsheet.builder.tck.AbstractBuilderSpec
import builders.dsl.spreadsheet.parser.data.json.JsonSpreadsheetParser
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared

class DataSpreadsheetBuilderSpec extends AbstractBuilderSpec {

    @Shared ObjectMapper mapper = new ObjectMapper()
    @Rule TemporaryFolder tmp = new TemporaryFolder()

    File spreadsheetFile
    File jsonFile

    DataSpreadsheetBuilder builder = DataSpreadsheetBuilder.create()

    void setup() {
        spreadsheetFile = tmp.newFile("sample${System.currentTimeMillis()}.xlsx")
        jsonFile = tmp.newFile("sample${System.currentTimeMillis()}.json")
    }

    @Override
    protected SpreadsheetCriteria createCriteria() {
        return PoiSpreadsheetCriteria.FACTORY.forFile(spreadsheetFile)
    }

    @Override
    protected SpreadsheetBuilder createSpreadsheetBuilder() {
        return builder
    }

    @Override
    protected void openSpreadsheet() {
        mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, builder.data)

        open jsonFile

        SpreadsheetBuilder poiBuilder = PoiSpreadsheetBuilder.create(spreadsheetFile)
        new JsonSpreadsheetParser(poiBuilder).parse(jsonFile.newInputStream())

        open spreadsheetFile
    }

}
