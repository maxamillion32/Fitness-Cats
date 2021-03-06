package com.egoriku.catsrunning.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.egoriku.catsrunning.helpers.DbCursor;
import com.egoriku.catsrunning.helpers.InquiryBuilder;
import com.egoriku.catsrunning.models.AllFitnessDataModel;

import java.util.ArrayList;
import java.util.List;

import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Columns.BEGINS_AT;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Columns.DISTANCE;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Columns.LIKED;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Columns.TIME;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Columns.TRACK_TOKEN;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Columns.TYPE_FIT;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Columns._ID;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Query.AND;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Query.IS_TRACK_DELETE_EQ;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Query.IS_TRACK_DELETE_FALSE;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Query.TYPE_FIT_EQ;
import static com.egoriku.catsrunning.models.Constants.ConstantsSQL.Tables.TABLE_TRACKS;
import static com.egoriku.catsrunning.models.Constants.Tags.ARG_SECTION_NUMBER;

public class AsyncTracksLoader extends AsyncTaskLoader<List<AllFitnessDataModel>> {
    private List<AllFitnessDataModel> dataModelList;
    private int typeReminder;


    public AsyncTracksLoader(Context context, Bundle args) {
        super(context);
        if (args != null) {
            typeReminder = args.getInt(ARG_SECTION_NUMBER);
        }
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (dataModelList == null || takeContentChanged()) {
            forceLoad();
        }

        if (dataModelList != null) {
            deliverResult(dataModelList);
        }

    }


    @Override
    public List<AllFitnessDataModel> loadInBackground() {
        dataModelList = new ArrayList<>();
        Cursor cursorTracks = new InquiryBuilder()
                .get(_ID, BEGINS_AT, TIME, DISTANCE, LIKED, TRACK_TOKEN, TYPE_FIT)
                .from(TABLE_TRACKS)
                .where(true, IS_TRACK_DELETE_EQ + " " + IS_TRACK_DELETE_FALSE + " " + AND + " " + TYPE_FIT_EQ + typeReminder)
                .orderBy(BEGINS_AT)
                .desc()
                .select();

        DbCursor cursor = new DbCursor(cursorTracks);
        if (cursor.isValid()) {
            do {
                AllFitnessDataModel listAdapter = new AllFitnessDataModel();
                listAdapter.setId(cursor.getInt(_ID));
                listAdapter.setBeginsAt(cursor.getInt(BEGINS_AT));
                listAdapter.setTime(cursor.getInt(TIME));
                listAdapter.setDistance(cursor.getInt(DISTANCE));
                listAdapter.setLiked(cursor.getInt(LIKED));
                listAdapter.setTrackToken(cursor.getString(TRACK_TOKEN));
                listAdapter.setTypeFit(cursor.getInt(TYPE_FIT));
                dataModelList.add(listAdapter);
            } while (cursorTracks.moveToNext());
        }
        cursor.close();
        return dataModelList;
    }


    @Override
    public void deliverResult(List<AllFitnessDataModel> data) {
        dataModelList = data;
        super.deliverResult(data);
    }
}
