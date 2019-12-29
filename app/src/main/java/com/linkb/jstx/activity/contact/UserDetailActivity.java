
package com.linkb.jstx.activity.contact;


import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.OrganizationRepository;
import com.linkb.jstx.database.StarMarkRepository;
import com.linkb.jstx.model.MomentRule;
import com.linkb.jstx.activity.chat.FriendChatActivity;
import com.linkb.jstx.activity.trend.FriendMomentActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.database.MomentRuleRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;

public class UserDetailActivity extends BaseActivity {
    private Friend friend;
    private User self;
    private WebImageView icon;
    @Override
    public void initComponents() {
        super.setStatusBarColor(Color.TRANSPARENT);
        super.setWindowFullscreen();
        friend = (Friend) this.getIntent().getSerializableExtra(Friend.class.getName());
        self = Global.getCurrentUser();

        if (!self.account.equals(friend.account)) {

            findViewById(R.id.bar_album).setOnClickListener(this);
            findViewById(R.id.bar_menu_starmark).setOnClickListener(this);
            findViewById(R.id.bar_chat).setOnClickListener(this);
            findViewById(R.id.bar_menu_momentrule).setOnClickListener(this);

            if (MomentRuleRepository.hasExist(self.account, friend.account, MomentRule.TYPE_0)) {
                findViewById(R.id.icon_moment_limited).setSelected(true);
            }
            if (StarMarkRepository.isStarMark(friend.account)) {
                findViewById(R.id.icon_starmark).setSelected(true);
            }

        } else {
            findViewById(R.id.bar_panel).setVisibility(View.GONE);
        }

        icon = findViewById(R.id.icon);
        icon.setOnClickListener(this);
        icon.load(FileURLBuilder.getUserIconUrl(friend.account), R.mipmap.lianxiren, 999);

        ((TextView) findViewById(R.id.account)).setText(friend.code);
        ((TextView) findViewById(R.id.email)).setText(friend.email);
        ((TextView) findViewById(R.id.telephone)).setText(friend.telephone);
        ((TextView) findViewById(R.id.org)).setText(OrganizationRepository.queryOrgName(friend.code));
        ((TextView) findViewById(R.id.motto)).setText(friend.motto);


        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(friend.name);

        ImageView genderIcon = findViewById(R.id.icon_gender);
        if (User.GENDER_MAN.equals(friend.gender)) {
            genderIcon.setImageResource(R.drawable.icon_man);
        }
        if (User.GENDER_FEMALE.equals(friend.gender)) {
            genderIcon.setImageResource(R.drawable.icon_lady);
        }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.icon:
                LvxinApplication.getInstance().startPhotoActivity(this, icon.getUrl(), icon);
                break;
            case R.id.bar_album:
                Intent uintent = new Intent(this, FriendMomentActivity.class);
                uintent.putExtra(Friend.class.getName(), friend);
                startActivity(uintent);
                break;
            case R.id.bar_menu_momentrule:
                Intent intent = new Intent(this, MomentRuleActivity.class);
                intent.putExtra(Friend.NAME, friend);
                startActivityForResult(intent, MomentRuleActivity.REQUEST_CODE);
                break;
            case R.id.bar_chat:
                Intent chatIntent = new Intent(this, FriendChatActivity.class);
                chatIntent.putExtra(Constant.CHAT_OTHRES_ID, friend.account);
                chatIntent.putExtra(Constant.CHAT_OTHRES_NAME, friend.name);
                startActivity(chatIntent);
                break;
            case R.id.bar_menu_starmark:
                if (StarMarkRepository.isStarMark(friend.account)) {
                    StarMarkRepository.delete(friend.account);
                    findViewById(R.id.icon_starmark).setSelected(false);
                } else {
                    StarMarkRepository.save(friend.account);
                    findViewById(R.id.icon_starmark).setSelected(true);
                }
                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        boolean isLimited = MomentRuleRepository.hasExist(self.account, friend.account, MomentRule.TYPE_0);
        findViewById(R.id.icon_moment_limited).setSelected(isLimited);

    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_user_detail;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.common_detailed;
    }

}
