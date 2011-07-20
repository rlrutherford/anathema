package net.sf.anathema.character.generic.magic.charms.special;

import net.sf.anathema.character.generic.traits.ITraitType;

public interface ITraitBoostingCharm extends IMultiLearnableCharm
{
	public ITraitType getTraitType();
	
	public ITraitType getLimitingTraitType();
	
	public int getModifier();
}
