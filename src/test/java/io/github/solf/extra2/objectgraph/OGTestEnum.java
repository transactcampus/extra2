/**
 * Copyright Sergey Olefir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.solf.extra2.objectgraph;

import static io.github.solf.extra2.util.NullUtil.nn;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.github.solf.extra2.objectgraph.ObjectGraphUtil;

/**
 * Enum for testing {@link ObjectGraphUtil} functionality
 *
 * @author Sergey Olefir
 */
@NonNullByDefault
/*package*/ enum OGTestEnum
{
	VAL1,
	VAL2,
	;
	
	/**
	 * lower case name
	 */
	public final String lowerCaseName;
	
	/**
	 * Constructor.
	 */
	private OGTestEnum()
	{
		lowerCaseName = nn(name().toLowerCase());
	}
}
