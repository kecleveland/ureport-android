package in.ureport.managers;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.ilhasoft.support.tool.ResourceUtil;
import br.com.ilhasoft.support.tool.StatusBarDesigner;
import in.ureport.R;
import in.ureport.models.CountryProgram;
import in.ureport.models.rapidpro.AgeGroup;

import static in.ureport.R.string.brasil_channel;
import static in.ureport.R.string.chile_channel;
import static in.ureport.R.string.cotedivoire_channel;
import static in.ureport.R.string.global_channel;
import static in.ureport.R.string.indonesia_channel;
import static in.ureport.R.string.ireland_channel;
import static in.ureport.R.string.mexico_channel;
import static in.ureport.R.string.newguinea_channel;
import static in.ureport.R.string.thailand_channel;
import static in.ureport.R.string.nigeria_channel;
import static in.ureport.R.string.rapidpro_host_address1;
import static in.ureport.R.string.rapidpro_host_address2;
import static in.ureport.R.string.syria_channel;
import static in.ureport.R.string.tunisie_channel;
import static in.ureport.R.string.uganda_channel;
import static in.ureport.R.string.ukraine_channel;
import static in.ureport.R.string.unitedkingdom_channel;
import static in.ureport.R.string.ureport_host_address1;
import static in.ureport.R.string.ureport_host_address2;

import static in.ureport.R.style.AppTheme;
import static in.ureport.R.style.AppTheme_Brasil;
import static in.ureport.R.style.AppTheme_Burundi;
import static in.ureport.R.style.AppTheme_Cameroun;
import static in.ureport.R.style.AppTheme_Chile;
import static in.ureport.R.style.AppTheme_CoteDIvoire;
import static in.ureport.R.style.AppTheme_Fiji;
import static in.ureport.R.style.AppTheme_Guatemala;
import static in.ureport.R.style.AppTheme_Indonesia;
import static in.ureport.R.style.AppTheme_Ireland;
import static in.ureport.R.style.AppTheme_Liberia;
import static in.ureport.R.style.AppTheme_Mali;
import static in.ureport.R.style.AppTheme_Mexico;
import static in.ureport.R.style.AppTheme_Nigeria;
import static in.ureport.R.style.AppTheme_Pakistan;
import static in.ureport.R.style.AppTheme_PapuaNewGuinea;
import static in.ureport.R.style.AppTheme_RepubliqueCentrafricaine;
import static in.ureport.R.style.AppTheme_Senegal;
import static in.ureport.R.style.AppTheme_SierraLeone;
import static in.ureport.R.style.AppTheme_Syria;
import static in.ureport.R.style.AppTheme_Swaiziland;
import static in.ureport.R.style.AppTheme_Thailand;
import static in.ureport.R.style.AppTheme_Tunisia;
import static in.ureport.R.style.AppTheme_Uganda;
import static in.ureport.R.style.AppTheme_Ukraine;
import static in.ureport.R.style.AppTheme_UnitedKingdom;
import static in.ureport.R.style.AppTheme_Zimbabwe;

/**
 * Created by johncordeiro on 7/23/15.
 */
public class CountryProgramManager {

    public static final int INVALID_VALUE = -1;

    private static CountryProgram countryProgram;
    private static List<CountryProgram> countryPrograms;

    public static void switchCountryProgram(CountryProgram countryProgram) {
        CountryProgramManager.countryProgram = countryProgram;
    }

    public static void switchCountryProgram(String countryCode) {
        CountryProgramManager.countryProgram = getCountryProgramForCode(countryCode);
    }

    public static void switchToUserCountryProgram() {
        CountryProgramManager.countryProgram = getCountryProgramForCode(UserManager.getCountryCode());
    }

    public static void setThemeIfNeeded(Activity activity) {
        CountryProgram countryProgram = getCurrentCountryProgram();
        activity.setTheme(countryProgram.getTheme());

        ResourceUtil resourceUtil = new ResourceUtil(activity);
        StatusBarDesigner statusBarDesigner = new StatusBarDesigner();
        statusBarDesigner.setStatusBarColor(activity, resourceUtil.getColorByAttr(R.attr.colorPrimaryDark));
    }

