package net.sf.anathema.character.reporting.pdf.content.stats.magic;

import net.sf.anathema.character.generic.magic.IMagicStats;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.generic.magic.IThaumaturgyVisitor;

public class ThaumaturgyProcedureStats extends AbstractThaumaturgyStats {

	public ThaumaturgyProcedureStats(IThaumaturgy thaumaturgy) {
		super(thaumaturgy);
	}

	@Override
	public int compareTo(IMagicStats o) {
		if (o instanceof AbstractThaumaturgyStats) {
			final AbstractThaumaturgyStats thaumaturgy = (AbstractThaumaturgyStats)o;
			final int[] r = new int[1];
			thaumaturgy.visitThaumaturgy(new IThaumaturgyVisitor() {
				@Override
				public void visitDegree(IThaumaturgy degree) {
					r[0] = 1;
				}

				@Override
				public void visitProcedure(IThaumaturgy procedure) {
					r[0] = getMagic().getArt().getId().compareTo(thaumaturgy.getMagic().getArt().getId());
				    if (r[0] == 0) {
				      r[0] = getMagic().getId().compareTo(thaumaturgy.getMagic().getId());
				    }
				}
			});
			return r[0];
		}
		return 1;
	}

}
