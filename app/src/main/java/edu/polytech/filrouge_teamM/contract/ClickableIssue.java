package edu.polytech.filrouge_teamM.contract;

import android.content.Context;
import java.util.List;

import edu.polytech.filrouge_teamM.adapter.IssueAdapter;

public interface ClickableIssue<T> {
    void onRatingBarChange(int itemIndex, float value, IssueAdapter adapter, List<T> items);
    void onClickItem(List<T> items, int itemIndex);
    Context getContext();
}
