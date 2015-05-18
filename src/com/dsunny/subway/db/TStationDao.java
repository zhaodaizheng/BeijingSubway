package com.dsunny.subway.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.dsunny.subway.bean.TStation;
import com.dsunny.subway.util.Logger;

/**
 * @author m 查询TSTATION表
 * 
 */
public class TStationDao {
    public static final String TAG = "TStationDao";

    private DataBase db;

    public TStationDao() {
        db = DataBase.getInstance();
    }

    /**
     * @param sid
     *            车站ID(图)
     * @return 车站是否是首末车站或换乘站
     */
    public boolean isTransOrTailStation(String sid) {
        boolean result = false;

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT count(*) ");
        sql.append(" FROM TSTATION ");
        sql.append(" WHERE StartSID = '").append(sid).append("' ");
        sql.append(" OR EndSID = '").append(sid).append("' ");

        Cursor c = db.query(sql.toString());
        if (c.moveToFirst()) {
            result = c.getInt(0) > 0 ? true : false;
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, String.valueOf(result));

        c.close();
        return result;
    }

    /**
     * @return 所有车站换乘信息
     */
    public List<TStation> getAllTStations() {
        List<TStation> result = new ArrayList<TStation>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT StartSID, EndSID, LID, Meters ");
        sql.append(" FROM TSTATION ");

        Cursor c = db.query(sql.toString());
        while (c.moveToNext()) {
            TStation ts = new TStation();
            ts.StartSID = c.getString(0);
            ts.EndSID = c.getString(1);
            ts.LID = c.getString(2);
            ts.Meters = c.getInt(3);
            result.add(ts);
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, result.toString());

        c.close();
        return result;
    }

    /**
     * @param lid
     *            线路ID
     * @return 线路车站换乘信息
     */
    public List<TStation> getLineTStations(String lid) {
        List<TStation> result = new ArrayList<TStation>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT StartSID, EndSID, LID, Meters ");
        sql.append(" FROM TSTATION ");
        sql.append(" WHERE LID = '").append(lid).append("' ");

        Cursor c = db.query(sql.toString());
        while (c.moveToNext()) {
            TStation ts = new TStation();
            ts.StartSID = c.getString(0);
            ts.EndSID = c.getString(1);
            ts.LID = c.getString(2);
            ts.Meters = c.getInt(3);
            result.add(ts);
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, result.toString());

        c.close();
        return result;
    }
}
