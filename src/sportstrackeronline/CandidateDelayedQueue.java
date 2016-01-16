/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportstrackeronline;

import java.sql.Connection;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Developer
 */
public class CandidateDelayedQueue implements Delayed {

    public String getRaceid() {
        return raceid;
    }

    public void setRaceid(String raceid) {
        this.raceid = raceid;
    }

    public String getShiftid() {
        return shiftid;
    }

    public void setShiftid(String shiftid) {
        this.shiftid = shiftid;
    }

    public Connection getConnect() {
        return connect;
    }

    public void setConnect(Connection connect) {
        this.connect = connect;
    }

    private String data;
    private String raceid;
    private String shiftid;
    private Connection connect = null;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    private long startTime;

    public CandidateDelayedQueue(String data, String raceid, String shiftid, long delay) {
        this.data = data;
        this.raceid = raceid;
        this.shiftid = shiftid;
        this.startTime = System.currentTimeMillis() + delay;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.startTime < ((CandidateDelayedQueue) o).startTime) {
            return -1;
        }
        if (this.startTime > ((CandidateDelayedQueue) o).startTime) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return data;
//        return "{"
//                + "data='" + data + '\''
//                + ", startTime=" + startTime
//                + '}';
    }
}
