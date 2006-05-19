package net.sf.anathema.charmtree.batik.intvalue;

import static net.sf.anathema.charmtree.provider.svg.ISVGCascadeXMLConstants.ATTRIB_TRANSFORM;
import static net.sf.anathema.charmtree.provider.svg.ISVGCascadeXMLConstants.ATTRIB_Y;
import static net.sf.anathema.charmtree.provider.svg.ISVGCascadeXMLConstants.TAG_G;
import static net.sf.anathema.charmtree.provider.svg.ISVGCascadeXMLConstants.TAG_TEXT;

import java.awt.Color;

import net.sf.anathema.character.generic.impl.traits.EssenceTemplate;
import net.sf.anathema.charmtree.batik.IBoundsCalculator;
import net.sf.anathema.framework.value.IIntValueView;
import net.sf.anathema.lib.control.intvalue.IIntValueChangedListener;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class SVGDefaultTraitView implements IIntValueView {

  private final SVGIntValueDisplay valueDisplay;
  private final String labelString;
  private final double maxWidth;

  public SVGDefaultTraitView(int maxValue, double maxWidth, Color fillColor, String labelString, int initialValue) {
    this.maxWidth = maxWidth;
    this.labelString = labelString;
    this.valueDisplay = new SVGIntValueDisplay(
        maxValue,
        fillColor,
        initialValue,
        SVGIntValueDisplay.getDiameter(maxWidth));
  }

  public Element initGui(SVGOMDocument svgDocument, IBoundsCalculator boundsCalculator) {
    Element groupElement = svgDocument.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, TAG_G);
    Element textElement = svgDocument.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, TAG_TEXT);
    setAttribute(textElement, ATTRIB_Y, String.valueOf(SVGIntValueDisplay.getDiameter(maxWidth) * 0.9));
    Text text = svgDocument.createTextNode(labelString);
    textElement.appendChild(text);
    groupElement.appendChild(textElement);
    Element valueGroupElement = valueDisplay.initGui(svgDocument, boundsCalculator);
    setAttribute(valueGroupElement, ATTRIB_TRANSFORM, "translate(" //$NON-NLS-1$
        + String.valueOf(maxWidth
            - EssenceTemplate.SYSTEM_ESSENCE_MAX
            * SVGIntValueDisplay.getDiameter(maxWidth)
            * 1.11)
        + ",0)"); //$NON-NLS-1$
    groupElement.appendChild(valueGroupElement);
    return groupElement;
  }

  private void setAttribute(org.w3c.dom.Element element, String attributeName, String attributeValue) {
    element.setAttributeNS(null, attributeName, attributeValue);
  }

  public void setValue(int newValue) {
    valueDisplay.setValue(newValue);
  }

  public void addIntValueChangedListener(IIntValueChangedListener listener) {
    valueDisplay.addIntValueChangedListener(listener);

  }

  public void removeIntValueChangedListener(IIntValueChangedListener listener) {
    valueDisplay.removeIntValueChangedListener(listener);
  }

  public void setMaximum(int maximalValue) {
    valueDisplay.setMaximum(maximalValue);
  }

  public void setVisible(boolean visible) {
    valueDisplay.setVisible(visible);
  }
}