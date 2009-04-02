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
/**
 * Class to hold parsing errors. Equality is based on line number, message string and severity
 * @author Richard Adams
 *
 */
public class SedMLError {
	
	Object getNode() {
		return node;
	}
	void setNode(Object node) {
		this.node = node;
	}
	private int severity, lineNo;
	private String message;
	private Object node;
	SedMLError(int lineNo, String message, int severity) {
		super();
		this.lineNo = lineNo;
		this.message = message;
		this.severity = severity;
	}
	public int getSeverity() {
		return severity;
	}
	public int getLineNo() {
		return lineNo;
	}
	public String getMessage() {
		return message;
	}
	
	public String toString (){
		return message + " at " + lineNo + ",  severity:" + severity;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineNo;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + severity;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SedMLError other = (SedMLError) obj;
		if (lineNo != other.lineNo)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (severity != other.severity)
			return false;
		return true;
	}
	

}
