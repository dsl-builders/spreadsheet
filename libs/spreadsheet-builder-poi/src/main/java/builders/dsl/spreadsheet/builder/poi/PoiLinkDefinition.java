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
package builders.dsl.spreadsheet.builder.poi;

import builders.dsl.spreadsheet.builder.api.LinkDefinition;
import builders.dsl.spreadsheet.impl.Utils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


class PoiLinkDefinition implements LinkDefinition {

    PoiLinkDefinition(PoiWorkbookDefinition workbook, PoiCellDefinition cell) {
        this.cell = cell;
        this.workbook = workbook;
    }

    @Override
    public PoiCellDefinition name(String name) {
        workbook.addPendingLink(name, cell);
        return cell;
    }

    @Override
    public PoiCellDefinition email(String email) {
        Workbook workbook = cell.getRow().getSheet().getWorkbook().getWorkbook();
        Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.EMAIL);
        link.setAddress("mailto:" + email);
        cell.getCell().setHyperlink(link);
        return cell;
    }

    @Override
    public PoiCellDefinition email(final Map<String, ?> parameters, String email) {
        try {
            Workbook workbook = cell.getRow().getSheet().getWorkbook().getWorkbook();
            Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.EMAIL);
            List<String> params = new ArrayList<String>();
            for (Map.Entry<String, ?> parameter: parameters.entrySet()) {
                if (parameter.getValue() != null) {
                    params.add(URLEncoder.encode(parameter.getKey(), "UTF-8") + "=" + URLEncoder.encode(parameter.getValue().toString(), "UTF-8"));
                } else {
                    params.add(URLEncoder.encode(parameter.getKey(), "UTF-8"));
                }
            }
            link.setAddress("mailto:" + email + "?" + Utils.join(params, "&"));
            cell.getCell().setHyperlink(link);
            return cell;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PoiCellDefinition url(String url) {
        Workbook workbook = cell.getRow().getSheet().getWorkbook().getWorkbook();
        Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
        link.setAddress(url);
        cell.getCell().setHyperlink(link);
        return cell;
    }

    @Override
    public PoiCellDefinition file(String path) {
        Workbook workbook = cell.getRow().getSheet().getWorkbook().getWorkbook();
        Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.FILE);
        link.setAddress(path);
        cell.getCell().setHyperlink(link);
        return cell;
    }

    private final PoiCellDefinition cell;
    private final PoiWorkbookDefinition workbook;
}
