package net.sf.anathema.character.alchemical.slots.presenter;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ListCellRenderer;

import net.disy.commons.swing.action.SmartAction;
import net.sf.anathema.character.alchemical.slots.model.CharmPick;
import net.sf.anathema.character.alchemical.slots.model.CharmSlot;
import net.sf.anathema.character.alchemical.slots.model.ICharmSlotsModel;
import net.sf.anathema.character.alchemical.slots.view.ICharmSlotsView;
import net.sf.anathema.character.alchemical.slots.view.ISlotView;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.ICharacterChangeListener;
import net.sf.anathema.character.generic.magic.IGenericCombo;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.framework.view.AbstractSelectCellRenderer;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;
import net.sf.anathema.lib.gui.IPresenter;
import net.sf.anathema.lib.gui.selection.ObjectSelectionView;
import net.sf.anathema.lib.resources.IResources;

public class CharmSlotsPresenter implements IPresenter
{
	private ObjectSelectionView<IGenericCombo> comboSelection;
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
				updateComboView();
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
		
		ListCellRenderer renderer = new AbstractSelectCellRenderer<IGenericCombo>(resources) {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getCustomizedDisplayValue(IGenericCombo value) {
				return value.getName() == null ? "N/A" : value.getName();
			}
        };
        SmartAction newComboAction = new SmartAction()
        {
			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(Component parentComponent)
			{
				IGenericCombo currentCombo = comboSelection.getSelectedObject();
				if (currentCombo != null)
				{
					model.installCombo(currentCombo);
					updateComboView();
					updateAllSlotViews();
					comboSelection.setSelectedObject(null);
					model.fireChange();
				}
			}
        	
        };
        comboSelection = new ObjectSelectionView<IGenericCombo>("", renderer);
		view.createControlPanel(newGenericAction,
				newDedicatedAction,
				comboSelection,
				newComboAction);
		
		updateAllSlotViews();
		updateComboView();
	}
	
	private void addCharmSlotView(final CharmSlot slot, int index)
	{
		ListCellRenderer renderer = new AbstractSelectCellRenderer<CharmPick>(resources) {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getCustomizedDisplayValue(CharmPick pick) {
				String charmName = resources.getString(pick.getCharm().getId());
				String prefix = "";
				if (pick.getCombo() != null)
				{
					String comboName = pick.getCombo().getName();
					comboName = comboName == null ? "N/A" : comboName;
					prefix = comboName + ": ";
				}
				return prefix + charmName;
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
		newView.setSelectionValue(slot.getPick());
		newView.addObjectValueChangedListener(new IObjectValueChangedListener<CharmPick>()
				{
					@Override
					public void valueChanged(CharmPick newValue)
					{
						if (newView.isChanging())
							return;
						if (newView.isNull())
							newValue = null;
						if (slot.setPick(newValue))
						{
							updateAllSlotViewsExcept(slot);
							updateSlotView(slot);
						}
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
		view.updateAttunementString(model.getAttunedMotes(), model.getMaxPersonalMotes());
		updateComboView();
	}
	
	private void updateSlotView(CharmSlot slot)
	{
		CharmPick[] validPicks = model.getValidPicks(slot);
		ISlotView view = slotViews.get(slot);
		view.setPicks(validPicks);
		
		if (checkValidPick(validPicks, slot.getPick()))
			view.setSelectionValue(slot.getPick());
		else
		{
			view.setSelectionValue(null);
			slot.setCharm(null);
		}
		
		view.setToggleEnabled(model.allowToggle(slot));
		view.setRemoveEnabled(model.allowRemoval(slot));
		view.setChangeEnabled(model.allowChange(slot));
	}
	
	private boolean checkValidPick(CharmPick[] validPicks, CharmPick pick)
	{
		for (CharmPick currentPick : validPicks)
			if (currentPick != null && currentPick.equals(pick))
				return true;
		return false;
	}
	
	private void updateComboView()
	{
		comboSelection.setObjects(model.getAvaliableCombos());
	}
}
