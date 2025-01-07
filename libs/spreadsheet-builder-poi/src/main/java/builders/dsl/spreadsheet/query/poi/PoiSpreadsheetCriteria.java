/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2025 Vladimir Orany.
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

import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria;
import builders.dsl.spreadsheet.query.simple.SimpleSpreadsheetCriteria;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public enum PoiSpreadsheetCriteria {

    FACTORY;

    public SpreadsheetCriteria forFile(File spreadsheet) throws FileNotFoundException {
        return forStream(new FileInputStream(spreadsheet));
    }

    public SpreadsheetCriteria forStream(InputStream stream) {
        try {
            return SimpleSpreadsheetCriteria.forWorkbook(new PoiWorkbook(new XSSFWorkbook(stream)));
        } catch (IOException e) {
            throw new RuntimeException("Exception creating new workbook: " + stream, e);
        }
    }

}
