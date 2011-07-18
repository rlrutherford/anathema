package net.sf.anathema.character.generic.impl.magic.persistence.builder;

import net.sf.anathema.lib.exception.PersistenceException;
import net.sf.anathema.lib.xml.ElementUtilities;

import org.dom4j.Element;

public class CharmAttunementBuilder
{
	public static final String TAG_ATTUNEMENT = "attunement";
	public static final String TAG_ESSENCE = "essence";
	
	public Integer getAttunementCost(Element rulesElement) throws PersistenceException
	{
		Element attunementElement = rulesElement.element(TAG_ATTUNEMENT);
		if (attunementElement == null)
			return 0;
		return ElementUtilities.getIntAttrib(attunementElement, TAG_ESSENCE, 0);
	}
}
