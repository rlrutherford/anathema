package net.sf.anathema.character.alchemical.slots;

import net.sf.anathema.character.generic.template.additional.IAdditionalTemplate;

public class CharmSlotsTemplate implements IAdditionalTemplate {

	  public static final String TEMPLATE_ID = "Alchemical.CharmSlots"; //$NON-NLS-1$

	  private final int genericSlots;
	  private final int dedicatedSlots;
	  
	  public CharmSlotsTemplate(int generic, int dedicated)
	  {
		  this.genericSlots = generic;
		  this.dedicatedSlots = dedicated;
	  }
	  
	  public int getGeneric()
	  {
		  return genericSlots;
	  }
	  
	  public int getDedicated()
	  {
		  return dedicatedSlots;
	  }
	  
	  public String getId() {
		    return TEMPLATE_ID;
		  }
	}
