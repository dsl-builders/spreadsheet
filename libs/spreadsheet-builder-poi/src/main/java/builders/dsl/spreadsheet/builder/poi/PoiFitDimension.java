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
package builders.dsl.spreadsheet.builder.poi;

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.FitDimension;

class PoiFitDimension implements FitDimension {

    PoiFitDimension(PoiPageSettingsProvider pageSettingsProvider, Keywords.Fit fit) {
        this.pageSettingsProvider = pageSettingsProvider;
        this.fit = fit;
    }

    @Override
    public PoiPageSettingsProvider to(int numberOfPages) {
        if (Keywords.Fit.HEIGHT.equals(fit)) {
            pageSettingsProvider.getPrintSetup().setFitHeight((short) numberOfPages);
            return pageSettingsProvider;
        }
        if (Keywords.Fit.WIDTH.equals(fit)) {
            pageSettingsProvider.getPrintSetup().setFitWidth((short) numberOfPages);
            return pageSettingsProvider;
        }
        throw new IllegalStateException();
    }

    private final PoiPageSettingsProvider pageSettingsProvider;
    private final Keywords.Fit fit;
}
