package net.sf.anathema.character.impl.model;

import net.sf.anathema.character.generic.impl.magic.persistence.IThaumaturgyCache;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.model.IMagicLearnListener;
import net.sf.anathema.character.model.IThaumaturgyConfiguration;
import net.sf.anathema.lib.util.IIdentificate;

public class ThaumaturgyConfiguration implements IThaumaturgyConfiguration {

	final IThaumaturgyCache cache;
	
	public ThaumaturgyConfiguration(IThaumaturgyCache cache) {
		this.cache = cache;
	}
	
	@Override
	public boolean isLearned(IThaumaturgy thaumaturgy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IIdentificate[] getArts() {
		return cache.getArts();
	}

	@Override
	public IThaumaturgy[] getThaumaturgyByArt(IIdentificate art) {
		return cache.getThaumaturgy(art);
	}

	@Override
	public IThaumaturgy[] getAllLearnedThaumaturgy() {
		// TODO
		return new IThaumaturgy[0];
	}

	@Override
	public boolean isThaumaturgyAllowed(IThaumaturgy thaumaturgy) {
		// TODO
		return false;
	}

	@Override
	public void addThaumaturgy(IThaumaturgy[] thaumaturgy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeThaumaturgy(IThaumaturgy[] thaumaturgy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMagicLearnListener(IMagicLearnListener<IThaumaturgy> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLearnedOnCreation(IThaumaturgy thaumaturgy) {
		// TODO Auto-generated method stub
		return false;
	}

}
