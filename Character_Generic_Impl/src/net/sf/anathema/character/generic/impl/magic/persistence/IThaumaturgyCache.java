package net.sf.anathema.character.generic.impl.magic.persistence;

import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.lib.resources.IExtensibleDataSet;
import net.sf.anathema.lib.util.IIdentificate;

public interface IThaumaturgyCache extends IExtensibleDataSet {
	IThaumaturgy[] getThaumaturgy(IIdentificate type);
	
	IIdentificate[] getArts();
}
