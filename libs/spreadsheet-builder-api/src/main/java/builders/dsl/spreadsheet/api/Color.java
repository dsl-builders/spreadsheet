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
package builders.dsl.spreadsheet.api;

import java.util.Arrays;

public final class Color {

    //CHECKSTYLE:OFF
    public static final Color aliceBlue = new Color("#F0F8FF");
    public static final Color antiqueWhite = new Color("#FAEBD7");
    public static final Color aqua = new Color("#00FFFF");
    public static final Color aquamarine = new Color("#7FFFD4");
    public static final Color azure = new Color("#F0FFFF");
    public static final Color beige = new Color("#F5F5DC");
    public static final Color bisque = new Color("#FFE4C4");
    public static final Color black = new Color("#000000");
    public static final Color blanchedAlmond = new Color("#FFEBCD");
    public static final Color blue = new Color("#0000FF");
    public static final Color blueViolet = new Color("#8A2BE2");
    public static final Color brown = new Color("#A52A2A");
    public static final Color burlyWood = new Color("#DEB887");
    public static final Color cadetBlue = new Color("#5F9EA0");
    public static final Color chartreuse = new Color("#7FFF00");
    public static final Color chocolate = new Color("#D2691E");
    public static final Color coral = new Color("#FF7F50");
    public static final Color cornflowerBlue = new Color("#6495ED");
    public static final Color cornsilk = new Color("#FFF8DC");
    public static final Color crimson = new Color("#DC143C");
    public static final Color cyan = new Color("#00FFFF");
    public static final Color darkBlue = new Color("#00008B");
    public static final Color darkCyan = new Color("#008B8B");
    public static final Color darkGoldenRod = new Color("#B8860B");
    public static final Color darkGray = new Color("#A9A9A9");
    public static final Color darkGreen = new Color("#006400");
    public static final Color darkKhaki = new Color("#BDB76B");
    public static final Color darkMagenta = new Color("#8B008B");
    public static final Color darkOliveGreen = new Color("#556B2F");
    public static final Color darkOrange = new Color("#FF8C00");
    public static final Color darkOrchid = new Color("#9932CC");
    public static final Color darkRed = new Color("#8B0000");
    public static final Color darkSalmon = new Color("#E9967A");
    public static final Color darkSeaGreen = new Color("#8FBC8F");
    public static final Color darkSlateBlue = new Color("#483D8B");
    public static final Color darkSlateGray = new Color("#2F4F4F");
    public static final Color darkTurquoise = new Color("#00CED1");
    public static final Color darkViolet = new Color("#9400D3");
    public static final Color deepPink = new Color("#FF1493");
    public static final Color deepSkyBlue = new Color("#00BFFF");
    public static final Color dimGray = new Color("#696969");
    public static final Color dodgerBlue = new Color("#1E90FF");
    public static final Color fireBrick = new Color("#B22222");
    public static final Color floralWhite = new Color("#FFFAF0");
    public static final Color forestGreen = new Color("#228B22");
    public static final Color fuchsia = new Color("#FF00FF");
    public static final Color gainsboro = new Color("#DCDCDC");
    public static final Color ghostWhite = new Color("#F8F8FF");
    public static final Color gold = new Color("#FFD700");
    public static final Color goldenRod = new Color("#DAA520");
    public static final Color gray = new Color("#808080");
    public static final Color green = new Color("#008000");
    public static final Color greenYellow = new Color("#ADFF2F");
    public static final Color honeyDew = new Color("#F0FFF0");
    public static final Color hotPink = new Color("#FF69B4");
    public static final Color indianRed  = new Color("#CD5C5C");
    public static final Color indigo  = new Color("#4B0082");
    public static final Color ivory = new Color("#FFFFF0");
    public static final Color khaki = new Color("#F0E68C");
    public static final Color lavender = new Color("#E6E6FA");
    public static final Color lavenderBlush = new Color("#FFF0F5");
    public static final Color lawnGreen = new Color("#7CFC00");
    public static final Color lemonChiffon = new Color("#FFFACD");
    public static final Color lightBlue = new Color("#ADD8E6");
    public static final Color lightCoral = new Color("#F08080");
    public static final Color lightCyan = new Color("#E0FFFF");
    public static final Color lightGoldenRodYellow = new Color("#FAFAD2");
    public static final Color lightGray = new Color("#D3D3D3");
    public static final Color lightGreen = new Color("#90EE90");
    public static final Color lightPink = new Color("#FFB6C1");
    public static final Color lightSalmon = new Color("#FFA07A");
    public static final Color lightSeaGreen = new Color("#20B2AA");
    public static final Color lightSkyBlue = new Color("#87CEFA");
    public static final Color lightSlateGray = new Color("#778899");
    public static final Color lightSteelBlue = new Color("#B0C4DE");
    public static final Color lightYellow = new Color("#FFFFE0");
    public static final Color lime = new Color("#00FF00");
    public static final Color limeGreen = new Color("#32CD32");
    public static final Color linen = new Color("#FAF0E6");
    public static final Color magenta = new Color("#FF00FF");
    public static final Color maroon = new Color("#800000");
    public static final Color mediumAquaMarine = new Color("#66CDAA");
    public static final Color mediumBlue = new Color("#0000CD");
    public static final Color mediumOrchid = new Color("#BA55D3");
    public static final Color mediumPurple = new Color("#9370DB");
    public static final Color mediumSeaGreen = new Color("#3CB371");
    public static final Color mediumSlateBlue = new Color("#7B68EE");
    public static final Color mediumSpringGreen = new Color("#00FA9A");
    public static final Color mediumTurquoise = new Color("#48D1CC");
    public static final Color mediumVioletRed = new Color("#C71585");
    public static final Color midnightBlue = new Color("#191970");
    public static final Color mintCream = new Color("#F5FFFA");
    public static final Color mistyRose = new Color("#FFE4E1");
    public static final Color moccasin = new Color("#FFE4B5");
    public static final Color navajoWhite = new Color("#FFDEAD");
    public static final Color navy = new Color("#000080");
    public static final Color oldLace = new Color("#FDF5E6");
    public static final Color olive = new Color("#808000");
    public static final Color oliveDrab = new Color("#6B8E23");
    public static final Color orange = new Color("#FFA500");
    public static final Color orangeRed = new Color("#FF4500");
    public static final Color orchid = new Color("#DA70D6");
    public static final Color paleGoldenRod = new Color("#EEE8AA");
    public static final Color paleGreen = new Color("#98FB98");
    public static final Color paleTurquoise = new Color("#AFEEEE");
    public static final Color paleVioletRed = new Color("#DB7093");
    public static final Color papayaWhip = new Color("#FFEFD5");
    public static final Color peachPuff = new Color("#FFDAB9");
    public static final Color peru = new Color("#CD853F");
    public static final Color pink = new Color("#FFC0CB");
    public static final Color plum = new Color("#DDA0DD");
    public static final Color powderBlue = new Color("#B0E0E6");
    public static final Color purple = new Color("#800080");
    public static final Color rebeccaPurple = new Color("#663399");
    public static final Color red = new Color("#FF0000");
    public static final Color rosyBrown = new Color("#BC8F8F");
    public static final Color royalBlue = new Color("#4169E1");
    public static final Color saddleBrown = new Color("#8B4513");
    public static final Color salmon = new Color("#FA8072");
    public static final Color sandyBrown = new Color("#F4A460");
    public static final Color seaGreen = new Color("#2E8B57");
    public static final Color seaShell = new Color("#FFF5EE");
    public static final Color sienna = new Color("#A0522D");
    public static final Color silver = new Color("#C0C0C0");
    public static final Color skyBlue = new Color("#87CEEB");
    public static final Color slateBlue = new Color("#6A5ACD");
    public static final Color slateGray = new Color("#708090");
    public static final Color snow = new Color("#FFFAFA");
    public static final Color springGreen = new Color("#00FF7F");
    public static final Color steelBlue = new Color("#4682B4");
    public static final Color tan = new Color("#D2B48C");
    public static final Color teal = new Color("#008080");
    public static final Color thistle = new Color("#D8BFD8");
    public static final Color tomato = new Color("#FF6347");
    public static final Color turquoise = new Color("#40E0D0");
    public static final Color violet = new Color("#EE82EE");
    public static final Color wheat = new Color("#F5DEB3");
    public static final Color white = new Color("#FFFFFF");
    public static final Color whiteSmoke = new Color("#F5F5F5");
    public static final Color yellow = new Color("#FFFF00");
    public static final Color yellowGreen = new Color("#9ACD32");
    //CHECKSTYLE:ON

    private final String hex;

    public Color(String hex) {
        if (!hex.matches("#[\\dA-Fa-f]{6}")) {
            throw new IllegalArgumentException("Wrong format for color: " + hex);
        }
        this.hex = hex.toUpperCase();
    }

    public Color(byte[] rgb) {
        if (rgb.length != 3) {
            throw new IllegalArgumentException("Wrong number of parts in: " + Arrays.toString(rgb));
        }
        this.hex = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Color color = (Color) o;

        return !(hex != null ? !hex.equals(color.hex) : color.hex != null);

    }

    @Override
    public int hashCode() {
        return hex != null ? hex.hashCode() : 0;
    }

    public String getHex() {
        return hex;
    }

    @Override
    public String toString() {
        return "Color[" + getHex() + "]";
    }
}
