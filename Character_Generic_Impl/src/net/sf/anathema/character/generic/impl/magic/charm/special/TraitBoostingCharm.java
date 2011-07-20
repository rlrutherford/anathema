package net.sf.anathema.character.generic.impl.magic.charm.special;

import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharmVisitor;
import net.sf.anathema.character.generic.magic.charms.special.ITraitBoostingCharm;
import net.sf.anathema.character.generic.traits.IGenericTrait;
import net.sf.anathema.character.generic.traits.ITraitType;

public class TraitBoostingCharm extends TraitDependentMultiLearnableCharm implements ITraitBoostingCharm
{
	private final ITraitType limitType;
	private final double multiplier;
	
	public TraitBoostingCharm(String charmId, ITraitType trait, ITraitType limit, int max, double multiplier)
	{
		super(charmId, max, trait);
		this.limitType = limit;
		this.multiplier = multiplier;
	}

	@Override
	public void accept(ISpecialCharmVisitor visitor)
	{
		visitor.visitTraitBoostingCharm(this);
	}

	public int getMaximumLearnCount(IGenericTraitCollection traitCollection)
	{
		IGenericTrait trait = traitCollection.getTrait(limitType);
		int count = trait.getCurrentValue();
		count *= multiplier;
		count = Math.max(count, 0);
		count = Math.min(count, getAbsoluteLearnLimit());
		return count;
	}
	
	public ITraitType getLimitingTraitType()
	{
		return limitType;
	}

	public String toString()
	{
		return "[" + getCharmId() + ";boost " + getTraitType().getId() + "]"; 
	}
}
