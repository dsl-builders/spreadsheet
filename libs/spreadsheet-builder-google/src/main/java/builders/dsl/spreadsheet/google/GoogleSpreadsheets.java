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
package builders.dsl.spreadsheet.google;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.function.Consumer;

public interface GoogleSpreadsheets {

    Iterable<String> DEFAULT_FIELDS = Arrays.asList("webViewLink", "id");

    static GoogleSpreadsheets create(HttpRequestInitializer credentials) {
        return new DefaultGoogleSpreadsheets(credentials);
    }

    default File uploadAndConvert(final String name, final Consumer<OutputStream> withOutputStream) {
        return uploadAndConvert(name, DEFAULT_FIELDS, withOutputStream);
    }

    default File uploadAndConvert(final String name, final Iterable<String> fields, final Consumer<OutputStream> withOutputStream) {
        return updateAndConvert(null, name, fields, withOutputStream);
    }

    default File updateAndConvert(String id, final String name, final Consumer<OutputStream> withOutputStream) {
        return updateAndConvert(id, name, DEFAULT_FIELDS, withOutputStream);
    }

    File updateAndConvert(String id, final String name, final Iterable<String> fields, final Consumer<OutputStream> withOutputStream);

    InputStream convertAndDownload(final String id);

    Drive drive();

}