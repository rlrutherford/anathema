package net.sf.anathema.character.alchemical.slots;

import org.dom4j.Element;

import net.sf.anathema.character.generic.framework.xml.additional.IAdditionalTemplateParser;
import net.sf.anathema.character.generic.template.additional.IAdditionalTemplate;
import net.sf.anathema.lib.exception.PersistenceException;
import net.sf.anathema.lib.xml.ElementUtilities;

public class CharmSlotsTemplateParser implements IAdditionalTemplateParser
{
	private final static String ATTRIB_GENERIC = "generic";
	private final static String ATTRIB_DEDICATED = "dedicated";

	@Override
	public IAdditionalTemplate parse(Element element) {
		int generic = 0, dedicated = 0;
		try {
			generic = ElementUtilities.getIntAttrib(element, ATTRIB_GENERIC, 0);
			dedicated = ElementUtilities.getIntAttrib(element, ATTRIB_DEDICATED, 0);
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new CharmSlotsTemplate(generic, dedicated);
	}

}
