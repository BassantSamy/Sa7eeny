package com.example.project;

import android.content.res.Resources;

public class Model {
        private Resources res;

        Model(Resources r)
        {
            res=r;
        }

        public String[] GetStuff(int id)
        {
            return res.getStringArray(id);
        }

}
