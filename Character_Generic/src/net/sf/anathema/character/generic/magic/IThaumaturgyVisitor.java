package net.sf.anathema.character.generic.magic;

public interface IThaumaturgyVisitor {
	public void visitDegree(IThaumaturgy degree);
	
	public void visitProcedure(IThaumaturgy procedure);
}
