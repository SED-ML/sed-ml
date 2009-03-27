package org.miase.jlibsedml.api;

/**
 * Encapsulates a model referenced in the sedml file
 * @author Richard Adams
 *
 */
public interface IModelContent {
  /**
   * A name for the model; typically this will be used to identify the model in the archive
   * This method should return the same name as is specified in 'source' field of the sedml model definition.
   * @return A non-null, non-empty string
   */
  String getName();
  
  /**
   * Gets the contents of a model as a String
   * @return A non-null, non-empty string
   */
  String getContents();
}
