package net.sf.anathema.character.generic.impl.magic;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.magic.IMagicVisitor;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.generic.magic.ThaumaturgyRank;
import net.sf.anathema.character.generic.magic.general.ICostList;
import net.sf.anathema.character.generic.rules.IExaltedSourceBook;
import net.sf.anathema.character.generic.traits.types.AbilityType;
import net.sf.anathema.lib.util.IIdentificate;
import net.sf.anathema.lib.util.Identificate;

public abstract class Thaumaturgy extends Identificate implements IThaumaturgy {
	
	private final IIdentificate art;
	private final ThaumaturgyRank rank;
	
	public Thaumaturgy(String id, IIdentificate art, ThaumaturgyRank rank) {
		super(id);
		this.art = art;
		this.rank = rank;
	}
	
	public IIdentificate getArt() {
		return art;
	}
	
	public ThaumaturgyRank getRank() {
		return rank;
	}
	
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
	public IExaltedSourceBook getPrimarySource() {
		IExaltedSourceBook[] sources = getSources();
		if (sources.length == 0) {
			return null;
		}
		return sources[0];
	}

	@Override
	public ICostList getTemporaryCost() {
		// TODO Auto-generated method stub
		return null;
	}
}
