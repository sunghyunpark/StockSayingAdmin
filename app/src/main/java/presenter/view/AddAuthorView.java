package presenter.view;

import base.presenter.view.BasePresenterView;

public interface AddAuthorView extends BasePresenterView {

    void setAuthorList();    // author 정보를 받아와 셋팅

    void registerAuthor();

}
