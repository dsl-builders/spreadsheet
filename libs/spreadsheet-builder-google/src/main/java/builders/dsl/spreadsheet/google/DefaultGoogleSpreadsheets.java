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
package builders.dsl.spreadsheet.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class DefaultGoogleSpreadsheets implements GoogleSpreadsheets {

    private static final String APPLICATION_NAME = "Google Spreadsheet Builder";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String EXCEL_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String GOOGLE_SHEET_MIME_TYPE = "application/vnd.google-apps.spreadsheet";

    private final HttpRequestInitializer credentials;

    DefaultGoogleSpreadsheets(HttpRequestInitializer credentials) {
        this.credentials = credentials;
    }

    @Override
    public File updateAndConvert(final String id, final String name, final Iterable<String> fields, final Consumer<OutputStream> withOutputStream) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            withOutputStream.accept(out);

            final File googleFile = new File();
            googleFile.setName(name);
            googleFile.setMimeType(GOOGLE_SHEET_MIME_TYPE);

            String fieldsString = StreamSupport.stream(fields.spliterator(), false).collect(Collectors.joining(","));

            if (id == null) {
                final Drive.Files.Create create = drive().files().create(googleFile, new ByteArrayContent(EXCEL_MIME_TYPE, out.toByteArray()));
                return create.setFields(fieldsString).execute();
            }

            final Drive.Files.Update create = drive().files().update(id, googleFile, new ByteArrayContent(EXCEL_MIME_TYPE, out.toByteArray()));
            return create.setFields(fieldsString).execute();
        } catch (IOException e) {
            throw new IllegalStateException("Exception while uploading spreadsheet " + name, e);
        }
    }

    @Override
    public InputStream convertAndDownload(final String id) {
        try {
            Drive.Files.Export export = drive().files().export(id, EXCEL_MIME_TYPE);
            return export.executeMediaAsInputStream();
        } catch (IOException e) {
            throw new IllegalStateException("Exception while downloading spreadsheet " + id, e);
        }
    }

    @Override
    public Drive drive() {
        try {
            return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credentials)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Exception creating drive client", e);
        }
    }
}
