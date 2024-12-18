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

import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.builder.api.FitDimension;
import builders.dsl.spreadsheet.builder.api.PageDefinition;

class PageNode extends AbstractNode implements PageDefinition {

    @Override
    public PageDefinition orientation(Keywords.Orientation orientation) {
        node.set("orientation", orientation);
        return this;
    }

    @Override
    public PageDefinition paper(Keywords.Paper paper) {
        node.set("paper", paper);
        return this;
    }

    @Override
    public FitDimension fit(Keywords.Fit widthOrHeight) {
        return new FitDimensionHelper(this, widthOrHeight == Keywords.Fit.WIDTH ? "width" : "height");
    }

}
