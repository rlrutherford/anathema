package net.sf.anathema.character.alchemical;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.ICharmAttribute;

public class AlchemicalUtilities
{
	public static int getAttunementCost(ICharm charm)
	{
		for (ICharmAttribute attribute : charm.getAttributes())
		{
			System.out.println(charm.getId() + " " + attribute.getId());
		}
		return 0;
	}
}
