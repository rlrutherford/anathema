package net.sf.anathema.character.presenter.magic.thaumaturgy;

import net.disy.commons.core.util.ArrayUtilities;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.DedicatedCharacterChangeAdapter;
import net.sf.anathema.character.generic.framework.magic.stringbuilder.IMagicSourceStringBuilder;
import net.sf.anathema.character.generic.framework.magic.stringbuilder.MagicInfoStringBuilder;
import net.sf.anathema.character.generic.framework.magic.stringbuilder.ScreenDisplayInfoStringBuilder;
import net.sf.anathema.character.generic.framework.magic.stringbuilder.source.MagicSourceStringBuilder;
import net.sf.anathema.character.generic.framework.magic.view.IMagicViewListener;
import net.sf.anathema.character.generic.magic.IThaumaturgy;
import net.sf.anathema.character.model.ICharacterStatistics;
import net.sf.anathema.character.model.IMagicLearnListener;
import net.sf.anathema.character.model.IThaumaturgyConfiguration;
import net.sf.anathema.character.presenter.magic.detail.DetailDemandingMagicPresenter;
import net.sf.anathema.character.presenter.magic.detail.ShowMagicDetailListener;
import net.sf.anathema.character.view.magic.IMagicViewFactory;
import net.sf.anathema.character.view.magic.IThaumaturgyView;
import net.sf.anathema.lib.compare.I18nedIdentificateComparator;
import net.sf.anathema.lib.compare.I18nedIdentificateSorter;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;
import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.IIdentificate;
import net.sf.anathema.lib.workflow.labelledvalue.IValueView;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThaumaturgyPresenter implements DetailDemandingMagicPresenter {

  public static ThaumaturgyPresenter create(ICharacterStatistics statistics, IResources resources,
          IMagicViewFactory factory) {
    ThaumaturgyModel thaumaturgyModel = new ThaumaturgyModel(statistics);
    return new ThaumaturgyPresenter(thaumaturgyModel, statistics, resources, factory);
  }

  private final IThaumaturgyConfiguration thaumaturgyConfiguration;
  private ThaumaturgyModel thaumaturgyModel;
  private final ICharacterStatistics statistics;
  private final MagicInfoStringBuilder creator;
  private final IResources resources;
  private IIdentificate currentArt;
  private final IMagicSourceStringBuilder<IThaumaturgy> sourceStringBuilder;
  private final ThaumaturgyViewProperties properties;
  private final IThaumaturgyView view;

  public ThaumaturgyPresenter(ThaumaturgyModel thaumaturgyModel, ICharacterStatistics statistics, IResources resources,
          IMagicViewFactory factory) {
    this.thaumaturgyModel = thaumaturgyModel;
    this.statistics = statistics;
    this.properties = new ThaumaturgyViewProperties(resources, statistics);
    this.resources = resources;
    this.creator = new ScreenDisplayInfoStringBuilder(resources);
    this.sourceStringBuilder = new MagicSourceStringBuilder<IThaumaturgy>(resources);
    this.thaumaturgyConfiguration = statistics.getThaumaturgy();
    this.view = factory.createThaumaturgyView(properties);
    currentArt = thaumaturgyModel.getArts()[0];
  }

  @Override
  public void initPresentation() {
    IIdentificate[] allowedArts = thaumaturgyModel.getArts();
    initDetailsView();
    view.initGui(allowedArts);
    view.addMagicViewListener(new IMagicViewListener() {
      @Override
      public void magicRemoved(Object[] removedThaumaturgy) {
        List<IThaumaturgy> thaumaturgyList = new ArrayList<IThaumaturgy>();
        for (Object thaumaturgyObject : removedThaumaturgy) {
        	thaumaturgyList.add((IThaumaturgy) thaumaturgyObject);
        }
        thaumaturgyConfiguration.removeThaumaturgy(thaumaturgyList.toArray(new IThaumaturgy[thaumaturgyList.size()]));
      }

      @Override
      public void magicAdded(Object[] addedThaumaturgy) {
        List<IThaumaturgy> thaumaturgyList = new ArrayList<IThaumaturgy>();
        for (Object thaumaturgyObject : addedThaumaturgy) {
          if (thaumaturgyConfiguration.isThaumaturgyAllowed((IThaumaturgy) thaumaturgyObject)) {
        	  thaumaturgyList.add((IThaumaturgy) thaumaturgyObject);
          }
        }
        thaumaturgyConfiguration.addThaumaturgy(thaumaturgyList.toArray(new IThaumaturgy[thaumaturgyList.size()]));
      }
    });
    view.addArtSelectionListener(new IObjectValueChangedListener<IIdentificate>() {
      @Override
      public void valueChanged(IIdentificate art) {
        currentArt = art;
        view.setMagicOptions(getThaumaturgyToShow());
      }
    });
    thaumaturgyConfiguration.addMagicLearnListener(new IMagicLearnListener<IThaumaturgy>() {
      @Override
      public void magicForgotten(IThaumaturgy[] magic) {
        forgetThaumaturgyListsInView(view, magic);
      }

      @Override
      public void magicLearned(IThaumaturgy[] magic) {
        learnThaumaturgyListsInView(view, magic);
      }
    });
    initSpellListsInView(view);
    statistics.getCharacterContext().getCharacterListening().addChangeListener(new DedicatedCharacterChangeAdapter() {
      @Override
      public void experiencedChanged(boolean experienced) {
        view.clearSelection();
      }
    });
  }

  private void initDetailsView() {
    final JLabel titleView = view.addDetailTitleView();
    titleView.setText(" "); //$NON-NLS-1$
    final IValueView<String> circleView = view.addDetailValueView(properties.getCircleString() + ":"); //$NON-NLS-1$
    final IValueView<String> costView = view.addDetailValueView(properties.getCostString() + ":"); //$NON-NLS-1$
    final IValueView<String> targetView = view.addDetailValueView(properties.getTargetString() + ":"); //$NON-NLS-1$
    final IValueView<String> sourceView = view.addDetailValueView(properties.getSourceString() + ":"); //$NON-NLS-1$
    final ListSelectionListener detailListener = new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        final IThaumaturgy thaumaturgy = (IThaumaturgy) ((JList) e.getSource()).getSelectedValue();
        if (thaumaturgy == null) {
          return;
        }
        titleView.setText(resources.getString(thaumaturgy.getId()));
        circleView.setValue(resources.getString(thaumaturgy.getArt().getId()));
        costView.setValue(creator.createCostString(thaumaturgy));
        if (thaumaturgy.getDuration() == null) {
          targetView.setValue(properties.getUndefinedString());
        } else {
          targetView.setValue(resources.getString("Spells.Target." + thaumaturgy.getDuration())); //$NON-NLS-1$
        }
        sourceView.setValue(sourceStringBuilder.createSourceString(thaumaturgy));
      }
    };
    view.addOptionListListener(detailListener);
    view.addSelectionListListener(detailListener);
  }

  private void initSpellListsInView(final IThaumaturgyView thaumaturgyView) {
	thaumaturgyView.setLearnedMagic(getArtFilteredThaumaturgyList(thaumaturgyConfiguration.getAllLearnedThaumaturgy()));
	thaumaturgyView.setMagicOptions(getThaumaturgyToShow());
  }

  private void forgetThaumaturgyListsInView(final IThaumaturgyView thaumaturgyView, IThaumaturgy[] thaumaturgy) {
	thaumaturgyView.removeLearnedMagic(thaumaturgy);
    IThaumaturgy[] supportedThaumaturgy = getThaumaturgyOfCurrentArt(thaumaturgy);
    thaumaturgyView.addMagicOptions(supportedThaumaturgy, new I18nedIdentificateComparator(resources));
  }

  private void learnThaumaturgyListsInView(final IThaumaturgyView thaumaturgyView, IThaumaturgy[] thaumaturgy) {
    IThaumaturgy[] supportedThaumaturgy = getThaumaturgyOfCurrentArt(thaumaturgy);
    thaumaturgyView.addLearnedMagic(supportedThaumaturgy);
    thaumaturgyView.removeMagicOptions(supportedThaumaturgy);
  }

  private IThaumaturgy[] getThaumaturgyOfCurrentArt(IThaumaturgy[] thaumaturgyList) {
    List<IThaumaturgy> supportedThaumaturgy = new ArrayList<IThaumaturgy>();
    for (IThaumaturgy thaumaturgy : thaumaturgyList) {
      if (currentArt.equals(thaumaturgy.getArt())) {
        supportedThaumaturgy.add(thaumaturgy);
      }
    }
    return supportedThaumaturgy.toArray(new IThaumaturgy[supportedThaumaturgy.size()]);
  }

  private IThaumaturgy[] getThaumaturgyToShow() {
    List<IThaumaturgy> showThaumaturgy = new ArrayList<IThaumaturgy>();
    Collections.addAll(showThaumaturgy, thaumaturgyConfiguration.getThaumaturgyByArt(currentArt));
    showThaumaturgy.removeAll(Arrays.asList(thaumaturgyConfiguration.getAllLearnedThaumaturgy()));
    int count = showThaumaturgy.size();
    IThaumaturgy[] sortedThaumaturgy = new IThaumaturgy[count];
    sortedThaumaturgy = new I18nedIdentificateSorter<IThaumaturgy>()
            .sortAscending(showThaumaturgy.toArray(new IThaumaturgy[count]), sortedThaumaturgy, resources);
    return sortedThaumaturgy;
  }

  private IThaumaturgy[] getArtFilteredThaumaturgyList(IThaumaturgy[] providedThaumaturgy) {
    List<IThaumaturgy> thaumaturgyList = new ArrayList<IThaumaturgy>();
    for (IThaumaturgy thaumaturgy : providedThaumaturgy) {
      if (ArrayUtilities.containsValue(thaumaturgyModel.getArts(), thaumaturgy.getArt())) {
        thaumaturgyList.add(thaumaturgy);
      }
    }
    return thaumaturgyList.toArray(new IThaumaturgy[thaumaturgyList.size()]);
  }

  @Override
  public IView getView() {
    return view;
  }

  @Override
  public void addShowDetailListener(final ShowMagicDetailListener listener) {
    ListSelectionListener editListener = new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        final IThaumaturgy thaumaturgy = (IThaumaturgy) ((JList) e.getSource()).getSelectedValue();
        if (thaumaturgy == null) {
          return;
        }
        listener.showDetail(thaumaturgy.getId());
      }
    };
    view.addOptionListListener(editListener);
    view.addSelectionListListener(editListener);
  }
}
