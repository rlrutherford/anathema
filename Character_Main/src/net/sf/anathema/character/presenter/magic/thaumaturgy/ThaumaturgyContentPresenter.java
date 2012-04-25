package net.sf.anathema.character.presenter.magic.thaumaturgy;

import net.sf.anathema.character.model.ICharacterStatistics;
import net.sf.anathema.character.presenter.magic.IContentPresenter;
import net.sf.anathema.character.presenter.magic.detail.MagicAndDetailPresenter;
import net.sf.anathema.character.presenter.magic.detail.MagicDetailPresenter;
import net.sf.anathema.character.view.magic.IMagicViewFactory;
import net.sf.anathema.framework.presenter.view.IViewContent;
import net.sf.anathema.framework.presenter.view.SimpleViewContent;
import net.sf.anathema.framework.view.util.ContentProperties;
import net.sf.anathema.lib.resources.IResources;

public class ThaumaturgyContentPresenter implements IContentPresenter {

  public static IContentPresenter create(MagicDetailPresenter detailPresenter, ICharacterStatistics statistics,
          IResources resources, IMagicViewFactory factory) {
    String title = resources.getString("CardView.CharmConfiguration.Thaumaturgy.Title");
    ThaumaturgyPresenter spellPresenter = ThaumaturgyPresenter.create(statistics, resources, factory);
    return new MagicAndDetailPresenter(title, detailPresenter, spellPresenter);
  }

  private String title;
  private ThaumaturgyPresenter thaumaturgyPresenter;

  public ThaumaturgyContentPresenter(String title, ThaumaturgyPresenter thaumaturgyPresenter) {
    this.title = title;
    this.thaumaturgyPresenter = thaumaturgyPresenter;
  }

  @Override
  public void initPresentation() {
    thaumaturgyPresenter.initPresentation();
  }

  @Override
  public IViewContent getTabContent() {
    return new SimpleViewContent(new ContentProperties(title), thaumaturgyPresenter.getView());
  }
}
