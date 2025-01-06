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
package builders.dsl.spreadsheet.query.google;

import builders.dsl.spreadsheet.google.GoogleSpreadsheets;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria;
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria;
import com.google.api.client.http.HttpRequestInitializer;

public class GoogleSpreadsheetCriteriaFactory {

    public static GoogleSpreadsheetCriteriaFactory create(String spreadsheetId, GoogleSpreadsheets spreadsheets) {
        return create(spreadsheetId, spreadsheets);
    }

    public static GoogleSpreadsheetCriteriaFactory create(String spreadsheetId, HttpRequestInitializer credentials) {
        return new GoogleSpreadsheetCriteriaFactory(spreadsheetId, GoogleSpreadsheets.create(credentials));
    }

    private final String id;
    private final GoogleSpreadsheets spreadsheets;

    private GoogleSpreadsheetCriteriaFactory(String id, GoogleSpreadsheets spreadsheets) {
        this.id = id;
        this.spreadsheets = spreadsheets;
    }

    public SpreadsheetCriteria criteria() {
        return PoiSpreadsheetCriteria.FACTORY.forStream(spreadsheets.convertAndDownload(id));
    }

}
