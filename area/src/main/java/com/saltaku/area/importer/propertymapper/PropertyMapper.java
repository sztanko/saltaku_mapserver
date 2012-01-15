package com.saltaku.area.importer.propertymapper;

import java.util.Collection;

import org.opengis.feature.Property;
@Deprecated
public interface PropertyMapper {
  public AreaFeatures getFeatures(Collection<Property> props);
}
