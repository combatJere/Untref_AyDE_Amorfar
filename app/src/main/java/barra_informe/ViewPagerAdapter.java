package barra_informe;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import barra_informe.Fragmentos.InformeTabResumen;
import barra_informe.Fragmentos.InformeTabsDetalle;
import barra_informe.Fragmentos.InformeTabsListaPremiados;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }


    /**
     * VER: Ver si combiene hacer InformeTabXXXXXX.newInstance(), en vez de New InformeTabXXXXXX()
     * @param position
     * @return
     */
    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
                InformeTabResumen fragmenttab1 = new InformeTabResumen();
                return fragmenttab1;

            // Open FragmentTab2.java
            case 1:
                InformeTabsDetalle fragmenttab2 = new InformeTabsDetalle();
                return fragmenttab2;

            // Open FragmentTab3.java
            case 2:
                InformeTabsListaPremiados fragmenttab3 = new InformeTabsListaPremiados();
                return fragmenttab3;
        }
        return null;
    }


    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
