package net.sf.anathema.character.alchemical.caste;

import net.sf.anathema.character.generic.caste.ICasteType;

public enum AlchemicalCaste implements ICasteType {

  Orichalcum, Soulsteel, Starmetal, Moonsilver, Jade, Adamant;

  public String getId() {
    return name();
  }

  @Override
  public String toString() {
    return name();
  }
}