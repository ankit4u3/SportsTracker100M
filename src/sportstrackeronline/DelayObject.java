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
public class DelayObject implements Delayed {

    private String data;
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

    public DelayObject(String data, long delay) {
        this.data = data;
        this.startTime = System.currentTimeMillis() + delay;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.startTime < ((DelayObject) o).startTime) {
            return -1;
        }
        if (this.startTime > ((DelayObject) o).startTime) {
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
