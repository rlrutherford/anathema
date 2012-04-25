package net.sf.anathema.character.model;

import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.lib.util.IIdentificate;

public interface IThaumaturgyConfiguration {
	IIdentificate[] getArts();
	
	IThaumaturgy[] getThaumaturgyByArt(IIdentificate art);
	
	IThaumaturgy[] getAllLearnedThaumaturgy();
	
	boolean isThaumaturgyAllowed(IThaumaturgy thaumaturgy);
	
	void addThaumaturgy(IThaumaturgy[] thaumaturgy);
	
	void removeThaumaturgy(IThaumaturgy[] thaumaturgy);
	
	boolean isLearned(IThaumaturgy thaumaturgy);
	
	boolean isLearnedOnCreation(IThaumaturgy thaumaturgy);
	
	void addMagicLearnListener(IMagicLearnListener<IThaumaturgy> listener);
}