    public static boolean isPollEnabledForCurrentCountry() {
        return getCurrentCountryProgram().getChannel() != INVALID_VALUE;
    }

    public static boolean isPollEnabledForCountry(CountryProgram countryProgram) {
        return countryProgram.getChannel() != INVALID_VALUE;
    }

    @NonNull
    public static CountryProgram getCurrentCountryProgram() {
        return countryProgram != null ? countryProgram : getAvailableCountryPrograms().get(0);
    }

    public static CountryProgram getCountryProgramForCode(String countryCode) {
        CountryProgram countryProgram = new CountryProgram(countryCode);
        int indexOfCountryProgram = getAvailableCountryPrograms().indexOf(countryProgram);
        indexOfCountryProgram = indexOfCountryProgram > 0 ? indexOfCountryProgram : 0;

        return getAvailableCountryPrograms().get(indexOfCountryProgram);
    }

    public static List<CountryProgram> getAvailableCountryPrograms() {
        if(countryPrograms == null) {
            countryPrograms = new ArrayList<>();
            countryPrograms.add(buildCountryProgram("GLOBAL", AppTheme, global_channel, "U-Report Global", 13
                    , rapidpro_host_address1, ureport_host_address1, "UReportGlobal", "ureportglobal", "U-Reporters"));
            countryPrograms.add(buildCountryProgram("BRA", AppTheme_Brasil, brasil_channel, "Brasil", 1
                    , rapidpro_host_address2, ureport_host_address2, "ureportbrasil", "ureport.brasil", "UReport Brasil"));
            countryPrograms.add(buildCountryProgram("BDI", AppTheme_Burundi, INVALID_VALUE, "Burundi", 5
                    , rapidpro_host_address1, ureport_host_address1, "UReportGlobal", "U-report-Burundi-213297045697711", null));
            countryPrograms.add(buildCountryProgram("CMR", AppTheme_Cameroun, INVALID_VALUE, "Cameroun", 10
                    , rapidpro_host_address1, ureport_host_address1, "UReportCameroon", "ureportcameroon", null));
            countryPrograms.add(buildCountryProgram("CHL", AppTheme_Chile, chile_channel, "Chile", 12
                    , rapidpro_host_address1, ureport_host_address1, "ureportchile", "ureportchile", "UReporters"));
            countryPrograms.add(buildCoteDIvoire());
			countryPrograms.add(buildCountryProgram("FJI", AppTheme_Fiji, R.string.fiji_channel, "Fiji", INVALID_VALUE
					, rapidpro_host_address1, ureport_host_address1, null, null, "UReporters"));
            countryPrograms.add(buildGtmCountry());
            countryPrograms.add(buildCountryProgram("IDN", AppTheme_Indonesia, indonesia_channel, "Indonesia", 15
                    , rapidpro_host_address1, ureport_host_address1, "UReport_id", "UNICEFIndonesia", "UReporters_Indonesia"));
            countryPrograms.add(buildCountryProgram("IRL", AppTheme_Ireland, ireland_channel, "Ireland", 2
                    , rapidpro_host_address2, ureport_host_address2, "UReportIRL", "ureportIRL", "U-Reporters"));
            countryPrograms.add(buildCountryProgram("LBR", AppTheme_Liberia, INVALID_VALUE, "Liberia", 6
                    , rapidpro_host_address1, ureport_host_address1, "UReportLiberia", "ureport.liberia", null));
            countryPrograms.add(buildCountryProgram("MLI", AppTheme_Mali, INVALID_VALUE, "Mali", 3
                    , rapidpro_host_address1, ureport_host_address1, "UReportMali", "UreportMali", null));
            countryPrograms.add(buildCountryProgram("MEX", AppTheme_Mexico, mexico_channel, "México", 9
                    , rapidpro_host_address1, ureport_host_address1, "UReportMexico", "UReportMexico", "UReporters"));
//            countryPrograms.add(buildCountryProgram("MMR", R.style.AppTheme_Myanmar, INVALID_VALUE, "Myanmar",  INVALID_VALUE, "UReportMyanmar", "UReporters"));
            countryPrograms.add(buildCountryProgram("NGA", AppTheme_Nigeria, nigeria_channel, "Nigeria", 1
                    , rapidpro_host_address1, ureport_host_address1, "UReportNigeria", "U-report-Nigeria-1429673597287501", "UReporters"));
            countryPrograms.add(buildCountryProgram("PAK", AppTheme_Pakistan, INVALID_VALUE, "Pakistan", 16
                    , rapidpro_host_address1, ureport_host_address1, "PakAvaz", "ureportpakavaz", null));
            countryPrograms.add(buildPapuaNewGuinea());
            countryPrograms.add(buildCountryProgram("CAF", AppTheme_RepubliqueCentrafricaine, INVALID_VALUE, "République Centrafricaine", 8
                    , rapidpro_host_address1, ureport_host_address1, "Ureport_rca", "ureport.rca", null));
            countryPrograms.add(buildCountryProgram("SEN", AppTheme_Senegal, INVALID_VALUE, "Sénégal", 14
                    , rapidpro_host_address1, ureport_host_address1, "ureportsenegal", "UreportSenegal", null));
            countryPrograms.add(buildCountryProgram("SLE", AppTheme_SierraLeone, INVALID_VALUE, "Sierra Leone", 7
                    , rapidpro_host_address1, ureport_host_address1, "UreportSL", "U-report-Sierra-Leone-361005830734231", null));
            countryPrograms.add(buildCountryProgram("SYR", AppTheme_Syria, syria_channel, "Syria", 6
                    , rapidpro_host_address2, ureport_host_address2, "UReportSyria", "UReportSyria", "U-Reporters"));
            countryPrograms.add(buildCountryProgram("SWZ", AppTheme_Swaiziland, INVALID_VALUE, "Swaziland", 4
                    , rapidpro_host_address1, ureport_host_address1, "Ureportszd", "Swaziland-U-Report-1407332376221373", null));
            countryPrograms.add(buildTha());
            countryPrograms.add(buildTun());
            countryPrograms.add(buildCountryProgram("UGA", AppTheme_Uganda, uganda_channel, "Uganda", 18
                    , rapidpro_host_address1, ureport_host_address1, "UReportUganda", "UReportUganda", "U-Reporters"));
            countryPrograms.add(buildGbrCountry());
            countryPrograms.add(buildCountryProgram("UKR", AppTheme_Ukraine, ukraine_channel, "Ukraine", 19
					, rapidpro_host_address1, ureport_host_address1, "ureportukraine", "ureportukraine", "UReporters"));
            countryPrograms.add(buildCountryProgram("ZWE", AppTheme_Zimbabwe, INVALID_VALUE, "Zimbabwe", 2
                    , rapidpro_host_address1, ureport_host_address1, "Ureportzim", "U-Report-Zimbabwe-1477396805878097", null));
        }
        return countryPrograms;
    }

