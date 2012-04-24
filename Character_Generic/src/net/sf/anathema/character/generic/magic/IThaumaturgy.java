package net.sf.anathema.character.generic.magic;

import net.sf.anathema.character.generic.magic.charms.duration.IDuration;
import net.sf.anathema.character.generic.traits.types.AttributeType;
import net.sf.anathema.lib.util.IIdentificate;

public interface IThaumaturgy extends IMagic {
	IIdentificate getArt();
	
	ThaumaturgyRank getRank();
	
	AttributeType getAttribute();
	
	int getDifficulty();
	
	IDuration getDuration();
	
	public void visitThaumaturgy(IThaumaturgyVisitor visitor);
}
