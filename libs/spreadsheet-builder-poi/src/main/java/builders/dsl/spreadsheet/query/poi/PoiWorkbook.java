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
package builders.dsl.spreadsheet.query.poi;

import builders.dsl.spreadsheet.api.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PoiWorkbook implements Workbook {

    private final XSSFWorkbook workbook;

    PoiWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public List<builders.dsl.spreadsheet.api.Sheet> getSheets() {
        List<builders.dsl.spreadsheet.api.Sheet> sheets = new ArrayList<builders.dsl.spreadsheet.api.Sheet>();
        for (Sheet s : workbook) {
            sheets.add(new PoiSheet(this, (XSSFSheet) s));
        }
        return Collections.unmodifiableList(sheets);
    }

}
