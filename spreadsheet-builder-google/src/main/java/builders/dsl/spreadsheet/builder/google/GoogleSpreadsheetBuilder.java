package builders.dsl.spreadsheet.builder.google;

import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder;
import builders.dsl.spreadsheet.builder.api.WorkbookDefinition;
import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder;
import builders.dsl.spreadsheet.google.GoogleSpreadsheets;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.drive.model.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class GoogleSpreadsheetBuilder implements SpreadsheetBuilder {

    public static class Builder {
        private final String name;
        private final GoogleSpreadsheets spreadsheets;

        private String id;
        private InputStream template;

        private Builder(String name, GoogleSpreadsheets spreadsheets) {
            this.name = name;
            this.spreadsheets = spreadsheets;
        }

        /**
         * Replace the spreadsheet of given ID instead of creating a new document.
         * @param spreadsheetID id of the document
         * @return self
         */
        public Builder id(String spreadsheetID) {
            this.id = id;
            return this;
        }

        /**
         * Create a spreadsheet from template supplied as an input stream.
         * @param template sheet template
         * @return self
         */
        public Builder template(InputStream template) {
            this.template = template;
            return this;
        }

        /**
         * Create a spreadsheet from template Google Spreadsheet
         * @param spreadsheetId sheet template id
         * @return self
         */
        public Builder template(String spreadsheetId) {
            this.template = spreadsheets.convertAndDownload(spreadsheetId);
            return this;
        }

        /**
         * Create a spreadsheet from template supplied as a file
         * @param file sheet template
         * @return self
         */
        public Builder template(java.io.File file) throws FileNotFoundException {
            this.template = new FileInputStream(file);
            return this;
        }

        public GoogleSpreadsheetBuilder build() {
            return new GoogleSpreadsheetBuilder(id, name, template, spreadsheets);
        }
    }

    public static GoogleSpreadsheetBuilder.Builder builder(String name, HttpRequestInitializer credentials) {
        return builder(name, GoogleSpreadsheets.create(credentials));
    }

    public static GoogleSpreadsheetBuilder.Builder builder(String name, GoogleSpreadsheets spreadsheets) {
        return new Builder(name, spreadsheets);
    }

    private final String name;
    private final GoogleSpreadsheets spreadsheets;
    private final InputStream template;

    private String webLink;
    private String id;

    private GoogleSpreadsheetBuilder(String id, String name, InputStream template, GoogleSpreadsheets spreadsheets) {
        this.id = id;
        this.name = name;
        this.template = template;
        this.spreadsheets = spreadsheets;
    }

    @Override
    public void build(Consumer<WorkbookDefinition> workbookDefinition) {
        File file = buildInternal(workbookDefinition);

        this.webLink = file.getWebViewLink();
        this.id = file.getId();
    }

    private File buildInternal(Consumer<WorkbookDefinition> workbookDefinition) {
        if (template == null) {
            return spreadsheets.updateAndConvert(id, name, out -> PoiSpreadsheetBuilder.create(out).build(workbookDefinition));
        }

        return spreadsheets.updateAndConvert(id, name, out -> {
            try {
                PoiSpreadsheetBuilder.create(out, template).build(workbookDefinition);
            } catch (IOException e) {
                throw new IllegalArgumentException("Problem reading file's template from " + template, e);
            }
        });
    }

    /**
     * @return the name of the newly crated Spreadsheet as shown in Google Drive
     */
    public String getName() {
        return name;
    }

    /**
     * @return the web link to the last generated spreadsheet or <code>null</code> if no file was generated yet
     */
    public String getWebLink() {
        return webLink;
    }

    /**
     * @return the id of the last generated spreadsheet or <code>null</code> if no file was generated yet
     */
    public String getId() {
        return id;
    }
}
