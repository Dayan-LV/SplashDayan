package com.example.splashdayan.MyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.splashdayan.R;
import com.example.splashdayan.db.Password;
import com.example.splashdayan.db.User;

import java.io.Serializable;
import java.util.List;

public class MyAdapter extends BaseAdapter implements Serializable {
    private List<Password> list;
    private Context context;
    private LayoutInflater layoutInflater;
    public static String TAG="Hola";
    public MyAdapter(List<Password> list, Context context)
    {
        this.list = list;
        this.context = context;
        if( context != null)
        {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    public boolean isEmptyOrNull( )
    {
        return list == null || list.size() == 0;
    }

    @Override
    public int getCount()
    {
        if(isEmptyOrNull())
        {
            return 0;
        }
        return list.size();
    }

    public boolean addPassword(Password password)
    {
        if(password != null)
        {
            list.add(password);
            return true;
        }
        return false;
    }

    public boolean removePassword(String site)
    {
        if(site != null)
        {
            for(Password password : list)
            {
                if(password.getSite().equals(site))
                {
                    list.remove(password);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Password getItem(int i)
    {
        if(isEmptyOrNull())
        {
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        TextView textView= null;
        TextView textView1= null;
        ImageView imageView = null;
        view = layoutInflater.inflate(R.layout.activity_list_view, null );
        textView= view.findViewById(R.id.tvSite);
        textView1=view.findViewById(R.id.tvPassword);
        textView1.setText(String.valueOf(list.get(i).getPassword()));
        textView.setText(String.valueOf(list.get(i).getSite()));
        return view;
    }
}
