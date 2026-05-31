package edu.polytech.filrouge_teamM.report.adapter;

import android.content.Context;
import android.view.View;
import java.util.List;

public interface ClickableIssue<T> {
    void onRatingBarChange(int itemIndex, float value, IssueAdapter adapter, List<T> items);
    void onClickItem(List<T> items, int itemIndex);
    void onStatusArrowClick(View view, List<T> items, int itemIndex);
    Context getContext();
}