    private static CountryProgram buildTun() {
        CountryProgram tunCountry = buildCountryProgram("TUN", AppTheme_Tunisia, tunisie_channel, "Tunisie", 31,
                rapidpro_host_address1, ureport_host_address1, "UReportTunisie", "uReportTunisie", "U-Reporters");
        tunCountry.setMaleGroup("U-Reporters Homme");
        tunCountry.setFemaleGroup("U-Reporters Femmes");
        return tunCountry;
    }

    @NonNull
    private static CountryProgram buildGtmCountry() {
        CountryProgram gtmCountry = buildCountryProgram("GTM", AppTheme_Guatemala, R.string.guatemala_channel, "Guatemala", 7
                , rapidpro_host_address2, ureport_host_address2, "UReportGua", "ureportglobal", "U-Reporters");
        gtmCountry.setMaleGroup("UReport Males");
        gtmCountry.setFemaleGroup("UReport Female");
        gtmCountry.setStateField("department");
        gtmCountry.setDistrictField("district");
        return gtmCountry;
    }

    @NonNull
    private static CountryProgram buildCoteDIvoire() {
        CountryProgram cviCountry = buildCountryProgram("CIV", AppTheme_CoteDIvoire, cotedivoire_channel, "Côte d'Ivoire", 26
                , rapidpro_host_address1, ureport_host_address1, "UReport_CIV", "U-Report-Côte-dIvoire-1218965818134275", "U-Reporters Cote d'Ivoire");
        cviCountry.setMaleGroup("U-Reporters Homme");
        cviCountry.setFemaleGroup("U-Reporters Femmes");

        List<AgeGroup> ageGroups = new ArrayList<>();
        ageGroups.add(new AgeGroup("Adolescents", 14, 19));
        ageGroups.add(new AgeGroup("Jeunes", 20, 24));
        ageGroups.add(new AgeGroup("Adults Jeunes", 25, 35));
        ageGroups.add(new AgeGroup("Adulte", 36));
        cviCountry.setAgeGroups(ageGroups);
        cviCountry.setStateField("state");

        return cviCountry;
    }

