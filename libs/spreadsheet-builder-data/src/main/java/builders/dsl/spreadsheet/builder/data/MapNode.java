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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class MapNode implements Node {

    private final Map<String, Object> map = new LinkedHashMap<>();

    public void add(String key, Node newItem) {
        add(key, newItem.getContent());
    }

    @SuppressWarnings("unchecked")
    public void add(String key, Object newItem) {
        List<Object> list = (List<Object>) map.computeIfAbsent(key, k -> new ArrayList<>());
        list.add(newItem);
    }

    public void set(String key, Node value) {
        set(key, value.getContent());
    }

    public void set(String key, Object value) {
        map.put(key, value);
    }

    @Override
    public Map<String, Object> getContent() {
        return map;
    }
}
