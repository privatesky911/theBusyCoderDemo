package com.dyhdm.huxj.fauxsendermnc;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("NewApi")
public class CTService extends ChooserTargetService {
    private String titleTemplate;
    @Override
    public void onCreate() {
        super.onCreate();
        titleTemplate=getString(R.string.title_template);
    }
    @Override
    public List<ChooserTarget> onGetChooserTargets(ComponentName sendTarget,
                                                   IntentFilter matchedFilter) {
        ArrayList<ChooserTarget> result = new ArrayList<ChooserTarget>();
        for (int i = 1; i <= 6; i++) {
            result.add(buildTarget(i));
        }
        return (result);
    }

    private ChooserTarget buildTarget(int targetId) {
        String title=String.format(titleTemplate, targetId);
        int iconId=getResources().getIdentifier("ic_share" + targetId,
                "drawable", getPackageName());
        Icon icon= Icon.createWithResource(this, iconId);
        float score=1.0f-((float)targetId/40);
        ComponentName cn=new ComponentName(this, FauxSender.class);
        Bundle extras=new Bundle();
        extras.putInt(FauxSender.EXTRA_TARGET_ID, targetId);
        return(new ChooserTarget(title, icon, score, cn, extras));
    }
}
