/*
 * Copyright (c) 2002-2015 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.ndp.runtime.internal;

import org.junit.Test;

import org.neo4j.kernel.api.exceptions.Status;
import org.neo4j.logging.AssertableLogProvider;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.neo4j.logging.AssertableLogProvider.inLog;

public class ErrorTranslatorTest
{
    @Test
    public void shouldReportUnknownErrors() throws Throwable
    {
        // Given
        AssertableLogProvider provider = new AssertableLogProvider();
        ErrorTranslator translator =
                new ErrorTranslator( provider.getLog( "userlog" ) );

        Throwable cause = new Throwable( "This is not an error we know how to handle." );

        // When
        Neo4jError translated = translator.translate( cause );

        // Then
        assertThat( translated.status(), equalTo( (Status) Status.General.UnknownFailure ) );
        provider.assertExactly(
                inLog( "userlog" )
                    .error( both(containsString( "START OF REPORT" ))
                            .and(containsString( "END OF REPORT" )) ));
    }
}