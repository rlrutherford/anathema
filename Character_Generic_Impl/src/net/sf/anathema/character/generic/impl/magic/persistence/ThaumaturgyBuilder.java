package net.sf.anathema.character.generic.impl.magic.persistence;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.generic.impl.magic.ThaumaturgyProcedure;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.DurationBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.SourceBuilder;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.generic.magic.ThaumaturgyRank;
import net.sf.anathema.character.generic.magic.charms.duration.IDuration;
import net.sf.anathema.character.generic.rules.IExaltedSourceBook;
import net.sf.anathema.character.generic.traits.types.AttributeType;
import net.sf.anathema.lib.exception.PersistenceException;
import net.sf.anathema.lib.util.IIdentificate;
import net.sf.anathema.lib.util.Identificate;
import net.sf.anathema.lib.xml.ElementUtilities;

import org.dom4j.Document;
import org.dom4j.Element;

public class ThaumaturgyBuilder {
	private final DurationBuilder durationBuilder = new DurationBuilder();
	private final SourceBuilder sourceBuilder = new SourceBuilder();
	
	public IThaumaturgy[] buildRituals(Document thaumaturgyDocument) throws PersistenceException {
		Element ritualListElement = thaumaturgyDocument.getRootElement();
	    List<IThaumaturgy> ritualList = new ArrayList<IThaumaturgy>();
	    for (Object ritualObject : ritualListElement.elements("ritual")) { //$NON-NLS-1$
	      Element ritualElement = (Element) ritualObject;
	      ritualList.add(buildRitual(ritualElement));
	    }
	    return ritualList.toArray(new IThaumaturgy[0]);
	}
	
	private IThaumaturgy buildRitual(Element ritualElement) {
		String id = ritualElement.attributeValue("id"); //$NON-NLS-1$
		IIdentificate art = new Identificate(ritualElement.attributeValue("art"));
		ThaumaturgyRank rank = ThaumaturgyRank.valueOf(ritualElement.attributeValue("rank"));
		Element attributeElement = ritualElement.element("attribute");
		AttributeType attribute = AttributeType.valueOf(attributeElement.attributeValue("id"));
		int difficulty = ElementUtilities.getIntAttrib(ritualElement.element("difficulty"),
				"amount", 0);
		IDuration duration = durationBuilder.buildDuration(ritualElement.element("duration"));
		IExaltedSourceBook[] sources = sourceBuilder.buildSourceList(ritualElement);
		
		return new ThaumaturgyProcedure(id, art, rank, attribute, difficulty, duration, sources);
		
	}
}
