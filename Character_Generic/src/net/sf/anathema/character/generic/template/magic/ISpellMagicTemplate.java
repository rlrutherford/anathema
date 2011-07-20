package net.sf.anathema.character.generic.template.magic;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.ISpell;
import net.sf.anathema.character.generic.magic.spells.CircleType;

public interface ISpellMagicTemplate {

  public CircleType[] getNecromancyCircles();

  public CircleType[] getSorceryCircles();
  
  public CircleType[] getProtocolCircles();

  public boolean knowsNecromancy();

  public boolean knowsSorcery();
  
  public boolean knowsProtocols();
  
  public boolean knowsSpellMagic();
  
  public boolean canLearnSpell(ISpell spell, ICharm[] knownCharms);
}