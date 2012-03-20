package net.sf.anathema.character.generic.magic;

public interface IThaumaturgy extends IMagic {
	int getRank();
	
	boolean isCreationLearned();
	
	public void visitThaumaturgy(IThaumaturgyVisitor visitor);
}
