package net.sf.anathema.character.generic.impl.magic;

import net.sf.anathema.character.generic.magic.IThaumaturgyVisitor;
import net.sf.anathema.character.generic.magic.ThaumaturgyRank;
import net.sf.anathema.character.generic.magic.charms.duration.IDuration;
import net.sf.anathema.character.generic.traits.types.AttributeType;
import net.sf.anathema.lib.util.IIdentificate;

public class ThaumaturgyDegree extends Thaumaturgy {

	public ThaumaturgyDegree(String id, IIdentificate art, ThaumaturgyRank rank,
			AttributeType attribute, int difficulty, IDuration duration) {
		super(id, art, rank, attribute, difficulty, duration);
	}

	@Override
	public void visitThaumaturgy(IThaumaturgyVisitor visitor) {
		visitor.visitDegree(this);
	}
}
