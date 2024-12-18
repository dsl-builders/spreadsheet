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
package builders.dsl.spreadsheet.parser.data;

public class InvalidPropertyException extends RuntimeException {

    private final String path;
    private final Object value;

    public InvalidPropertyException(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public InvalidPropertyException(String message, String path, Object value) {
        super(message);
        this.path = path;
        this.value = value;
    }

    public InvalidPropertyException(String message, Throwable cause, String path, Object value) {
        super(message, cause);
        this.path = path;
        this.value = value;
    }

    public InvalidPropertyException(Throwable cause, String path, Object value) {
        super(cause);
        this.path = path;
        this.value = value;
    }

    public InvalidPropertyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String path, Object value) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.path = path;
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return super.toString() + ", path=" + path + ", value=" + value;
    }
}
