package com.saltaku.area.importer.propertymapper;

import java.util.Collection;

import org.opengis.feature.Property;

public interface PropertyMapper {
  public AreaFeatures getFeatures(Collection<Property> props);
}
