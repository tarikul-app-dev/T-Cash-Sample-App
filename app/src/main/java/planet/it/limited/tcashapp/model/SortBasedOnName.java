package planet.it.limited.tcashapp.model;

import android.graphics.Bitmap;

import java.util.Comparator;

/**
 * Created by Tarikul on 6/7/2018.
 */

public class SortBasedOnName implements Comparator {

    public int compare(Object o1, Object o2)
    {

        ContactModel dd1 = (ContactModel) o1;// where FBFriends_Obj is your object class
        ContactModel dd2 = (ContactModel)o2;
        return dd1.userName.compareToIgnoreCase(dd2.userName);//where uname is field name
    }


}
