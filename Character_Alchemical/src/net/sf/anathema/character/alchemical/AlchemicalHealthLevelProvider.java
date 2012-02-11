package net.sf.anathema.character.alchemical;

import net.sf.anathema.character.generic.health.HealthLevelType;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.character.model.health.IHealthLevelProvider;
import net.sf.anathema.character.model.traits.ICoreTraitConfiguration;

public class AlchemicalHealthLevelProvider implements IHealthLevelProvider
{
	ICoreTraitConfiguration traits;
	
	public AlchemicalHealthLevelProvider(ICoreTraitConfiguration traits)
	{
		this.traits = traits;
	}

	@Override
	public int getHealthLevelTypeCount(HealthLevelType type)
	{
		if (type == HealthLevelType.TWO)
			return traits.getTrait(OtherTraitType.Essence).getCurrentValue();
		
		return 0;
	}
}
