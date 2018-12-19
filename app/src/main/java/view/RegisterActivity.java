package view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.investmentkorea.android.admin.AdminApplication;
import com.investmentkorea.android.admin.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.SayingModel;
import presenter.RegisterPresenter;
import presenter.view.RegisterView;
import util.Util;
import view.dialog.SelectAuthorDialog;

public class RegisterActivity extends BaseActivity implements RegisterView, TextWatcher {

    private static final int RESULT_DELETE = 3000;
    private String beforeStr = "";
    private int textSize = 15;    // 글자 크기
    private int progress = 0;    // seekBar progress
    private RegisterPresenter registerPresenter;
    private int gravityHorizontal = 1;    // 1(왼쪽), 2(중앙), 3(오른쪽)
    private int gravityVertical = 2;    // 1(상단), 2(중앙), 3(하단)
    private String createdAtDateStr = "";

    private boolean isRegister;
    private SayingModel sayingModel;

    @BindView(R.id.contents_edit_box) EditText contentsEditBox;
    @BindView(R.id.text_size_seek_bar) SeekBar seekBar;
    @BindView(R.id.text_size_tv) TextView textSizeTv;
    @BindView(R.id.preview_contents_tv) TextView previewContentsTv;    // 미리보기 명언 문구
    @BindView(R.id.created_at_tv) TextView createdAtTv;    // 날짜 선택 문구
    @BindView(R.id.preview_created_at_tv) TextView previewCreatedAtTv;    // 미리보기 날짜
    @BindView(R.id.author_tv) TextView authorTv;    // 작가명
    @BindView(R.id.preview_author_tv) TextView previewAuthorTv;    // 미리보기 작가명
    @BindView(R.id.delete_btn) ImageButton deleteBtn;

    @BindView(R.id.gravity_top_layout) ViewGroup gravityTopLayout;
    @BindView(R.id.gravity_vertical_layout) ViewGroup gravityVerticalLayout;
    @BindView(R.id.gravity_bottom_layout) ViewGroup gravityBottomLayout;
    @BindView(R.id.gravity_end_layout) ViewGroup gravityEndLayout;
    @BindView(R.id.gravity_horizontal_layout) ViewGroup gravityHorizontalLayout;
    @BindView(R.id.gravity_start_layout) ViewGroup gravityStartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        isRegister = intent.getBooleanExtra("isRegister", false);
        if(!isRegister){
            deleteBtn.setVisibility(View.VISIBLE);
            sayingModel = new SayingModel();
            sayingModel = (SayingModel)intent.getExtras().getSerializable("SayingModel");
        }

