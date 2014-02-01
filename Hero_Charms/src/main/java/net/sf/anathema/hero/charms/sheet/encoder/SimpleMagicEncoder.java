package net.sf.anathema.hero.charms.sheet.encoder;

import com.itextpdf.text.DocumentException;
import net.sf.anathema.hero.charms.sheet.content.AllMagicContent;
import net.sf.anathema.hero.sheet.pdf.encoder.boxes.AbstractContentEncoder;
import net.sf.anathema.hero.sheet.pdf.encoder.general.Bounds;
import net.sf.anathema.hero.sheet.pdf.encoder.graphics.SheetGraphics;
import net.sf.anathema.hero.sheet.pdf.session.ReportSession;

public class SimpleMagicEncoder extends AbstractContentEncoder<AllMagicContent> {

  public SimpleMagicEncoder() {
    super(AllMagicContent.class);
  }

  @Override
  public void encode(SheetGraphics graphics, ReportSession reportSession, Bounds bounds) throws DocumentException {
    float top = bounds.getMinY();
    Bounds remainingBounds = new Bounds(bounds.getMinX(), top, bounds.getWidth(), bounds.getMaxY() - top);
    MagicTableEncoder<AllMagicContent> tableEncoder = new MagicTableEncoder(false, AllMagicContent.class);
    tableEncoder.encodeTable(graphics, reportSession, remainingBounds);
  }
}
