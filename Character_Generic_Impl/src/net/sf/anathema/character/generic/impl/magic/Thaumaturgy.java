package net.sf.anathema.character.generic.impl.magic;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.magic.IMagicVisitor;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.generic.magic.general.ICostList;
import net.sf.anathema.character.generic.rules.IExaltedSourceBook;
import net.sf.anathema.character.generic.traits.types.AbilityType;

public abstract class Thaumaturgy implements IThaumaturgy {
	
	@Override
	public void accept(IMagicVisitor visitor) {
		visitor.visitThaumaturgy(this);
	}

	@Override
	public boolean isFavored(IBasicCharacterData basicCharacter,
			IGenericTraitCollection traitCollection) {
		return traitCollection.isFavoredOrCasteTrait(AbilityType.Occult);
	}

	@Override
	public IExaltedSourceBook[] getSources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExaltedSourceBook getPrimarySource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICostList getTemporaryCost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
