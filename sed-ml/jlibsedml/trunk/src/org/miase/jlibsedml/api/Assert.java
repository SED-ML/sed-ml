package org.miase.jlibsedml.api;
/*
 *    Copyright 2009 Richard Adams

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
/*
 * Utility class, package scoped.
 */
class Assert {
	// prevent subclassing
	private Assert (){}
	/*
	 * Checks any argument list for null
	 */
	static void checkNoNullArgs (Object ... args) {
		for (Object o : args) {
			if (o == null){
				throw new IllegalArgumentException();
			}
		}
	}

}