    @NonNull
    private static CountryProgram buildPapuaNewGuinea() {
        CountryProgram pngCountry = buildCountryProgram("PNG", AppTheme_PapuaNewGuinea, newguinea_channel, "Papua New Guinea", 28
                , rapidpro_host_address1, ureport_host_address1, null, "UReportPNG", "U-Reporters");
        pngCountry.setMaleGroup("U-Reporters Male");
        pngCountry.setFemaleGroup("U-Reporters Female");
        pngCountry.setStateField("region");

        return pngCountry;
    }

    @NonNull
    private static CountryProgram buildTha() {
        CountryProgram thaCountry = buildCountryProgram("THA", AppTheme_Thailand, thailand_channel, "Thailand", 5
                , rapidpro_host_address2, ureport_host_address2, "UReportThai", "ureportglobal", "U-Reporters");
        thaCountry.setMaleGroup("U-Reporters Male");
        thaCountry.setFemaleGroup("U-Reporters Female");

        List<AgeGroup> ageGroups = new ArrayList<>();
        ageGroups.add(new AgeGroup("6 - 9", 6, 9));
        ageGroups.add(new AgeGroup("10 - 13", 10, 13));
        ageGroups.add(new AgeGroup("14 - 18", 14, 18));
        ageGroups.add(new AgeGroup("19 - 23", 19, 23));
        ageGroups.add(new AgeGroup("24 - 28", 24, 28));
        ageGroups.add(new AgeGroup("29 - 33", 29, 33));
        ageGroups.add(new AgeGroup("> 34", 34));
        thaCountry.setAgeGroups(ageGroups);
        thaCountry.setStateField("province");

        return thaCountry;
    }

    @NonNull
    private static CountryProgram buildGbrCountry() {
        CountryProgram gbrCountry = buildCountryProgram("GBR", AppTheme_UnitedKingdom, unitedkingdom_channel, "United Kingdom", 3
                , rapidpro_host_address2, ureport_host_address2, "UReportUK", "ureportglobal", "U-Reporters");
        gbrCountry.setMaleGroup("U-Reporters Male");
        gbrCountry.setFemaleGroup("U-Reporters Female");

        List<AgeGroup> ageGroups = new ArrayList<>();
        ageGroups.add(new AgeGroup("U-Reporters 13-18", 13, 18));
        ageGroups.add(new AgeGroup("U-Reporters 18-25", 18, 25));
        ageGroups.add(new AgeGroup("U-Reporters 26+", 26));
        gbrCountry.setAgeGroups(ageGroups);

        return gbrCountry;
    }

    @NonNull
    private static CountryProgram buildCountryProgram(String global, int appTheme, int channel, String name, int organization
            , int rapidproEndpoint, int ureportEndpoint, String twitter, String facebook, String group) {
        return new CountryProgram(global, appTheme, channel, name, organization, rapidproEndpoint, ureportEndpoint, twitter, facebook, group);
    }

}
