package net.sf.anathema.character.generic.impl.magic.persistence;

import java.util.List;

import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.lib.collection.MultiEntryMap;
import net.sf.anathema.lib.util.IIdentificate;
import net.sf.anathema.lib.util.Identificate;

public class ThaumaturgyCache implements IThaumaturgyCache {
	MultiEntryMap<IIdentificate, IThaumaturgy> thaumaturgySets = new MultiEntryMap<IIdentificate, IThaumaturgy>();
	  
	@Override
	public IThaumaturgy[] getThaumaturgy(IIdentificate type) {
	  type = new Identificate(type.getId());
	  List<IThaumaturgy> thaumaturgyList = thaumaturgySets.get(type);
	  return thaumaturgyList.toArray(new IThaumaturgy[thaumaturgyList.size()]);
	}

	public void addThaumaturgy(IThaumaturgy thaumaturgy) {
	  IIdentificate type = new Identificate(thaumaturgy.getArt().getId());
	  thaumaturgySets.replace(type, thaumaturgy, thaumaturgy);
	}

	@Override
	public IIdentificate[] getArts() {
	  return thaumaturgySets.keySet().toArray(new IIdentificate[0]);
	}
}
