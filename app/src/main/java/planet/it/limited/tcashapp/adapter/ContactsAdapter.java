package planet.it.limited.tcashapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import planet.it.limited.tcashapp.R;
import planet.it.limited.tcashapp.model.ContactModel;


/**
 * Created by Tarikul on 6/7/2018.
 */

public class ContactsAdapter extends BaseAdapter {
    private ArrayList<ContactModel> mcontacts;
    private ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();

    private LayoutInflater mInflater;
    Context mContext;
    Bitmap bitmapImage;



    public ContactsAdapter(ArrayList<ContactModel> contact, Context context) {
        this.mcontacts = contact;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
       // mcontacts = new ArrayList<>();

    }


    @Override
    public int getCount() {

        return mcontacts.size();
    }

    @Override
    public ContactModel getItem(int position) {
        try {
            if (mcontacts != null) {
                return mcontacts.get(position);
            } else {
                return null;
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position,  View convertView,  ViewGroup parent) {

        final ViewHolder holder;
        final ContactModel contact = getItem(position);

        LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.contact_list_item, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }





           // holder.contactPic = (de.hdodenhof.circleimageview.CircleImageView) rootView.findViewById(R.id.profile_image);
            //  holder.listItemCheckBox = (CheckBox)rootView.findViewById(R.id.cb_select);

            String name = contact.getUserName();
            String phone = contact.getContactNumber();
            holder.contactImage= contact.getUserPic();

            if( holder.contactImage!=null){
                byte[] bytarray = Base64.decode( holder.contactImage, Base64.DEFAULT);
               bitmapImage = BitmapFactory.decodeByteArray(bytarray, 0,
                        bytarray.length);
            }


            boolean isChecked = contact.isChecked();

            holder.contactsName.setText(name);
            holder.phoneNumber.setText(phone);
            if(bitmapImage!=null){
                holder.contactPic.setImageBitmap(bitmapImage);
            }


        return convertView;
    }





    // /////////// //
    // ViewHolder //
    // ///////// //
    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.contactsName = (TextView) v.findViewById(R.id.txv_user_name);
        holder.phoneNumber = (TextView) v.findViewById(R.id.txv_user_number);
        holder.contactPic = (de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.profile_image);
        holder.contactImage = "";

        return holder;
    }
    private   class ViewHolder {
        TextView contactsName,phoneNumber;
        de.hdodenhof.circleimageview.CircleImageView contactPic;
        public String contactImage ;
       // CheckBox listItemCheckBox;
    }




}
