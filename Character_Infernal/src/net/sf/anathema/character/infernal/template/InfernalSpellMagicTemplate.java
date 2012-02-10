package net.sf.anathema.character.infernal.template;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.generic.impl.template.magic.SpellMagicTemplate;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.ISpell;
import net.sf.anathema.character.generic.magic.spells.CircleType;
import net.sf.anathema.character.generic.magic.spells.ICircleTypeVisitor;
import net.sf.anathema.character.generic.template.ICharacterTemplate;
import net.sf.anathema.character.generic.traits.types.YoziType;

public class InfernalSpellMagicTemplate extends SpellMagicTemplate
{
	public InfernalSpellMagicTemplate(CircleType[] sorceryCircles, CircleType[] necromancyCircles, CircleType[] protocolCircles,
			  ICharacterTemplate template)
	{
		super(sorceryCircles, necromancyCircles, protocolCircles, template);
	}
	
	@Override
	public boolean canLearnSpell(final ISpell spell, final ICharm[] knownCharms)
	{
	     final Boolean canLearn[] = new Boolean[1];
	     
	     //may be some special spell specific stuff here for demon summoning
	     
		 spell.getCircleType().accept(new ICircleTypeVisitor() {
		      public void visitTerrestrial(CircleType type) {
		        canLearn[0] = canLearnSorcery(spell, knownCharms);
		      }

  @Override
  public boolean canLearnSpell(final ISpell spell, final ICharm[] knownCharms) {
    final boolean canLearn[] = new boolean[1];

    // may be some special spell specific stuff here for demon summoning

    spell.getCircleType().accept(new ICircleTypeVisitor() {
      public void visitTerrestrial(CircleType type) {
        canLearn[0] = canLearnSorcerySpell(spell, knownCharms);
      }

      public void visitCelestial(CircleType type) {
        canLearn[0] = canLearnSorcerySpell(spell, knownCharms);
      }

		      public void visitVoid(CircleType type) {
		    	  canLearn[0] = canLearnNecromancy(spell, knownCharms);        
		      }
		      
		      public void visitManMachine(CircleType type) {
		    	  canLearn[0] = canLearnProtocols(spell, knownCharms);
		      }
		      
		      public void visitGodMachine(CircleType type) {
		    	  canLearn[0] = canLearnProtocols(spell, knownCharms);
		      }
		    });

		 return canLearn[0];
	  }
	
	private boolean canLearnProtocols(ISpell spell, ICharm[] knownCharms)
	{
		if (spell.getCircleType() == CircleType.ManMachine)
			return knowsCharm("Alchemical.Man-MachineWeavingEngine", knownCharms);
		if (spell.getCircleType() == CircleType.GodMachine)
			return knowsCharm("Alchemical.God-MachineWeavingEngine", knownCharms);
		return false;
	}
	
	private boolean canLearnSorcery(ISpell spell, ICharm[] knownCharms)
	{
		String[] charmNames = getCharmNames(spell.getCircleType());
		
		for (String charm : charmNames)
			if (knowsCharm(charm, knownCharms))
				return true;
		return false;
	}
	
	private boolean canLearnNecromancy(ISpell spell, ICharm[] knownCharms)
	{
		boolean canLearnNecromancy = knowsCharm("Infernal.UltimateDarknessInternalization", knownCharms);
		return canLearnNecromancy && canLearnSorcery(spell, knownCharms);
	}
	
	private boolean knowsCharm(String charm, ICharm[] knownCharms)
	{
		for (ICharm knownCharm : knownCharms)
			  if (charm.equals(knownCharm.getId()))
				  return true;
		  return false;
	}
	
	public String[] getCharmNames(CircleType circle)
	{
		final List<String> names = new ArrayList<String>();
		for (final YoziType yozi : YoziType.values())
		{
		  circle.accept(new ICircleTypeVisitor() {
		      public void visitTerrestrial(CircleType type) {
		        names.add("Infernal.SorcerousEnlightenment." + yozi.getId());
		      }

      public void visitLabyrinth(CircleType type) {
        canLearn[0] = canLearnNecromancySpell(spell, knownCharms);
      }

      public void visitVoid(CircleType type) {
        canLearn[0] = canLearnNecromancySpell(spell, knownCharms);
      }
    });

    return canLearn[0];
  }

  protected boolean knowsCharm(String charm, ICharm[] knownCharms) {
    for (ICharm knownCharm : knownCharms)
      if (charm.equals(knownCharm.getId()))
        return true;
    return false;
  }
  
  protected boolean knowsNecromancyInitiation(ICharm[] knownCharms) {
    return knowsCharm(necromancyInitiation, knownCharms);
  }
  
  @Override
  public boolean knowsSorcery(ICharm[] knownCharms) {
    for (CircleType circle : getSorceryCircles())
      for (String initiation : getInitiations(circle))
        if (knowsCharm(initiation, knownCharms))
          return true;
    return false;
  }

		      public void visitVoid(CircleType type) {
		    	  names.add("Infernal.SorcerousEnlightenment3." + yozi.getId());        
		      }
		      
		      public void visitManMachine(CircleType type)
		      {
		    	  // do nothing
		      }
		      
		      public void visitGodMachine(CircleType type)
		      {
		    	  // do nothing
		      }
		    });
		}
		String[] charms = new String[names.size()];
		  names.toArray(charms);
		  return charms;
	}

  private boolean canLearnSorcerySpell(ISpell spell, ICharm[] knownCharms) {
    String[] charmNames = getInitiations(spell.getCircleType());

    for (String charm : charmNames)
      if (knowsCharm(charm, knownCharms))
        return true;
    return false;
  }

  private boolean canLearnNecromancySpell(ISpell spell, ICharm[] knownCharms) {
    return knowsNecromancyInitiation(knownCharms)
           && canLearnSorcerySpell(spell, knownCharms);
  }

  public String[] getInitiations(CircleType circle) {
    final List<String> names = new ArrayList<String>();
    for (final YoziType yozi : YoziType.values()) {
      circle.accept(new ICircleTypeVisitor() {
        public void visitTerrestrial(CircleType type) {
          names.add("Infernal.SorcerousEnlightenment." + yozi.getId());
        }

        public void visitCelestial(CircleType type) {
          names.add("Infernal.SorcerousEnlightenment2." + yozi.getId());
        }

        public void visitSolar(CircleType type) {
          names.add("Infernal.SorcerousEnlightenment3." + yozi.getId());
        }

        public void visitShadowland(CircleType type) {
          names.add("Infernal.SorcerousEnlightenment." + yozi.getId());
        }

        public void visitLabyrinth(CircleType type) {
          names.add("Infernal.SorcerousEnlightenment2." + yozi.getId());
        }

        public void visitVoid(CircleType type) {
          names.add("Infernal.SorcerousEnlightenment3." + yozi.getId());
        }
      });
    }
    String[] charms = new String[names.size()];
    names.toArray(charms);
    return charms;
  }

}
