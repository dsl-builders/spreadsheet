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
package builders.dsl.spreadsheet.cli

import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification
import spock.lang.TempDir

class SpreadsheetCliSpec extends Specification {

    @TempDir File temporaryDirectory

    void 'writes an Excel workbook from YAML data'() {
        given:
        File yaml = new File(temporaryDirectory, 'people.yml')
        yaml.text = '''\
sheets:
- name: People
  rows:
  - cells: [Name, Age, City]
  - cells: [Alice, 30, Prague]
  - cells: [Bob, 41, Brno]
'''
        File workbook = new File(temporaryDirectory, 'people.xlsx')

        when:
        SpreadsheetCli.run('create', yaml.absolutePath, workbook.absolutePath)

        then:
        workbook.file
        PoiSpreadsheetCriteria.FACTORY.forFile(workbook).query { book ->
            book.sheet('People') { sheet ->
                sheet.row(2) { row ->
                    row.cell('A') { cell -> cell.string('Alice') }
                }
            }
        }.cell
    }

    void 'writes an Excel workbook from JSON data'() {
        given:
        File json = new File(temporaryDirectory, 'people.json')
        json.text = '''\
{
  "sheets": [
    {
      "name": "People",
      "rows": [
        {"cells": ["Name", "Age", "City"]},
        {"cells": ["Alice", 30, "Prague"]},
        {"cells": ["Bob", 41, "Brno"]}
      ]
    }
  ]
}
'''
        File workbook = new File(temporaryDirectory, 'people-json.xlsx')

        when:
        SpreadsheetCli.run('create', json.absolutePath, workbook.absolutePath)

        then:
        workbook.file
        PoiSpreadsheetCriteria.FACTORY.forFile(workbook).query { book ->
            book.sheet('People') { sheet ->
                sheet.row(3) { row ->
                    row.cell('C') { cell -> cell.string('Brno') }
                }
            }
        }.cell
    }

    void 'queries an Excel workbook using serialized YAML criteria'() {
        given:
        File workbook = workbook()
        File query = new File(temporaryDirectory, 'query.yml')
        query.text = '''\
sheets:
- name: People
  rows:
  - from: 2
    to: 4
    cells:
    - column: C
      value: Prague
'''

        when:
        String output = captureStandardOutput {
            SpreadsheetCli.run('query', workbook.absolutePath, query.absolutePath)
        }
        Map result = new ObjectMapper().readValue(output, Map)

        then:
        result.cells*.row == [2, 4]
        result.cells*.column == ['C', 'C']
        result.cells*.value == ['Prague', 'Prague']
        result.rows[0].values == ['Alice', 30.0, 'Prague']
    }

    void 'query JSON schema is packaged as a CLI resource'() {
        expect:
        SpreadsheetCli.classLoader.getResource('builders/dsl/spreadsheet/cli/query.schema.json')

        when:
        Map schema = new ObjectMapper().readValue(
            SpreadsheetCli.classLoader.getResource('builders/dsl/spreadsheet/cli/query.schema.json'),
            Map
        )

        then:
        schema.'$schema' == 'https://json-schema.org/draft/2020-12/schema'
        schema.title == 'Spreadsheet Builder CLI Query Criteria'
    }

    void 'queries an Excel workbook using serialized JSON criteria'() {
        given:
        File workbook = workbook()
        File query = new File(temporaryDirectory, 'query.json')
        query.text = '''\
{
  "sheets": [
    {
      "name": "People",
      "rows": [
        {"cells": [{"column": "A", "string": "Bob"}]}
      ]
    },
    {
      "name": "People",
      "rows": [
        {"cells": [{"column": "A", "string": "Carol"}]}
      ]
    }
  ]
}
'''

        when:
        String output = captureStandardOutput {
            SpreadsheetCli.run('query', workbook.absolutePath, query.absolutePath)
        }
        Map result = new ObjectMapper().readValue(output, Map)

        then:
        result.cells*.value == ['Bob', 'Carol']
        result.rows*.row == [3, 4]
    }

    private static String captureStandardOutput(Closure<?> action) {
        PrintStream original = System.out
        ByteArrayOutputStream buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer, true, 'UTF-8')
        try {
            action.call()
        } finally {
            System.out = original
        }
        return buffer.toString('UTF-8')
    }

    private File workbook() {
        File yaml = new File(temporaryDirectory, "people-${System.nanoTime()}.yml")
        yaml.text = '''\
sheets:
- name: People
  rows:
  - cells: [Name, Age, City]
  - cells: [Alice, 30, Prague]
  - cells: [Bob, 41, Brno]
  - cells: [Carol, 29, Prague]
'''
        File workbook = new File(temporaryDirectory, "people-${System.nanoTime()}.xlsx")
        SpreadsheetCli.run('create', yaml.absolutePath, workbook.absolutePath)
        return workbook
    }

}
