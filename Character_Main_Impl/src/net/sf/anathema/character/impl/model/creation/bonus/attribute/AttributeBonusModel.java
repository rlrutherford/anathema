package net.sf.anathema.character.impl.model.creation.bonus.attribute;

import net.sf.anathema.character.generic.template.creation.ICreationPoints;
import net.sf.anathema.character.generic.template.points.AttributeGroupPriority;
import net.sf.anathema.character.impl.model.advance.models.AbstractSpendingModel;

public class AttributeBonusModel extends AbstractSpendingModel {
  private final AttributeCostCalculator attributeCalculator;
  private final AttributeGroupPriority priority;
  private final ICreationPoints creationPoints;

  public AttributeBonusModel(
      AttributeCostCalculator attributeCalculator,
      AttributeGroupPriority priority,
      ICreationPoints creationPoints) {
    super("Attributes", creationPoints.getAttributeCreationPoints().isCasteAxis() ?
    		getCasteAxisString(priority) : priority.getId()); //$NON-NLS-1$
    this.attributeCalculator = attributeCalculator;
    this.priority = priority;
    this.creationPoints = creationPoints;
  }
  
  private static String getCasteAxisString(AttributeGroupPriority priority)
  {
	  switch (priority)
	  {
	  case Primary: return "Caste";
	  case Secondary: return "Favored";
	  case Tertiary: return "Other";
	  }
	  return "Caste";
  }

  public int getSpentBonusPoints() {
    return attributeCalculator.getAttributePoints(priority).getBonusPointsSpent();
  }

  public Integer getValue() {
    return attributeCalculator.getAttributePoints(priority).getDotsSpent();
  }

  public int getAlotment() {
    return creationPoints.getAttributeCreationPoints().getCount(priority);
  }
}