package net.sf.anathema.character.alchemical.reporting;

import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.layout.extended.AbstractSecondEditionExaltPdfPartEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.EncoderRegistry;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.ContentEncoder;
import net.sf.anathema.lib.resources.IResources;

public class SecondEditionAlchemicalPartEncoder extends AbstractSecondEditionExaltPdfPartEncoder
{

  //private final PdfEncodingRegistry registry;

  public SecondEditionAlchemicalPartEncoder(IResources resources)
  {
    super(resources);
  }

  @Override
  public ContentEncoder getGreatCurseEncoder(EncoderRegistry encoderRegistry, ReportContent content) {
    return null;
  }

  @Override
  public ContentEncoder getAnimaEncoder(ReportContent reportContent) {
    return null;
  }
}