package org.miase.jlibsedml.api;

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
