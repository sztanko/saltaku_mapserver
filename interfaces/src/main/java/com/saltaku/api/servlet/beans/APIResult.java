package com.saltaku.api.servlet.beans;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class APIResult<T> {
public boolean success=true;
public String errorMessage=null;
public long requestTime;
public Map<String,String[]> params;
public Date requestStart=new Date();
public List<String> reference;
public T response=null;
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
	result = prime * result + ((params == null) ? 0 : params.hashCode());
	result = prime * result + ((reference == null) ? 0 : reference.hashCode());
	result = prime * result + ((requestStart == null) ? 0 : requestStart.hashCode());
	result = prime * result + (int) (requestTime ^ (requestTime >>> 32));
	result = prime * result + ((response == null) ? 0 : response.hashCode());
	result = prime * result + (success ? 1231 : 1237);
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
	APIResult other = (APIResult) obj;
	if (errorMessage == null) {
		if (other.errorMessage != null)
			return false;
	} else if (!errorMessage.equals(other.errorMessage))
		return false;
	if (params == null) {
		if (other.params != null)
			return false;
	} else if (!params.equals(other.params))
		return false;
	if (reference == null) {
		if (other.reference != null)
			return false;
	} else if (!reference.equals(other.reference))
		return false;
	if (requestStart == null) {
		if (other.requestStart != null)
			return false;
	} else if (!requestStart.equals(other.requestStart))
		return false;
	if (requestTime != other.requestTime)
		return false;
	if (response == null) {
		if (other.response != null)
			return false;
	} else if (!response.equals(other.response))
		return false;
	if (success != other.success)
		return false;
	return true;
}
}
