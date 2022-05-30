/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2022, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/

package schemacrawler.integration.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static schemacrawler.test.utility.FileHasContent.classpathResource;
import static schemacrawler.test.utility.FileHasContent.hasSameContentAs;
import static schemacrawler.test.utility.FileHasContent.outputOf;

import java.nio.file.Path;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import schemacrawler.test.utility.BaseSqliteTest;
import schemacrawler.test.utility.TestContext;
import schemacrawler.test.utility.TestContextParameterResolver;
import schemacrawler.test.utility.TestLoggingExtension;
import schemacrawler.tools.command.text.schema.options.TextOutputFormat;
import schemacrawler.tools.sqlite.EmbeddedSQLiteWrapper;
import us.fatehi.utility.IOUtility;

@ExtendWith(TestLoggingExtension.class)
@ExtendWith(TestContextParameterResolver.class)
public class EmbeddedSQLiteWrapperTest extends BaseSqliteTest {

  @Test
  public void djangoExcluded(final TestContext testContext) throws Exception {
    weakAssociations(testContext, "/django_schema.sql");
  }

  private void weakAssociations(final TestContext testContext, final String databaseSqlResource)
      throws Exception {

    final String currentMethodFullName = testContext.testMethodFullName();

    // Create database from script, on disk
    final Path dbFile = IOUtility.createTempFilePath("test_sqlite_db", "");
    final DataSource dataSource =
        createDatabaseFromScript(createDataSourceFromFile(dbFile), databaseSqlResource);

    final EmbeddedSQLiteWrapper sqLiteDatabaseLoader = new EmbeddedSQLiteWrapper();
    sqLiteDatabaseLoader.setDatabasePath(dbFile);
    final Path diagram =
        sqLiteDatabaseLoader.executeForOutput("Test Diagram Title", TextOutputFormat.text);

    assertThat(outputOf(diagram), hasSameContentAs(classpathResource(currentMethodFullName)));
  }
}