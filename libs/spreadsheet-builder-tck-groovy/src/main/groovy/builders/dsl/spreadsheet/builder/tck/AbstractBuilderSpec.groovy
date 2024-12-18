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
package builders.dsl.spreadsheet.builder.tck

import builders.dsl.spreadsheet.api.Cell
import builders.dsl.spreadsheet.api.Color
import builders.dsl.spreadsheet.api.DataRow
import builders.dsl.spreadsheet.api.ForegroundFill
import builders.dsl.spreadsheet.api.Row
import groovy.transform.CompileStatic
import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteriaResult
import spock.lang.Specification

import java.awt.Desktop

@SuppressWarnings([
    'NestedBlockDepth',
    'AbcMetric',
    'NoJavaUtilDate',
    'MethodSize',
    'BuilderMethodWithSideEffects',
    'ClassSize',
])
abstract class AbstractBuilderSpec extends Specification {

    void 'create sample spreadsheet'() {
        when:
            SpreadsheetBuilder builder = createSpreadsheetBuilder()

            Date today = new Date()

            buildSpreadsheet(builder, today)
            openSpreadsheet()
        then:
            noExceptionThrown()

        when:
            SpreadsheetCriteria matcher = createCriteria()
            SpreadsheetCriteriaResult allCells = matcher.all()

        then:
            allCells

        when:
            int allCellSize = allCells.size()
            int sheetCount = allCells.sheets.size()
            int rowsCount = allCells.rows.size()
        then:
            allCellSize == expectedAllCellSize
            sheetCount == 21
            rowsCount == expectedAllRowsSize

        when:
            SpreadsheetCriteriaResult sampleCells = matcher.query {
                sheet('Sample')
            }
        then:
            sampleCells
            sampleCells.size() == 2
            sampleCells.sheets.size() == 1

        when:
            Iterable<Cell> rowCells = matcher.query {
                sheet('many rows') {
                    row 1
                }
            }
        then:
            rowCells
            rowCells.size() == 4
            rowCells.sheets.size() == 1
            rowCells.rows.size() == 1
        when:
            Row manyRowsHeader = matcher.query {
                sheet('many rows') {
                    row 1
                }
            }.row
        then:
            manyRowsHeader
        when:
            Row manyRowsDataRow = matcher.query {
                sheet('many rows') {
                    row(2)
                }
            }.row
            DataRow dataRow = DataRow.create(manyRowsDataRow, manyRowsHeader)
        then:
            dataRow['One']
            dataRow['One'].value == '1'

        when:
            DataRow dataRowFromMapping = DataRow.create(manyRowsDataRow, primo: 1)
        then:
            dataRowFromMapping['primo']
            dataRowFromMapping['primo'].value == '1'

        when:
            Iterable<Cell> someCells = matcher.query {
                sheet {
                    row {
                        cell {
                            date today
                        }
                    }
                }
            }
        then:
            someCells
            someCells.size() == 1

        when:
            Iterable<Cell> commentedCells = matcher.query {
                sheet {
                    row {
                        cell {
                            comment 'This is a date!'
                        }
                    }
                }
            }
        then:
            commentedCells
            commentedCells.size() == 1

        when:
            Iterable<Cell> dateCells = matcher.query {
                sheet {
                    row {
                        cell {
                            style {
                                format 'd.m.y'
                            }
                        }
                    }
                }
            }
        then:
            dateCells
            dateCells.size() == 1
        when:
            Iterable<Cell> magentaCells = matcher.query {
                sheet {
                    row {
                        cell {
                            style {
                                foreground aquamarine
                            }
                        }
                    }
                }
            }
        then:
            magentaCells
            magentaCells.size() == 1
        when:
            Iterable<Cell> redOnes = matcher.query {
                sheet {
                    row {
                        cell {
                            style {
                                font {
                                    color red
                                }
                            }
                        }
                    }
                }
            }
        then:
            redOnes
            redOnes.size() == 20006
            redOnes.rows.size() == 20004

        when:
            Iterable<Cell> boldOnes = matcher.query {
                sheet {
                    row {
                        cell {
                            style {
                                font {
                                    style bold
                                }
                            }
                        }
                    }
                }
            }
        then:
            boldOnes
            boldOnes.size() == 5

        when:
            Iterable<Cell> bigOnes = matcher.query {
                sheet {
                    row {
                        cell {
                            style {
                                font {
                                    size 22
                                }
                            }
                        }
                    }
                }
            }
        then:
            bigOnes
            bigOnes.size() == 40002

        when:
            Iterable<Cell> bordered = matcher.query {
                sheet {
                    row {
                        cell {
                            style {
                                border(top) {
                                    style thin
                                }
                            }
                        }
                    }
                }
            }
        then:
            bordered
            bordered.size() == 10
        when:
            Iterable<Cell> combined = matcher.query {
                sheet {
                    row {
                        cell {
                            value 'Bold Red 22'
                            style {
                                font {
                                    color red
                                }
                            }
                        }
                    }
                }
            }
        then:
            combined
            combined.size() == 1

        when:
            Iterable<Cell> conjunction = matcher.query {
                sheet {
                    row {
                        or {
                            cell {
                                value 'Bold Red 22'
                            }
                            cell {
                                value 'A'
                            }
                        }
                    }
                }
            }
        then:
            conjunction
            conjunction.size() == 3

        when:
            Iterable<Cell> traversal = matcher.query {
                sheet('Traversal') {
                    row {
                        cell {
                            value 'E'
                        }
                    }
                }
            }
        then:
            traversal
            traversal.size() == 1

        when:
            Cell cellE = traversal.first()
        then:
            cellE.row.sheet.name == 'Traversal'
            cellE.row.sheet.previous
            cellE.row.sheet.previous.name == 'Formula'
            cellE.row.sheet.next
            cellE.row.sheet.next.name == 'Border'
            cellE.row.number == 2
            cellE.row.above
            cellE.row.above.number == 1
            cellE.row.below
            cellE.row.below.number == 3
            cellE.colspan == 2
            cellE.aboveLeft
            cellE.aboveLeft.value == 'A'
            cellE.above
            cellE.above.value == 'B'
            cellE.aboveRight
            cellE.aboveRight.value == 'C'
            cellE.left
            cellE.left.value == 'D'
            cellE.right
            cellE.right.value == 'F'
            cellE.belowLeft
            cellE.belowLeft.value == 'G'
            cellE.belowRight
            cellE.belowRight.value == 'I'
            cellE.below
            cellE.below.value == 'H'
            cellE.below.below.value == 'J'
        when:
            Iterable<Cell> zeroCells = matcher.query {
                sheet('Zero') {
                    row {
                        cell {
                            value 0d
                        }
                    }
                }
            }
        then:
            zeroCells
            zeroCells.size() == 1
            zeroCells.first().value == 0d
            zeroCells.cell
            zeroCells.cell.value == 0d
        when:
            Cell noneCell = matcher.find {
                sheet('Styles') {
                    row {
                        cell {
                            value 'NONE'
            }   }   }   }
            Cell redCell = matcher.find {
                sheet('Styles') {
                    row {
                        cell {
                            value 'RED'
            }   }   }   }
            Cell blueCell = matcher.find {
                sheet('Styles') {
                    row {
                        cell {
                            value 'BLUE'
            }   }   }   }
            Cell greenCell = matcher.find {
                sheet('Styles') {
                    row {
                        cell {
                            value 'GREEN'
            }   }   }   }
        then:
            !noneCell?.style?.foreground
            redCell?.style?.foreground == Color.red
            blueCell?.style?.foreground == Color.blue
            greenCell?.style?.foreground == Color.green

        expect:
            matcher.query {
                sheet {
                    page {
                        paper a5
                        orientation landscape
            }   }   }.size() == 1

        and:
            matcher.query {
                sheet('Hidden') {
                    state hidden
                }
            }.sheets.size() == 1

        when:
            Iterable<Cell> namedCells = matcher.query {
                sheet('Formula') {
                    row {
                        cell {
                            name '_Cell10'
                        }
                    }
                }
            }
        then:
            namedCells
            namedCells.size() == 1

        when:
            Iterable<Cell> filledCells = matcher.query {
                sheet {
                    row {
                        cell {
                            style {
                                fill fineDots
                            }
                        }
                    }
                }
            }
        then:
            !fillSupported || filledCells
            !fillSupported || filledCells.size() == 1

        expect:
            !veryHiddenSupported || matcher.query {
                sheet('Very hidden') {
                    state veryHidden
                }
            }.sheets.size() == 1
    }

