package net.sf.anathema.character.impl.model.charm.special;

import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.ICharmLearnableArbitrator;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharmLearnListener;
import net.sf.anathema.character.generic.magic.charms.special.ITraitBoostingCharm;
import net.sf.anathema.character.library.trait.DefaultTrait;
import net.sf.anathema.character.model.charm.ICharmConfiguration;
import net.sf.anathema.character.model.charm.special.ITraitBoostingCharmConfiguration;

public class TraitBoostingCharmConfiguration extends MultiLearnableCharmConfiguration implements ITraitBoostingCharmConfiguration
{
	private final ICharacterModelContext context;
	private final ITraitBoostingCharm specialCharm;
	private final ICharm charm;
	private int lastModifier = 0;
	
	public TraitBoostingCharmConfiguration(
	      final ICharacterModelContext context,
	      final ICharmConfiguration config,
	      final ICharm charm,
	      final ITraitBoostingCharm specialCharm,
	      final ICharmLearnableArbitrator arbitrator)
	{
		super(context, config, charm, specialCharm, arbitrator);
		this.specialCharm = specialCharm;
		this.context = context;
		this.charm = charm;
		
		addSpecialCharmLearnListener(new ISpecialCharmLearnListener()
		{
			@Override
			public void learnCountChanged(int newValue)
			{
				applyModifier(newValue - lastModifier);
				lastModifier = newValue;
			}
		});
	}
	
	@Override
	public void learn(boolean experienced)
	{
		super.learn(experienced);
		applyModifier(getCurrentLearnCount());
	}
	
	@Override
	public void forget()
	{
		super.forget();
		applyModifier(-lastModifier);
	}
	
	private void applyModifier(int amount)
	{
		DefaultTrait trait = (DefaultTrait) context.getTraitCollection().getTrait(specialCharm.getTraitType());
		trait.applyBoost(amount);
		lastModifier += amount;
	}

	

	@Override
	public ICharm getCharm() {
		return charm;
	}

}
