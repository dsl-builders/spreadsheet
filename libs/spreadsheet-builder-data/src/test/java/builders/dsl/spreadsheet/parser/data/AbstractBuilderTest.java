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
package builders.dsl.spreadsheet.parser.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import builders.dsl.spreadsheet.api.Cell;
import builders.dsl.spreadsheet.api.DataRow;
import builders.dsl.spreadsheet.api.Keywords;
import builders.dsl.spreadsheet.api.Row;
import builders.dsl.spreadsheet.builder.api.SpreadsheetBuilder;
import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteriaResult;
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static builders.dsl.spreadsheet.api.Color.*;
import static builders.dsl.spreadsheet.api.Keywords.*;

public abstract class AbstractBuilderTest {

    private static final Date EXPECTED_DATE = Date.from(ZonedDateTime.parse("2017-07-29T03:07:16.404Z", DateTimeFormatter.ISO_DATE_TIME).toInstant());

    @Rule public TemporaryFolder tmp = new TemporaryFolder();

    @Test public void testTrivial() throws IOException {
        File excel = tmp.newFile();
        SpreadsheetBuilder builder = PoiSpreadsheetBuilder.create(excel);
        try {
            build(builder, "trivial");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpreadsheetCriteria matcher = PoiSpreadsheetCriteria.FACTORY.forFile(excel);
        SpreadsheetCriteriaResult allCells = matcher.all();

        assertEquals(1, allCells.getSheets().size());
        assertEquals(1, allCells.getRows().size());
        assertEquals(2, allCells.getCells().size());
    }

    @Test public void testDate() throws IOException, InterruptedException {
        File excel = tmp.newFile(System.currentTimeMillis() + ".xlsx");
        SpreadsheetBuilder builder = PoiSpreadsheetBuilder.create(excel);
        try {
            build(builder, "date");
        } catch (Exception e) {
            e.printStackTrace();
        }

        open(excel);

        SpreadsheetCriteria matcher = PoiSpreadsheetCriteria.FACTORY.forFile(excel);

        SpreadsheetCriteriaResult someCells = matcher.query(w ->
            w.sheet(s ->
                s.row(r ->
                    r.cell(c ->
                        c.date(EXPECTED_DATE)
                    )
                )
            )
        );

        assertEquals(1, someCells.getCells().size());
    }

    @Test public void testTextContent() throws IOException, InterruptedException {
        File excel = tmp.newFile(System.currentTimeMillis() + ".xlsx");
        SpreadsheetBuilder builder = PoiSpreadsheetBuilder.create(excel);
        try {
            build(builder, "texts");
        } catch (Exception e) {
            e.printStackTrace();
        }

        open(excel);

        SpreadsheetCriteria matcher = PoiSpreadsheetCriteria.FACTORY.forFile(excel);

        SpreadsheetCriteriaResult someCells = matcher.query(w ->
            w.sheet(s ->
                s.row(r ->
                    r.cell(c ->
                        c.string("Hello World")
                    )
                )
            )
        );

        assertEquals(1, someCells.getCells().size());
    }

    @Test public void testBuilderFull() throws IOException, InterruptedException {
        File excel = tmp.newFile(System.currentTimeMillis() + ".xlsx");
        SpreadsheetBuilder builder = PoiSpreadsheetBuilder.create(excel);
        try {
            build(builder, "sheet");
        } catch (Exception e) {
            e.printStackTrace();
        }

        open(excel);

        SpreadsheetCriteria matcher = PoiSpreadsheetCriteria.FACTORY.forFile(excel);

        SpreadsheetCriteriaResult allCells = matcher.all();

        assertEquals(21, allCells.getSheets().size());
        assertEquals(47, allCells.getRows().size());
        assertEquals(96, allCells.getCells().size());

        SpreadsheetCriteriaResult sampleCells = matcher.query(w -> w.sheet("Sample"));

        assertEquals(1, sampleCells.getSheets().size());
        assertEquals(1, sampleCells.getRows().size());
        assertEquals(2, sampleCells.getCells().size());

        SpreadsheetCriteriaResult rowCells = matcher.query(w ->
            w.sheet("many rows", s ->
                s.row(1)
            )
        );

        assertEquals(4, rowCells.getCells().size());
        assertEquals(1, rowCells.getSheets().size());
        assertEquals(1, rowCells.getRows().size());

        Row manyRowsHeader = matcher.query(w ->
            w.sheet("many rows", s ->
                s.row(1)
            )
        ).getRow();

        assertNotNull(manyRowsHeader);

        Row manyRowsDataRow = matcher.query(w -> w.sheet("many rows", s -> s.row(2))).getRow();

        DataRow dataRow = DataRow.create(manyRowsDataRow, manyRowsHeader);

        assertNotNull(dataRow.get("One"));
        assertEquals("1", dataRow.get("One").getValue());


        Map<String, Integer> dataRowMapping = new HashMap<>();
        dataRowMapping.put("primo", 1);


        DataRow dataRowFromMapping = DataRow.create(dataRowMapping, manyRowsDataRow);
        assertNotNull(dataRowFromMapping.get("primo"));
        assertEquals("1", dataRowFromMapping.get("primo").getValue());


        SpreadsheetCriteriaResult someCells = matcher.query(w ->
            w.sheet(s ->
                s.row(r ->
                    r.cell(c ->
                        c.date(EXPECTED_DATE)
                    )
                )
            )
        );

        assertEquals(1, someCells.getCells().size());


        SpreadsheetCriteriaResult commentedCells = matcher.query(w ->
            w.sheet(s ->
                s.row(r ->
                    r.cell(c ->
                        c.comment("This is a date!")
                    )
                )
            )
        );

        assertEquals(1, commentedCells.getCells().size());


        SpreadsheetCriteriaResult namedCells = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.name("_Cell10");
                    });
                });
            });
        });

        assertEquals(1, namedCells.getCells().size());


        SpreadsheetCriteriaResult dateCells = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.style(st -> {
                            st.format("d.m.y");
                        });
                    });
                });
            });
        });

        assertEquals(1, dateCells.getCells().size());

        SpreadsheetCriteriaResult filledCells = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.style(st -> {
                            st.fill(fineDots);
                        });
                    });
                });
            });
        });

        assertEquals(1, filledCells.getCells().size());

        SpreadsheetCriteriaResult magentaCells = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.style(st -> {
                            st.foreground(aquamarine);
                        });
                    });
                });
            });
        });

        assertEquals(2, magentaCells.getCells().size());

        SpreadsheetCriteriaResult redOnes = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.style(st -> {
                            st.font(f -> {
                                f.color(red);
                            });
                        });
                    });
                });
            });
        });

        assertEquals(7, redOnes.getCells().size());
        assertEquals(5, redOnes.getRows().size());

        SpreadsheetCriteriaResult boldOnes = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.style(st -> {
                            st.font(f -> {
                                f.style(bold);
                            });
                        });
                    });
                });
            });
        });

        assertEquals(11, boldOnes.getCells().size());

        SpreadsheetCriteriaResult bigOnes = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.style(st -> {
                            st.font(f -> {
                                f.size(22);
                            });
                        });
                    });
                });
            });
        });

        assertEquals(1, bigOnes.getCells().size());

        SpreadsheetCriteriaResult bordered = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.style(st -> {
                            st.border(top, b -> {
                                b.style(thin);
                            });
                        });
                    });
                });
            });
        });

        assertEquals(9, bordered.getCells().size());

        SpreadsheetCriteriaResult combined = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.value("Bold Red 22");
                        c.style(st -> {
                            st.font(f -> {
                                f.color(red);
                            });
                        });
                    });
                });
            });
        });

        assertEquals(1, combined.getCells().size());

        SpreadsheetCriteriaResult conjunction = matcher.query(w -> {
            w.sheet(s -> {
                s.row(r -> {
                    r.or(o -> {
                        o.cell(c -> {
                            c.value("Bold Red 22");
                        });
                        o.cell(c -> {
                            c.value("A");
                        });
                    });
                });
            });
        });

        assertEquals(3, conjunction.getCells().size());

        SpreadsheetCriteriaResult traversal = matcher.query(w -> {
            w.sheet("Traversal", s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.value("E");
                    });
                });
            });
        });

        assertEquals(1, traversal.getCells().size());

        Cell cellE = traversal.iterator().next();

        assertEquals("Traversal", cellE.getRow().getSheet().getName());
        assertNotNull(cellE.getRow().getSheet().getPrevious());
        assertEquals("Formula", cellE.getRow().getSheet().getPrevious().getName());
        assertNotNull(cellE.getRow().getSheet().getNext());
        assertEquals("Border", cellE.getRow().getSheet().getNext().getName());
        assertEquals(2, cellE.getRow().getNumber());
        assertNotNull(cellE.getRow().getAbove());
        assertEquals(1, cellE.getRow().getAbove().getNumber());
        assertNotNull(cellE.getRow().getBelow());
        assertEquals(3, cellE.getRow().getBelow().getNumber());
        assertEquals(2, cellE.getColspan());
        assertNotNull(cellE.getAboveLeft());
        assertEquals("A", cellE.getAboveLeft().getValue());
        assertNotNull(cellE.getAbove());
        assertEquals("B", cellE.getAbove().getValue());
        assertNotNull(cellE.getAboveRight());
        assertEquals("C", cellE.getAboveRight().getValue());
        assertNotNull(cellE.getLeft());
        assertEquals("D", cellE.getLeft().getValue());
        assertNotNull(cellE.getRight());
        assertEquals("F", cellE.getRight().getValue());
        assertNotNull(cellE.getBelowLeft());
        assertEquals("G", cellE.getBelowLeft().getValue());
        assertNotNull(cellE.getBelowRight());
        assertEquals("I", cellE.getBelowRight().getValue());
        assertNotNull(cellE.getBelow());
        assertEquals("H", cellE.getBelow().getValue());
        assertEquals("J", cellE.getBelow().getBelow().getValue());

        SpreadsheetCriteriaResult zeroCells = matcher.query(w -> {
            w.sheet("Zero", s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.value(0);
                    });
                });
            });
        });

        assertEquals(1, zeroCells.getCells().size());
        assertEquals(0d, zeroCells.getCell().getValue());

        Cell noneCell = matcher.find(w -> {
            w.sheet("Styles", s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.value("NONE");
                    });
                });
            });
        });

        Cell redCell = matcher.find(w -> {
            w.sheet("Styles", s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.value("RED");
                    });
                });
            });
        });

        Cell blueCell = matcher.find(w -> {
            w.sheet("Styles", s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.value("BLUE");
                    });
                });
            });
        });

        Cell greenCell = matcher.find(w -> {
            w.sheet("Styles", s -> {
                s.row(r -> {
                    r.cell(c -> {
                        c.value("GREEN");
                    });
                });
            });
        });

        assertNull(noneCell.getStyle().getForeground());
        assertEquals(red, redCell.getStyle().getForeground());
        assertEquals(blue, blueCell.getStyle().getForeground());
        assertEquals(green, greenCell.getStyle().getForeground());

        assertEquals(1, matcher.query(w -> {
            w.sheet(s -> {
                s.page(p -> {
                    p.paper(Keywords.Paper.A5);
                    p.orientation(Keywords.Orientation.LANDSCAPE);
                });
            });
        }).getCells().size());
        assertEquals(19, matcher.query(w -> {
            w.sheet(s -> {
                s.state(visible);
            });
        }).getSheets().size());
        assertEquals(1, matcher.query(w -> {
            w.sheet("Hidden", s -> {
                s.state(hidden);
            });
        }).getSheets().size());
        assertEquals(1, matcher.query(w -> {
            w.sheet("Very hidden", s -> {
                s.state(veryHidden);
            });
        }).getSheets().size());
    }

    protected abstract void build(SpreadsheetBuilder builder, String sheet) throws Exception;

    private void open(File excel) throws IOException, InterruptedException {
        if (System.getenv("CI") == null && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(excel);
            Thread.sleep(3000);
        }
    }

}