    protected abstract SpreadsheetCriteria createCriteria()
    protected abstract SpreadsheetBuilder createSpreadsheetBuilder()

    protected int getExpectedAllRowsSize() { return 20068 }
    protected int getExpectedAllCellSize() { return 80134 }

    @SuppressWarnings('EmptyMethodInAbstractClass')
    protected void openSpreadsheet() { }

    protected boolean isVeryHiddenSupported() { return true }
    protected boolean isFillSupported() { return true }

    /**
     * Tries to open the file in Word. Only works locally on Mac at the moment. Ignored otherwise.
     * Main purpose of this method is to quickly open the generated file for manual review.
     * @param file file to be opened
     */
    @SuppressWarnings('StaticMethodsBeforeInstanceMethods')
    protected static void open(File file) {
        try {
            if (Desktop.desktopSupported && Desktop.desktop.isSupported(Desktop.Action.OPEN)) {
                Desktop.desktop.open(file)
                Thread.sleep(10000)
            }
        } catch (ignored) {
            // CI
        }
    }

    @SuppressWarnings([
            'FactoryMethodName',
            'BracesForForLoop',
    ])
    @CompileStatic
    private static void buildSpreadsheet(SpreadsheetBuilder builder, Date today) {
        builder.build {
            style 'red', {
                font {
                    color red
                }
            }
            style 'bold', {
                font {
                    style bold
                }
            }

            style 'h1', {
                font {
                    style bold
                }
            }

            style 'h2', {
                font {
                    style bold
                }
            }

            style 'borders', {
                font {
                    color red
                }
                border {
                    style thin
                    color black
                }
            }

            style 'centered', {
                align center, center
            }

            style 'redfg', {
                foreground red
            }

            style 'greenfg', {
                foreground green
            }

            style 'bluefg', {
                foreground blue
            }

            style 'nonefg', { }

            apply MyStyles // or apply(new MyStyles())

            sheet('many rows') {
                filter auto
                row {
                    cell 'One'
                    cell 'Two'
                    cell 'Three'
                    cell 'Four'
                }
                20000.times {
                    row {
                        cell {
                            value '1'
                            styles 'h1', 'red'
                        }
                        cell {
                            value '2'
                            style 'h2'
                        }
                        cell {
                            value '3'
                            style 'h1'
                        }
                        cell {
                            value '4'
                            style 'h2'
                        }
                    }
                }
            }

            sheet('Sample') {
                row {
                    cell {
                        value 'Hello'
                        style 'h1'
                    }
                    cell {
                        value 'World'
                        style 'h2'
                    }
                }
            }

            sheet('Fonts') {
                row {
                    cell {
                        width auto
                        value 'Bold Red 22'
                        style {
                            font {
                                style bold
                                color red
                                size 22
                            }
                        }
                    }
                    cell {
                        width auto
                        value 'Underline Courier New'
                        style {
                            font {
                                style underline
                                name 'Courier New'
                            }
                        }
                    }
                    cell {
                        width auto
                        value 'Italic'
                        style {
                            font {
                                style italic
                            }
                        }
                    }
                    cell {
                        width auto
                        value 'Strikeout'
                        style {
                            font {
                                style strikeout
                            }
                        }
                    }
                }
            }

            sheet('Image') {
                row(3) {
                    cell('C') {
                        // png image from 'https://goo.gl/UcL1wy'
                    }
                }
            }

            sheet('Rich Text') {
                row {
                    cell {
                        text 'Little'
                        text ' '
                        text 'Red', {
                            color red
                            size 22
                            name 'Times New Roman'
                        }
                        text ' '
                        text 'Riding', {
                            style italic
                            size 18
                        }
                        text ' '
                        text 'Hood', {
                            style bold
                            size 22
                        }
                    }
                }
                row {
                    cell {
                        style {
                            wrap text
                        }
                        text 'First Line'
                        text '\n'
                        text 'Second Line', {
                            style bold
                            size 12
                        }
                        text '\n'
                        for (Map.Entry<String, String> entry in [foo: 'bar', boo: 'cow', empty: '', '': 'nothing'].entrySet()) {
                            text entry.key, {
                                style bold
                            }
                            text ': '
                            text entry.value
                            text '\n'
                        }
                        text '\n\n'
                        text 'Next line after two spaces'
                        text '\n'
                        text 'Last line', {
                            style italic
                        }
                        text '\n'
                    }
                }

                row {
                    cell {
                        text 'Genomics England Consent Withdrawal Options'
                        text '\n\n'
                        text 'Enumerations', {
                            size 12
                            style bold
                        }
                        text '\n'

                        for (Map.Entry<String, String> entry in [
                                FULL_WITHDRAWAL: 'OPTION 2: FULL WITHDRAWAL: No further use',
                                PARTIAL_WITHDRAWAL: 'OPTION 1: PARTIAL WITHDRAWAL: No further contact',
                        ].entrySet()) {
                            text entry.key, {
                                style bold
                            }
                            text ': '
                            text entry.value
                            text '\n'
                        }
                    }
                }
            }

            sheet('Cell Addressing') {
                filter auto
                row(2) {
                    style {
                        background whiteSmoke
                        border top, bottom, {
                            style thin
                            color black
                        }
                    }
                    cell('B') {
                        value 'A'
                        style {
                            border left, {
                                style thin
                                color black
                            }
                        }
                    }
                    cell 'B'
                    cell {
                        value 'C'
                        style {
                            border right, {
                                style thin
                                color black
                            }
                        }
                    }
                }
                row {
                    cell('B') { value 1 }
                    cell 2
                    cell 3
                }
            }
            sheet('One') {
                filter auto
                freeze 1, 1
                row {
                    cell 'First Row'
                }

                row {
                    cell 'AC', {
                        value 'AC'
                    }
                    cell 'BE', {
                        value 'BE'
                    }
                }

                row {
                    style {
                        align center, left
                        border {
                            color '#abcdef'
                            style dashDotDot
                        }
                        border right, {
                            color '#00ff00'
                        }
                    }
                    cell {
                        value 'x' * 10000
                        name 'Salutation'
                        width auto
                    }
                    cell {
                        style 'bold'
                        value 'World'
                        comment {
                            text 'This cell has some fancy fg/bg'
                            author 'musketyr'
                        }
                        width 50
                    }
                    cell {
                        style {
                            format 'd.m.y'
                            align center, center
                        }
                        value today
                        comment 'This is a date!'
                        colspan 5
                        rowspan 2
                    }
                }
            }
            sheet('Links') {
                freeze 1, 0
                row {
                    cell {
                        value 'Document (and a very long text)'
                        link to name 'Salutation'
                        width auto
                    }
                    cell {
                        value 'File'
                        link to file 'text.txt'
                    }
                    cell {
                        value 'URL'
                        link to url 'https://www.google.com'
                    }
                    cell {
                        value 'Mail (plain)'
                        link to email 'vladimir@orany.cz'
                    }
                    cell {
                        value 'Mail (with subject)'
                        link to email 'vladimir@orany.cz', subject: 'Testing Excel Builder', body: 'It is really great tools'
                    }
                }
            }
            sheet('Groups') {
                row {
                    cell 'Headline 1'
                    group {
                        cell {
                            value 'Headline 2'
                            style {
                                foreground aquamarine
                                fill solidForeground
                            }
                        }
                        cell 'Headline 3'
                        collapse {
                            cell 'Headline 4'
                            cell 'Headline 5'
                        }
                        cell 'Headline 6'
                    }
                    cell 'Headline 7'
                    collapse {
                        cell 'Headline 8'
                    }
                }
                group {
                    row {
                        cell 'Some stuff'
                    }
                    collapse {
                        row {
                            cell 'Something'
                        }
                        row {
                            cell 'Something other'
                        }
                    }
                    row {
                        cell 'Other stuff'
                    }
                }
                row {
                    cell()
                }
                collapse {
                    row {
                        cell 'Collapse one row'
                    }
                }
            }
            sheet('Fills') {
                for (ForegroundFill foregroundFill in ForegroundFill.values()) {
                    row {
                        cell {
                            width auto
                            value foregroundFill.name()
                        }
                        cell {
                            style {
                                background '#FF8C00' // darkOrange
                                foreground brown
                                fill foregroundFill
                            }
                        }
                    }
                }
            }
            sheet('Formula') {
                row {
                    cell {
                        value 10
                        name '_Cell10'
                    }
                    cell {
                        value 20
                        name '_Cell20'
                    }
                    cell {
                        formula 'SUM(#{_Cell10}:#{_Cell20})'
                    }
                }
            }
            // create sheet with same name, it should query the
            sheet('Formula') {
                row {
                    cell {
                        value 30
                        name '_Cell30'
                    }
                    cell {
                        value 40
                        name '_Cell40'
                    }
                    cell {
                        formula 'SUM(#{_Cell30}:#{_Cell40})'
                    }
                }
            }
            sheet('Traversal') {
                row {
                    cell 'A'
                    cell 'B'
                    cell 'C'
                }
                row {
                    cell 'D'
                    cell {
                        value 'E'
                        colspan 2
                    }
                    cell 'F'
                }
                row {
                    cell 'G'
                    cell {
                        value 'H'
                        rowspan(2)
                    }
                    cell 'I'
                }
                row(5) {
                    cell('B') {
                        value 'J'
            }   }   }
            sheet('Border') {
                row {
                    style 'borders'
                    cell {
                        value 1
                        colspan(2)
                    }
                    cell {
                        value 2
                    }
                }
                row {
                    style 'borders'
                    cell {
                        value 1
                        colspan(2)
                    }
                    cell {
                        value 2
                    }
                }
            }
            sheet('Zero') {
                row {
                    cell {
                        value 0
                    }
                }
            }
            sheet('Filtered') {
                filter auto
                row {
                    cell 'Name'
                    cell 'Profession'
                }
                row {
                    cell 'Donald'
                    cell 'Sailor'
                }
                row {
                    cell 'Bob'
                    cell 'Builder'
                }
            }

            sheet('Styles') {
                row {
                    style 'nonefg'
                    cell {
                        value 'NONE'
                        rowspan 3
                        style 'centered'
                    }
                }
                row(4) {
                    style 'redfg'
                    cell {
                        value 'RED'
                        rowspan 3
                        style 'centered'
                    }
                }
                row(7) {
                    style 'greenfg'
                    cell {
                        value 'GREEN'
                        rowspan 3
                        styles 'centered', 'bold'
                    }
                }
                row(10) {
                    styles 'bluefg', 'borders'
                    cell {
                        value 'BLUE'
                        rowspan 3
                        styles 'centered', 'bold'
                    }
                }
            }

            sheet('Dimensions') {
                row {
                    cell {
                        value 'cm'
                        height 1 cm
                        width 1 cm
                    }
                }

                row {
                    cell('B') {
                        value 'inches'
                        width 1 inch
                        height 1 inch
                    }
                }
                row {
                    cell('C') {
                        value 'points'
                        width 10
                        height 50
                    }
                }
            }

            sheet('Custom Page') {
                page {
                    paper a5
                    fit width to 1
                    orientation landscape
                }
                row {
                    cell 'A5 Landcapse'
                }
            }
            sheet('Broken row styles') {
                row {
                    styles 'bold', 'redfg'
                    cell {
                        value 'BOLD and RED'
                    }
                }
            }
            sheet('Hidden') {
                state hidden
            }
            sheet('Very hidden') {
                state veryHidden
            }
        }
    }

}
