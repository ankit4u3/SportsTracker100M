/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportstrackeronline;

/**
 *
 * @author Developer
 */
public class jackets {

    public jackets(String jacket, String tagone, String tagtwo) {
        this.jacketno = jacket;
        this.tagone = tagone;
        this.tagtwo = tagtwo;

    }

    public String getJacketno() {
        return jacketno;
    }

    public void setJacketno(String jacketno) {
        this.jacketno = jacketno;
    }

    public String getTagone() {
        return tagone;
    }

    public void setTagone(String tagone) {
        this.tagone = tagone;
    }

    public String getTagtwo() {
        return tagtwo;
    }

    public void setTagtwo(String tagtwo) {
        this.tagtwo = tagtwo;
    }

    String jacketno;
    String tagone;
    String tagtwo;

}
