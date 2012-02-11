package net.sf.anathema.character.alchemical.virtueflaw;

import org.dom4j.Element;

import net.sf.anathema.character.generic.framework.xml.additional.IAdditionalTemplateParser;
import net.sf.anathema.character.generic.template.additional.IAdditionalTemplate;

public class AlchemicalVirtueFlawParser implements IAdditionalTemplateParser {

  @Override
  public IAdditionalTemplate parse(Element element) {
    return new AlchemicalVirtueFlawTemplate();
  }
}