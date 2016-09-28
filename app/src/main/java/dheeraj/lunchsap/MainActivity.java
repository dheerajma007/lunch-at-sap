package dheeraj.lunchsap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    public static MenuTable menuTable;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private boolean readyToRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        int dispHeight = getWindowManager().getDefaultDisplay().getHeight();
        //System.out.println("Height = " + findViewById(R.id.toolbar).getHeight());

        setSupportActionBar(toolbar);



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        File workFile = new File(this.getFilesDir() + "/Menu.xlsx");
        System.out.println(this.getFilesDir() + "/Menu.xlsx");
        boolean flag = false;
        if(workFile.exists())
        {
            long timeNow = System.currentTimeMillis();
            long timeSynced = workFile.lastModified();
            if(timeNow - timeSynced > 3600000L)
                flag = true;
        }
        else
            flag = true;

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        if(false)           //if(flag)   skipped file reading
        {
            dialog.setMessage("Fetching data...");
            dialog.show();
            new DownloadAsync().execute();
        }
        else {
            System.out.println("Sync not required");
            dialog.setMessage("Reading data...");
            dialog.show();
            //ReadExcel readExcel = new ReadExcel(getApplicationContext());
            //menuTable = readExcel.getMenuTable();
            new ReadAsync().execute();
        }

    }

    class ReadAsync extends AsyncTask<String, String, String >{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            ReadExcel readExcel = new ReadExcel(getApplicationContext());
            menuTable = readExcel.getMenuTable();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            createContents();
        }
    }


    class DownloadAsync extends AsyncTask<String, String, String> {
        int result;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("Starting File Access...");
            FileAccess accessObj = new FileAccess(getApplicationContext());
            result = accessObj.syncFile();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.setMessage("Reading data...");
            new ReadAsync().execute();
        }
    }

    public void createContents()
    {
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        for(int i = 1; i <= 5; i++)
            tabLayout.addTab(tabLayout.newTab().setText(getWeekday(i)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, dispHeight - 64));
        //mViewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dispHeight - 64));
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //mSectionsPagerAdapter.setPrimaryItem(mViewPager, 3, mSectionsPagerAdapter.instantiateItem(mViewPager, 3));
        mViewPager.setCurrentItem(3);

        mViewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getWeekday(int day)
    {
        switch (day)
        {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
        }
        return "";
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            int week = getArguments().getInt(ARG_SECTION_NUMBER);

            CardView cards[] = new CardView[3];
            cards[0] = (CardView) rootView.findViewById(R.id.cardViewLunch);
            cards[1] = (CardView) rootView.findViewById(R.id.cardViewSnacks);
            cards[2] = (CardView) rootView.findViewById(R.id.cardViewDinner);
            String cardTitles[] = {"Lunch", "Snacks", "Dinner"};
            int cardBgs[] = {R.drawable.lunch, R.drawable.snacks, R.drawable.dinner};
            for(int i = 0; i <= 2; i++)
            {
                String text = "";
                int len = menuTable.menus[i].menuList.get(0).size();
                boolean categoryExists = false;
                if(menuTable.menus[i].categoryList.size() != 0)
                    categoryExists = true;
                //System.out.println("Category List Size = " + len);

                View cardView = inflater.inflate(R.layout.cardlayout, null);
                LinearLayout cardTitleLayout = (LinearLayout)cardView.findViewById(R.id.cardTitleLayout);
                cardTitleLayout.setBackgroundDrawable(getResources().getDrawable(cardBgs[i]));
                TextView cardTitle = (TextView) cardView.findViewById(R.id.cardTitle);
                cardTitle.setText(cardTitles[i]);
                LinearLayout linearLayout = (LinearLayout) cardView.findViewById(R.id.cardLinearLayout);
                int index = linearLayout.getChildCount();
                for(int j = 0; j < len; j++)
                {
                    text = menuTable.menus[i].menuList.get(week-1).get(j);
                    if(!text.equals("")) {
                        View listView = inflater.inflate(R.layout.list_layout, null);
                        TextView listText = (TextView) listView.findViewById(R.id.textView);
                        listText.setText(text);
                        linearLayout.addView(listView, index++);
                    }
                }

                /*for(int j = 0; j < len; j++)
                {
                    if(categoryExists)
                        text = text + menuTable.menus[i].categoryList.get(j) + "  :  ";
                    text += menuTable.menus[i].menuList.get(week-1).get(j) +"\n";
                }
                TextView textView1 = new TextView(getContext());
                textView1.setSingleLine(false);
                textView1.setText(text);*/
                //cards[i].addView(textView1);


                cards[i].addView(cardView);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Monday";
                case 1:
                    return "Tuesday";
                case 2:
                    return "Wednesday";
                case 3:
                    return "Thursday";
                case 4:
                    return "Friday";
            }
            return null;
        }
    }
}
