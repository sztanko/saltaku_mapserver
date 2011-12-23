package com.saltaku.area.importer.propertymapper;

public class AreaFeatures {
private int id;
private String code;
private String name;
private String englishName;


public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEnglishName() {
	return englishName;
}
public void setEnglishName(String englishName) {
	this.englishName = englishName;
}

public void setId(int id) {
	this.id = id;
}
public int getId() {
	return id;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((code == null) ? 0 : code.hashCode());
	result = prime * result + ((englishName == null) ? 0 : englishName.hashCode());
	result = prime * result + id;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	AreaFeatures other = (AreaFeatures) obj;
	if (code == null) {
		if (other.code != null)
			return false;
	} else if (!code.equals(other.code))
		return false;
	if (englishName == null) {
		if (other.englishName != null)
			return false;
	} else if (!englishName.equals(other.englishName))
		return false;
	if (id != other.id)
		return false;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	return true;
}
@Override
public String toString() {
	return "AreaFeatures [id=" + id + ", code=" + code + ", name=" + name + ", englishName=" + englishName + "]";
}
 
}