        init(isRegister);
    }

    private void init(boolean isRegister){
        setGravityBtn(gravityHorizontal, gravityVertical);

        registerPresenter = new RegisterPresenter(getApplicationContext(), this);

        //명언 EditText 리스너 연결
        contentsEditBox.addTextChangedListener(this);

        textSizeTv.setTextSize(15);
        seekBar.setMax(5);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int seekBarProgress, boolean fromUser) {
                // TODO Auto-generated method stub
                progress = seekBarProgress;
                textSizeTv.setTextSize(textSize + seekBarProgress);
                textSizeTv.setText(String.valueOf(textSize + seekBarProgress));
                previewContentsTv.setTextSize(textSize + seekBarProgress);
            }
        });

        // 수정인 경우
        if(!isRegister){
            contentsEditBox.setText(sayingModel.getContents());
            previewContentsTv.setText(sayingModel.getContents());
            authorTv.setText(sayingModel.getAuthorName());
            previewAuthorTv.setText(sayingModel.getAuthorName());
            String[] createdAtArray = Util.parseTimeWithoutTime(sayingModel.getCreatedAt()).split("-");
            createdAtDateStr = sayingModel.getCreatedAt();
            createdAtTv.setText(createdAtArray[0]+"년 "+createdAtArray[1]+"월 "+createdAtArray[2]+"일");
            previewCreatedAtTv.setText(createdAtArray[0]+"년 "+createdAtArray[1]+"월 "+createdAtArray[2]+"일");
        }
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-M-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat newFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String strBeforeFormat = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            String strAfterFormat = "";
            try {
                Date originDate = originFormat.parse(strBeforeFormat);

                strAfterFormat = newFormat.format(originDate);
                createdAtDateStr = newFormat2.format(originDate);
            }catch (ParseException e){
                e.printStackTrace();
            }
            createdAtTv.setText(strAfterFormat);
            previewCreatedAtTv.setText(strAfterFormat);
        }
    };

    private void setPreviewTextGravity(int gravityVertical, int gravityHorizontal){
        int horizontal = Gravity.START;
        int vertical = Gravity.CENTER_VERTICAL;

        if(gravityHorizontal == 1){
            horizontal = Gravity.START;
        }else if(gravityHorizontal == 2){
            horizontal = Gravity.CENTER_HORIZONTAL;
        }else if(gravityHorizontal == 3){
            horizontal = Gravity.END;
        }

        if(gravityVertical == 1){
            vertical = Gravity.TOP;
        }else if(gravityVertical == 2){
            vertical = Gravity.CENTER_VERTICAL;
        }else if(gravityVertical == 3){
            vertical = Gravity.BOTTOM;
        }
        previewContentsTv.setGravity(vertical | horizontal);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(contentsEditBox.getLineCount() > 5){
            contentsEditBox.setText(beforeStr);
            contentsEditBox.setSelection(contentsEditBox.length());
            Toast.makeText(getApplicationContext(), "최대 5줄까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
        }else{
            previewContentsTv.setText(s);
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeStr = s.toString();
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void deleteSaying(){
        Intent returnIntent = new Intent();
        setResult(RESULT_DELETE,returnIntent);
        finish();
    }

    private void setGravityBtn(int horizon, int vertical){
        gravityTopLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
        gravityVerticalLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
        gravityBottomLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
        gravityStartLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
        gravityEndLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
        gravityHorizontalLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite));

        switch (vertical){
            case 1:
                gravityTopLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                gravityVerticalLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                break;
            case 3:
                gravityBottomLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                break;
        }

        switch (horizon){
            case 1:
                gravityStartLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                gravityHorizontalLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                break;
            case 3:
                gravityEndLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                break;
        }

        setPreviewTextGravity(gravityVertical, gravityHorizontal);

    }

    @OnClick({R.id.back_btn, R.id.gravity_start_layout, R.id.gravity_horizontal_layout, R.id.gravity_end_layout, R.id.gravity_top_layout, R.id.gravity_vertical_layout, R.id.gravity_bottom_layout,
            R.id.up_layout, R.id.down_layout, R.id.created_at_tv, R.id.author_tv, R.id.save_btn, R.id.delete_btn, R.id.add_author_tv}) void onClick(View v){
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.gravity_start_layout:
                gravityHorizontal = 1;
                setGravityBtn(gravityHorizontal, gravityVertical);
                break;
            case R.id.gravity_horizontal_layout:
                gravityHorizontal = 2;
                setGravityBtn(gravityHorizontal, gravityVertical);
                break;
            case R.id.gravity_end_layout:
                gravityHorizontal = 3;
                setGravityBtn(gravityHorizontal, gravityVertical);
                break;
            case R.id.gravity_top_layout:
                gravityVertical = 1;
                setGravityBtn(gravityHorizontal, gravityVertical);
                break;
            case R.id.gravity_vertical_layout:
                gravityVertical = 2;
                setGravityBtn(gravityHorizontal, gravityVertical);
                break;
            case R.id.gravity_bottom_layout:
                gravityVertical = 3;
                setGravityBtn(gravityHorizontal, gravityVertical);
                break;
            case R.id.up_layout:
                if(progress >= 0 && progress < 5){
                    progress++;
                    seekBar.setProgress(progress);
                }
                break;
            case R.id.down_layout:
                if(progress > 0 && progress < 6){
                    progress--;
                    seekBar.setProgress(progress);
                }
                break;
            case R.id.created_at_tv:
                DatePickerDialog dialog = new DatePickerDialog(this, onDateSetListener, AdminApplication.TODAY_YEAR, AdminApplication.TODAY_MONTH-1, AdminApplication.TODAY_DAY);
                dialog.show();
                break;
            case R.id.author_tv:
                SelectAuthorDialog selectAuthorDialog = new SelectAuthorDialog(this, new SelectAuthorDialog.SelectAuthorListener() {
                    @Override
                    public void selectAuthor(String authorName) {
                        authorTv.setText(authorName);
                        previewAuthorTv.setText(authorName);
                    }
                });
                selectAuthorDialog.show();
                break;
            case R.id.save_btn:
                String contentsStr = contentsEditBox.getText().toString();
                String createdAtStr = createdAtTv.getText().toString();
                String authorNameStr = authorTv.getText().toString();

                if(contentsStr.equals("") || createdAtStr.equals("날짜 선택") || authorNameStr.equals("선택해주세요.")){
                    showMessage("정보를 입력해주세요.");
                }else{
                    if(isRegister){
                        registerPresenter.registerSaying(contentsStr, authorNameStr, gravityHorizontal, gravityVertical, (textSize+progress));
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }else{
                        sayingModel.setAuthorName(authorNameStr);
                        sayingModel.setContents(contentsStr);
                        sayingModel.setCreatedAt(createdAtDateStr);
                        sayingModel.setGravityHorizontal(gravityHorizontal);
                        sayingModel.setGravityVertical(gravityVertical);
                        sayingModel.setTextSize((textSize+progress));
                        registerPresenter.editSaying(sayingModel.getNo(), contentsStr, createdAtDateStr, authorNameStr, gravityHorizontal, gravityVertical, (textSize+progress));
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("SayingModel", sayingModel);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                }
                break;
            case R.id.delete_btn:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("명언 삭제");
                alert.setMessage("정말 삭제 하시겠습니까?");
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        registerPresenter.deleteSaying(sayingModel.getNo());
                    }
                });
                alert.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.

                            }
                        });
                alert.show();
                break;
            case R.id.add_author_tv:
                Intent intent = new Intent(getApplicationContext(), AddAuthorActivity.class);
                startActivity(intent);
                break;
        }
    }
}
