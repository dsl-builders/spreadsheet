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
package builders.dsl.spreadsheet.impl;

import builders.dsl.spreadsheet.builder.api.CellDefinition;
import builders.dsl.spreadsheet.builder.api.Resolvable;

/**
 * Pending link is a link which needs to be resolved at the end of the build when all the named references are known.
 */
public abstract class AbstractPendingLink implements Resolvable {

    protected AbstractPendingLink(CellDefinition cell, final String name) {
        if (!name.equals(Utils.fixName(name))) {
            throw new IllegalArgumentException("Cannot call cell \'" + name + "\' as this is invalid identifier in Excel. Suggestion: " + Utils.fixName(name));
        }

        this.cell = cell;
        this.name = name;
    }

    public final CellDefinition getCell() {
        return cell;
    }

    public final String getName() {
        return name;
    }

    private final CellDefinition cell;
    private final String name;
}
