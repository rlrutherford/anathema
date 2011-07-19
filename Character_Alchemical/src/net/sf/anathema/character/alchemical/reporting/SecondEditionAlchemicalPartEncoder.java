package net.sf.anathema.character.alchemical.reporting;

import net.sf.anathema.character.reporting.sheet.PdfEncodingRegistry;
import net.sf.anathema.character.reporting.sheet.common.IPdfContentBoxEncoder;
import net.sf.anathema.character.reporting.sheet.page.AbstractSecondEditionExaltPdfPartEncoder;
import net.sf.anathema.lib.resources.IResources;

public class SecondEditionAlchemicalPartEncoder extends AbstractSecondEditionExaltPdfPartEncoder {

  //private final PdfEncodingRegistry registry;

  public SecondEditionAlchemicalPartEncoder(IResources resources, PdfEncodingRegistry registry, int essenceMax) {
    super(resources, registry, essenceMax);
    //this.registry = registry;
  }

  public IPdfContentBoxEncoder getGreatCurseEncoder() {
    //return new SecondEditionLunarGreatCurseEncoder(getBaseFont());
	  return null;
  }

  @Override
  public IPdfContentBoxEncoder getAnimaEncoder() {
    //return new LunarAnimaEncoderFactory(getResources(), getBaseFont(), getSymbolBaseFont()).createAnimaEncoder();
	return null;
  }

  @Override
  public boolean isEncodeAttributeAsFavorable() {
    return true;
  }
}