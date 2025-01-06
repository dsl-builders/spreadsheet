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
package builders.dsl.spreadsheet.parser.data.yml;

import builders.dsl.spreadsheet.parser.data.DataSpreadsheetParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder;

import java.io.IOException;
import java.io.InputStream;

public class YmlSpreadsheetParser {
    private final DataSpreadsheetParser facade;

    public YmlSpreadsheetParser(SpreadsheetBuilder builder) {
        facade = new DataSpreadsheetParser(builder);
    }

    public void parse(InputStream json) throws IOException {
        facade.build(new ObjectMapper(new YAMLFactory()).readValue(json, Object.class));
    }
}
