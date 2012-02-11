package net.sf.anathema.character.alchemical.virtueflaw;

import net.sf.anathema.character.generic.character.ILimitationContext;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.impl.traits.AbstractTraitTemplate;
import net.sf.anathema.character.generic.impl.traits.limitation.StaticTraitLimitation;
import net.sf.anathema.character.generic.template.ITraitLimitation;
import net.sf.anathema.character.generic.traits.LowerableState;
import net.sf.anathema.character.library.trait.LimitedTrait;
import net.sf.anathema.character.library.trait.TraitType;
import net.sf.anathema.character.library.trait.favorable.FriendlyIncrementChecker;
import net.sf.anathema.character.library.trait.visitor.IDefaultTrait;
import net.sf.anathema.character.library.virtueflaw.model.VirtueFlaw;

public class AlchemicalVirtueFlaw extends VirtueFlaw
{
	protected int clarityMinimum = 0;
	private IDefaultTrait limitTrait;
	private final ICharacterModelContext context;

	public AlchemicalVirtueFlaw(ICharacterModelContext context) {
		super(context);
		this.context = context;
	}
	
	protected String getLimitString()
	  {
		  return "Alchemical.VirtueFlaw.Clarity";
	  }
	
	public IDefaultTrait getLimitTrait()
	{
	   if (limitTrait == null)
		  limitTrait = new LimitedTrait(new TraitType(getLimitString()),
				  new ClarityTemplate(0, new StaticTraitLimitation(10),
						  LowerableState.LowerableLoss),
				  new FriendlyIncrementChecker(), context.getTraitContext());
		return limitTrait;
	  }
	
	public void setClarityMinimum(int val)
	{
		clarityMinimum = val;
	}
	
	private class ClarityTemplate extends AbstractTraitTemplate
	{
		private final ITraitLimitation limitation;
		
		private ClarityTemplate(int startValue, ITraitLimitation limitation, LowerableState lowerable) {
			super(startValue, lowerable, startValue);
			this.limitation = limitation;
		}

		public int getMinimumValue(ILimitationContext limitationContext) {
		    return clarityMinimum;
		}

		public ITraitLimitation getLimitation()
		{
		    return limitation;
		}
	}

}
