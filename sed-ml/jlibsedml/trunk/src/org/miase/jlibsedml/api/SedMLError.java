package org.miase.jlibsedml.api;

/**
 * Class to hold parsing errors. Equality is based on line number, message string and severity
 * @author Richard Adams
 *
 */
public class SedMLError {
	
	private int severity, lineNo;
	private String message;
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
