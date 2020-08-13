/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2020, Sualeh Fatehi <sualeh@hotmail.com>.
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
package us.fatehi.utility.ioresource;


import static java.util.Objects.requireNonNull;

import java.io.FilterReader;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputResourceUtility
{

  public static Writer wrapWriter(final String description,
                                  final Writer writer,
                                  final boolean shouldClose)
  {
    requireNonNull(writer, "No writer provided");
    return new FilterWriter(writer)
    {
      @Override
      public void close()
        throws IOException
      {
        if (shouldClose)
        {
          super.close();
        } else {
          super.flush();
        }
      }

      @Override
      public String toString()
      {
        return description;
      }
    };
  }

  public static Reader wrapReader(final String description,
                                  final Reader reader,
                                  final boolean shouldClose)
  {
    requireNonNull(reader, "No reader provided");
    return new FilterReader(reader)
    {
      @Override
      public void close()
        throws IOException
      {
        if (shouldClose)
        {
          super.close();
        }
      }

      @Override
      public String toString()
      {
        return description;
      }
    };
  }

  /**
   * Creates an input resource from the classpath, or from the file system. If
   * neither are found, returns an empty input resource.
   *
   * @param inputResourceName
   *   Name of input resource.
   * @return Input resource
   */
  public static InputResource createInputResource(final String inputResourceName)
  {
    InputResource inputResource = null;
    try
    {
      final Path filePath = Paths.get(inputResourceName);
      inputResource = new FileInputResource(filePath);
    }
    catch (final Exception e)
    {
      // No-op
    }
    try
    {
      if (inputResource == null)
      {
        inputResource = new ClasspathInputResource(inputResourceName);
      }
    }
    catch (final Exception e)
    {
      // No-op
    }
    if (inputResource == null)
    {
      inputResource = new EmptyInputResource();
    }
    return inputResource;
  }

  private InputResourceUtility()
  {
    // Prevent instantiation
  }

}
