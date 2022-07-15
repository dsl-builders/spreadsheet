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
package builders.dsl.spreadsheet.google

import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder
import builders.dsl.spreadsheet.builder.google.GoogleSpreadsheetBuilder
import builders.dsl.spreadsheet.builder.tck.AbstractBuilderSpec
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria
import builders.dsl.spreadsheet.query.google.GoogleSpreadsheetCriteriaFactory
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import spock.lang.Requires

import java.awt.Desktop

@SuppressWarnings('ClassStartsWithBlankLine')
@Requires({ System.getenv('DSL_SPREADSHEET_BUILDER_GOOGLE_CLIENT_SECRET') })
class GoogleBuilderSpec extends AbstractBuilderSpec {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.defaultInstance
    private static final String TOKENS_DIRECTORY_PATH = 'tokens'
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE)
    private static final String CREDENTIALS_FILE_PATH = 'credentials.json'
    private static final GoogleClientSecrets GOOGLE_SECRETS = buildGoogleSecrets()
    private static final String FILENAME = 'DELETE ME'

    HttpRequestInitializer credentials = buildCredentials(GOOGLE_SECRETS)
    GoogleSpreadsheetBuilder builder = GoogleSpreadsheetBuilder.builder(FILENAME, credentials).build()

    @SuppressWarnings('UnnecessaryGetter')
    void cleanup() {
        final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport()

        Drive service = new Drive.Builder(transport, JSON_FACTORY, credentials)
                .setApplicationName('Google Builder Spec')
                .build()

        FileList files = null

        while (!files || files.getIncompleteSearch()) {
            files = service.files().list().setPageToken(files?.getNextPageToken()).execute()
            for (File file in files.getFiles()) {
                if (file.getName() == FILENAME) {
                    service.files().delete(file.getId()).execute()
                }
            }
        }
    }

    @Override
    protected SpreadsheetCriteria createCriteria() {
        assert builder.id
        return GoogleSpreadsheetCriteriaFactory.create(builder.id, credentials).criteria()
    }

    @Override
    protected SpreadsheetBuilder createSpreadsheetBuilder() {
        return builder
    }

    @Override
    protected void openSpreadsheet() {
        browse builder.webLink
    }

    @Override
    protected boolean isVeryHiddenSupported() { return false }

    @Override
    protected boolean isFillSupported() { return false }

    // google exports empty rows as well
    @Override
    protected int getExpectedAllRowsSize() { return 39665 }

    @Override
    protected int getExpectedAllCellSize() { return 80130 }

    /**
     * Tries to open the file in the browser. Only works locally on Mac at the moment. Ignored otherwise.
     * Main purpose of this method is to quickly open the generated file for manual review.
     * @param uri uri to be opened
     */
    private static void browse(String uri) {
        try {
            if (uri && Desktop.desktopSupported && Desktop.desktop.isSupported(Desktop.Action.BROWSE)) {
                Desktop.desktop.browse(new URI(uri))
                Thread.sleep(10000)
            }
        } catch (ignored) {
            // CI
        }
    }

    @SuppressWarnings([
            'UnnecessaryGetter',
            'UnnecessarySetter',
    ])
    private static GoogleClientSecrets buildGoogleSecrets() {
        InputStream is = GoogleBuilderSpec.getResourceAsStream(CREDENTIALS_FILE_PATH)
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(is))

        secrets.getInstalled().setClientSecret(System.getenv('DSL_SPREADSHEET_BUILDER_GOOGLE_CLIENT_SECRET'))

        return secrets
    }

    @SuppressWarnings([
            'UnnecessaryGetter',
            'UnnecessarySetter',
    ])
    private static HttpRequestInitializer buildCredentials(GoogleClientSecrets secrets) {
        final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport()
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                transport, JSON_FACTORY, secrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType('offline')
                .build()
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build()
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize('user')
    }

}
