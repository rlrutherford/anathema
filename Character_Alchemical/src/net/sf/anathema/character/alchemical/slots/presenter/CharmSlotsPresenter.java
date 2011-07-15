package net.sf.anathema.character.alchemical.slots.presenter;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ListCellRenderer;

import net.disy.commons.swing.action.SmartAction;
import net.sf.anathema.character.alchemical.slots.model.CharmSlot;
import net.sf.anathema.character.alchemical.slots.model.ICharmSlotsModel;
import net.sf.anathema.character.alchemical.slots.view.ICharmSlotsView;
import net.sf.anathema.character.alchemical.slots.view.ISlotView;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.ICharacterChangeListener;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.framework.view.AbstractSelectCellRenderer;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;
import net.sf.anathema.lib.gui.IPresenter;
import net.sf.anathema.lib.resources.IResources;

public class CharmSlotsPresenter implements IPresenter
{
	private Map<CharmSlot, ISlotView> slotViews = new HashMap<CharmSlot, ISlotView>();
	private IResources resources;
	private ICharmSlotsView view;
	private ICharmSlotsModel model;
	
	public CharmSlotsPresenter(
			IResources resources,
			ICharmSlotsView view,
			final ICharmSlotsModel model)
	{
		this.resources = resources;
		this.view = view;
		this.model = model;
		
		model.addCharacterChangeListener(new ICharacterChangeListener()
		{

			@Override
			public void casteChanged() {
			}

			@Override
			public void characterChanged() {
				model.validateSlots();
				updateAllSlotViews();
			}

			@Override
			public void experiencedChanged(boolean experienced)
			{
				updateAllSlotViews();
			}

			@Override
			public void traitChanged(ITraitType type) {
			}
			
		});
	}

	@Override
	public void initPresentation()
	{
		int i = 0;
		for (final CharmSlot slot : model.getCharmSlots())
			addCharmSlotView(slot, i++);
		
		SmartAction newGenericAction = new SmartAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(Component parentComponent)
			{
				CharmSlot newSlot = model.addNewCharmSlot(true);
				addCharmSlotView(newSlot, Math.max(0, model.getGenericSlotCount() - 1));
				updateAllSlotViews();
				model.fireChange();
			}
		};
		SmartAction newDedicatedAction = new SmartAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(Component parentComponent) {
				CharmSlot newSlot = model.addNewCharmSlot(false);
				addCharmSlotView(newSlot, 
						Math.max(0, model.getGenericSlotCount() + model.getDedicatedSlotCount() - 1));
				updateAllSlotViews();
				model.fireChange();
			}
		};
		
		view.createAddSlotsPanel(newGenericAction, newDedicatedAction);
		
		updateAllSlotViews();
	}
	
	private void addCharmSlotView(final CharmSlot slot, int index)
	{
		ListCellRenderer renderer = new AbstractSelectCellRenderer<ICharm>(resources) {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getCustomizedDisplayValue(ICharm value) {
				return resources.getString(value.getId());
			}
        };
        SmartAction toggleAction = new SmartAction()
        {
			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(Component parentComponent)
			{
				model.toggleSlot(slot);
				view.updateSlotView(slotViews.get(slot), slot);
				updateAllSlotViews();
				model.fireChange();
			}
        };
        SmartAction removeAction = new SmartAction()
        {
			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(Component parentComponent)
			{
				model.removeSlot(slot);
				slotViews.get(slot).remove();
				model.fireChange();
			}
        };
        boolean canRemove = model.allowRemoval(slot);
        final ISlotView newView = view.addCharmSlotView(index, slot, renderer, toggleAction, removeAction, canRemove);
		slotViews.put(slot, newView);
		updateSlotView(slot);
		newView.setSelectionValue(slot.getCharm());
		newView.addObjectValueChangedListener(new IObjectValueChangedListener<ICharm>()
				{
					@Override
					public void valueChanged(ICharm newValue)
					{
						ICharm cur = newView.getSelectionValue();
						if (slot.setCharm(cur))
							updateAllSlotViewsExcept(slot);
					}
				});
	}
	
	private void updateAllSlotViews()
	{
		updateAllSlotViewsExcept(null);
	}
	
	private void updateAllSlotViewsExcept(CharmSlot exception)
	{
		for (CharmSlot slot : model.getCharmSlots())
		{
			if (slot == exception) continue;
			updateSlotView(slot);
		}
	}
	
	private void updateSlotView(CharmSlot slot)
	{
		ICharm[] validCharms = model.getValidCharms(slot);
		ISlotView view = slotViews.get(slot);
		view.setCharms(validCharms);
		
		view.setToggleEnabled(model.allowToggle(slot));
		view.setRemoveEnabled(model.allowRemoval(slot));
	}
}
