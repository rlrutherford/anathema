package net.sf.anathema.character.generic.impl.magic;

import net.sf.anathema.character.generic.magic.IThaumaturgyVisitor;
import net.sf.anathema.character.generic.magic.ThaumaturgyRank;
import net.sf.anathema.character.generic.magic.charms.duration.IDuration;
import net.sf.anathema.character.generic.rules.IExaltedSourceBook;
import net.sf.anathema.character.generic.traits.types.AttributeType;
import net.sf.anathema.lib.util.IIdentificate;

public class ThaumaturgyDegree extends Thaumaturgy {

	public ThaumaturgyDegree(IIdentificate art, ThaumaturgyRank rank) {
		super(art.getId() + "." + rank.toString(), art, rank);
	}

	@Override
	public void visitThaumaturgy(IThaumaturgyVisitor visitor) {
		visitor.visitDegree(this);
	}

	@Override
	public AttributeType getAttribute() {
		return null;
	}

	@Override
	public int getDifficulty() {
		return 0;
	}

	@Override
	public IDuration getDuration() {
		return null;
	}

	@Override
	public IExaltedSourceBook[] getSources() {
		return null;
	}
}
