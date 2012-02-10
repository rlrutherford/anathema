package net.sf.anathema.character.impl.model.charm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.disy.commons.core.util.Ensure;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;
import net.sf.anathema.character.generic.magic.charms.type.CharmType;
import net.sf.anathema.character.model.charm.ICombo;
import net.sf.anathema.lib.control.change.ChangeControl;
import net.sf.anathema.lib.control.change.IChangeListener;
import net.sf.anathema.lib.workflow.textualdescription.ITextualDescription;
import net.sf.anathema.lib.workflow.textualdescription.model.SimpleTextualDescription;

public class Combo implements ICombo {

  private final ChangeControl control = new ChangeControl();
  private final List<ICharm> creationCharmList = new ArrayList<ICharm>();
  private final List<ICharm> experiencedCharmList = new ArrayList<ICharm>();
  private final Map<ICharm, List<ISubeffect>> creationSubeffectList = new HashMap<ICharm, List<ISubeffect>>();
  private final Map<ICharm, List<ISubeffect>> experiencedSubeffectList = new HashMap<ICharm, List<ISubeffect>>();
  private ICharm extraActionCharm = null;
  private ICharm simpleCharm = null;
  private final ITextualDescription name = new SimpleTextualDescription();
  private final ITextualDescription description = new SimpleTextualDescription();
  private Integer id = null;

  public ICharm[] getCharms()
  {
	  ArrayList<ICharm> charms = new ArrayList<ICharm>();
	  charms.addAll(creationCharmList);
	  charms.addAll(experiencedCharmList);
      return charms.toArray(new ICharm[0]);
  }
  
  public ICharm[] getCreationCharms()
  {
	  return creationCharmList.toArray(new ICharm[0]);
  }
  
  public ICharm[] getExperiencedCharms()
  {
	  return experiencedCharmList.toArray(new ICharm[0]);
  }

  public void addCharm(ICharm charm, boolean experienced)
  {
	List<ICharm> targetList = experienced ? experiencedCharmList : creationCharmList;
    targetList.add(charm);
    if (charm.getCharmTypeModel().getCharmType() == CharmType.Simple) {
      simpleCharm = charm;
    }
    if (charm.getCharmTypeModel().getCharmType() == CharmType.ExtraAction) {
      extraActionCharm = charm;
    }
    fireComboChanged();
  }

  public void addComboModelListener(IChangeListener listener) {
    control.addChangeListener(listener);
  }

  private void fireComboChanged() {
    control.fireChangedEvent();
  }

  public void removeCharms(ICharm[] charms) {
    List<ICharm> removal = Arrays.asList(charms);
    creationCharmList.removeAll(removal);
    experiencedCharmList.removeAll(removal);
    if (simpleCharm != null && removal.contains(simpleCharm)) {
      simpleCharm = null;
    }
    if (extraActionCharm != null && removal.contains(extraActionCharm)) {
      extraActionCharm = null;
    }
    fireComboChanged();
  }

  @Override
  public ICombo clone() {
    Combo clone = new Combo();
    copyCombo(this, clone);
    return clone;
  }

  public void getValuesFrom(ICombo combo) {
    this.clear();
    copyCombo(combo, this);
  }

  private void copyCombo(ICombo source, Combo destination) {
    for (ICharm charm : source.getCreationCharms()) {
      destination.addCharm(charm, false);
    }
    for (ICharm charm : source.getExperiencedCharms()) {
        destination.addCharm(charm, true);
      }
    if (source.getId() != null) {
      destination.setId(source.getId());
    }
    destination.name.setText(source.getName().getText());
    destination.description.setText(source.getDescription().getText());
  }

  public void clear() {
    id = null;
    name.setText(""); //$NON-NLS-1$
    description.setText(""); //$NON-NLS-1$
    removeCharms(creationCharmList.toArray(new ICharm[0]));
    removeCharms(experiencedCharmList.toArray(new ICharm[0]));
  }

  public ITextualDescription getName() {
    return name;
  }

  public ITextualDescription getDescription() {
    return description;
  }

  public boolean contains(ICharm charm) {
    return creationCharmList.contains(charm) ||
    	   experiencedCharmList.contains(charm);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    Ensure.ensureNull("Id already set.", this.id); //$NON-NLS-1$
    Ensure.ensureNotNull("Id must not be null.", id); //$NON-NLS-1$
    this.id = id;
  }

	@Override
	public ICharm[] getCreationLearnedCharms() {
		return creationCharmList.toArray(new ICharm[0]);
	}
	
	@Override
	public ICharm[] getExperiencedLearnedCharms() {
		return experiencedCharmList.toArray(new ICharm[0]);
	}
	
	public ISubeffect[] getLearnedSubeffects(ICharm charm)
	{
		List<ISubeffect> effects = new ArrayList<ISubeffect>();
		for(List<ISubeffect> effectList : creationSubeffectList.values())
			effects.addAll(effectList);
		for(List<ISubeffect> effectList : experiencedSubeffectList.values())
			effects.addAll(effectList);
		return effects.toArray(new ISubeffect[0]);		
	}
	
	private List<ISubeffect> getList(ICharm charm, boolean experienced)
	{
		List<ISubeffect> list;
		if (!experienced)
		{
			list = creationSubeffectList.get(charm);
			if (list == null)
			{
				list = new ArrayList<ISubeffect>();
				creationSubeffectList.put(charm, list);
			}
		}
		else
		{
			list = experiencedSubeffectList.get(charm);
			if (list == null)
			{
				list = new ArrayList<ISubeffect>();
				experiencedSubeffectList.put(charm, list);
			}
		}
		return list;
	}
	
	public void addEffect(ICharm charm, ISubeffect effect, boolean experienced)
	{
		getList(charm, experienced).add(effect);
		fireComboChanged();
	}
	
	public void removeEffect(ICharm charm, ISubeffect effect)
	{
		List<ISubeffect> list = getList(charm, true);
		if (!list.contains(effect))
			list = getList(charm, false);
		list.remove(effect);
		fireComboChanged();
	}
	
	public ISubeffect[] getEffects(ICharm charm)
	{
		List<ISubeffect> list = new ArrayList<ISubeffect>();
		list.addAll(getList(charm, false));
		list.addAll(getList(charm, true));
		return list.toArray(new ISubeffect[0]);
	}
	
	public ISubeffect[] getCreationSubeffects()
	{
		List<ISubeffect> creationList = new ArrayList<ISubeffect>();
		for (List<ISubeffect> list : creationSubeffectList.values())
			creationList.addAll(list);
		return creationList.toArray(new ISubeffect[0]);
	}
	
	public ISubeffect[] getExperiencedSubeffects()
	{
		List<ISubeffect> experiencedList = new ArrayList<ISubeffect>();
		for (List<ISubeffect> list : experiencedSubeffectList.values())
			experiencedList.addAll(list);
		return experiencedList.toArray(new ISubeffect[0]);
	}
}