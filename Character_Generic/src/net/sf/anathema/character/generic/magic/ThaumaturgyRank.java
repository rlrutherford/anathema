package net.sf.anathema.character.generic.magic;

public enum ThaumaturgyRank {
	Apprentice, Initiate, Adept, Master;
	
	public int asNumber() {
		return ordinal();
	}
}
