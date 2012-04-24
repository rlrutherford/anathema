package net.sf.anathema.character.reporting.pdf.content.stats.magic;

import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.generic.magic.IThaumaturgyVisitor;
import net.sf.anathema.lib.resources.IResources;

public abstract class AbstractThaumaturgyStats extends AbstractMagicStats<IThaumaturgy> {
	
	public AbstractThaumaturgyStats(IThaumaturgy thaumaturgy) {
		super(thaumaturgy);
	}
	
	public void visitThaumaturgy(final IThaumaturgyVisitor visitor) {
		getMagic().visitThaumaturgy(new IThaumaturgyVisitor() {
			@Override
			public void visitDegree(IThaumaturgy degree) {
				visitor.visitDegree(degree);
			}

			@Override
			public void visitProcedure(IThaumaturgy procedure) {
				visitor.visitProcedure(procedure);
			}
		});
	}
	
	@Override
	public String getNameString(IResources resources) {
		return resources.getString(getName().getId());
	}
	
	@Override
	public String getGroupName(IResources resources) {
		return resources.getString(getMagic().getArt().getId());
	}
	
	@Override
	public String getType(IResources resources) {
		return resources.getString(getMagic().getAttribute().getId());
	}
	
	@Override
	public String[] getDetailStrings(IResources resources) {
		return new String[] { getMagic().getDifficulty() + "" };
	}
	
	@Override
	public String getDurationString(IResources resources) {
		return getMagic().getDuration().getText(resources);
	}
}
