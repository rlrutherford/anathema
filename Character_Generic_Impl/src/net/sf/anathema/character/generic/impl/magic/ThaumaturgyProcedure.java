package net.sf.anathema.character.generic.impl.magic;

import net.sf.anathema.character.generic.magic.IThaumaturgyVisitor;
import net.sf.anathema.character.generic.magic.ThaumaturgyRank;
import net.sf.anathema.character.generic.magic.charms.duration.IDuration;
import net.sf.anathema.character.generic.rules.IExaltedSourceBook;
import net.sf.anathema.character.generic.traits.types.AttributeType;
import net.sf.anathema.lib.util.IIdentificate;

public class ThaumaturgyProcedure extends Thaumaturgy {

	private final IExaltedSourceBook[] sources;
	private final AttributeType attribute;
	private final int difficulty;
	private final IDuration duration;
	
	public ThaumaturgyProcedure(String id, IIdentificate art, ThaumaturgyRank rank,
			AttributeType attribute, int difficulty, IDuration duration,
			IExaltedSourceBook[] sources) {
		super(id, art, rank);
		this.attribute = attribute;
		this.difficulty = difficulty;
		this.duration = duration;
		this.sources = sources;
	}

	@Override
	public void visitThaumaturgy(IThaumaturgyVisitor visitor) {
		visitor.visitProcedure(this);
	}
	
	@Override
	public IExaltedSourceBook[] getSources() {
		return sources;
	}
	
	public AttributeType getAttribute() {
		return attribute;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public IDuration getDuration() {
		return duration;
	}
	
}
