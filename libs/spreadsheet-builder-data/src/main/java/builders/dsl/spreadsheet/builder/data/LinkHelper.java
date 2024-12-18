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
package builders.dsl.spreadsheet.builder.data;

import builders.dsl.spreadsheet.builder.api.LinkDefinition;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


class LinkHelper implements LinkDefinition {

    LinkHelper(CellNode cell) {
        this.cell = cell;
    }

    @Override
    public CellNode name(String name) {
        cell.node.set("link", name);
        return cell;
    }

    @Override
    public CellNode email(String email) {
        cell.node.set("link", "mailto:" + email);
        return cell;
    }

    @Override
    public CellNode email(final Map<String, ?> parameters, String email) {
        try {
            List<String> params = new ArrayList<String>();
            for (Map.Entry<String, ?> parameter: parameters.entrySet()) {
                if (parameter.getValue() != null) {
                    params.add(URLEncoder.encode(parameter.getKey(), "UTF-8") + "=" + URLEncoder.encode(parameter.getValue().toString(), "UTF-8"));
                } else {
                    params.add(URLEncoder.encode(parameter.getKey(), "UTF-8"));
                }
            }
            cell.node.set("link", "mailto:" + email + "?" + String.join("&", params));
            return cell;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CellNode url(String url) {
        cell.node.set("link", url);
        return cell;
    }

    @Override
    public CellNode file(String path) {
        cell.node.set("link", "file:" + path);
        return cell;
    }

    private final CellNode cell;
}
