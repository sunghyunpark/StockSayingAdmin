package view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.investmentkorea.android.admin.R;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.AuthorModel;
import presenter.AddAuthorPresenter;
import presenter.view.AddAuthorView;
import util.EndlessRecyclerOnScrollListener;
import util.adapter.AuthorAdapter;

public class AddAuthorActivity extends BaseActivity implements AddAuthorView, TextWatcher {

    private static final int LOAD_DATA_COUNT = 30;

    private String beforeStr = "";
    private ArrayList<AuthorModel> authorModelArrayList;
    private AuthorAdapter authorAdapter;
    private AddAuthorPresenter addAuthorPresenter;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @BindView(R.id.author_recyclerView) RecyclerView authorRecyclerView;
    @BindView(R.id.author_edit_box) EditText authorEditBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_author);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        authorModelArrayList = new ArrayList<>();
        authorAdapter = new AuthorAdapter(getApplicationContext(), authorModelArrayList, new AuthorAdapter.AuthorAdapterListener() {
            @Override
            public void deleteAuthor(final int no, final String authorName, final int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddAuthorActivity.this);
                alert.setTitle("그룹 삭제");
                alert.setMessage("정말 삭제하시겠습니까?");
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        authorAdapter.onItemDismiss(position);
                        addAuthorPresenter.deleteAuthor(no, authorName);
                    }
                });
                alert.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.

                            }
                        });
                alert.show();
            }
        });

        authorEditBox.addTextChangedListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        addAuthorPresenter = new AddAuthorPresenter(getApplicationContext(), this, authorModelArrayList);

        // 최초로 한번 명언 리스트를 불러온다.
        addAuthorPresenter.getAuthorList(true, 0);

        // LoadMore 리스너 등록
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                if(!authorModelArrayList.isEmpty()){
                    addAuthorPresenter.getAuthorList(false, authorModelArrayList.get(authorModelArrayList.size()-1).getNo());
                }
            }
        };
        authorRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        authorRecyclerView.setLayoutManager(linearLayoutManager);
        authorRecyclerView.setAdapter(authorAdapter);

    }

    @Override
    public void registerAuthor(){
        authorAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() >= 10)
        {
            showMessage("10자까지 입력이 가능합니다.");
            authorEditBox.setText(beforeStr);
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        beforeStr = s.toString();

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void setAuthorList(){
        authorAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_btn, R.id.add_btn}) void onClick(View v){
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_btn:
                String authorName = authorEditBox.getText().toString().trim();
                if(authorName.equals("")){
                    showMessage("정보를 입력해주세요.");
                }else {
                    authorEditBox.setText(null);
                    addAuthorPresenter.registerAuthor(authorName);
                    endlessRecyclerOnScrollListener.reset(0, true);
                }
                break;
        }
    }

}
