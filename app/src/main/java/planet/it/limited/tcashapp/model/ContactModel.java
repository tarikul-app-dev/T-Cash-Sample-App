package planet.it.limited.tcashapp.model;

import android.graphics.Bitmap;

/**
 * Created by Tarikul on 6/7/2018.
 */

public class ContactModel {
    public ContactModel(String userName, String contactNumber,String imageUri) {
        this.userName = userName;
        this.contactNumber = contactNumber;
        this.userPic = imageUri;
    }
    public ContactModel( ) {
    }

    public String userName;
    public boolean checked;
    public String contactNumber;
    public String  userPic;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }



    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }




    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }



}
