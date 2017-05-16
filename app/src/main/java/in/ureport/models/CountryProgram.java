package in.ureport.models;

import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import in.ureport.BuildConfig;
import in.ureport.R;
import in.ureport.models.rapidpro.AgeGroup;

/**
 * Created by johncordeiro on 7/23/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryProgram {

    public static final String GROUP_UREPORT_MALES = "UReport Males";

    public static final String GROUP_UREPORT_FEMALES = "UReport Females";

    private String code;

    @StyleRes
    private int theme;

    @StringRes
    private int channel;

    private String name;

    private int organization;

    @StringRes
    private int rapidproEndpoint;

    @StringRes
    private int ureportEndpoint;

    private String twitter;

    private String facebook;

    private String group;

    private String stateField;

    private String districtField;

    private String maleGroup = GROUP_UREPORT_MALES;

    private String femaleGroup = GROUP_UREPORT_FEMALES;

    private List<AgeGroup> ageGroups;

    public CountryProgram(String code, @StyleRes int theme, @StringRes int channel
            , String name, int organization, int rapidproEndpoint, int ureportEndpoint, String twitter, String facebook, String group) {
        this.code = code;
        this.theme = theme;
        this.channel = channel;
        this.name = name;
        this.organization = organization;
        this.rapidproEndpoint = rapidproEndpoint;
        this.ureportEndpoint = ureportEndpoint;
        this.twitter = twitter;
        this.facebook = facebook;
        this.group = group;
    }

    public CountryProgram(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public CountryProgram(String code) {
        this.code = code;
    }

    @StyleRes
    public int getTheme() {
        return theme;
    }

    public void setTheme(@StyleRes int theme) {
        this.theme = theme;
    }

    public int getChannel() {
        if (BuildConfig.DEBUG) {
            return R.string.fcm_client_channel;
        }
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrganization() {
        return organization;
    }

    public void setOrganization(int organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRapidproEndpoint() {
        if (BuildConfig.DEBUG) {
            return R.string.fcm_client_host;
        }
        return rapidproEndpoint;
    }

    public void setRapidproEndpoint(int rapidproEndpoint) {
        this.rapidproEndpoint = rapidproEndpoint;
    }

    public int getUreportEndpoint() {
        return ureportEndpoint;
    }

    public void setUreportEndpoint(int ureportEndpoint) {
        this.ureportEndpoint = ureportEndpoint;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStateField() {
        return stateField;
    }

    public void setStateField(String stateField) {
        this.stateField = stateField;
    }

    public String getDistrictField() {
        return districtField;
    }

    public void setDistrictField(String districtField) {
        this.districtField = districtField;
    }

    public String getMaleGroup() {
        return maleGroup;
    }

    public void setMaleGroup(String maleGroup) {
        this.maleGroup = maleGroup;
    }

    public String getFemaleGroup() {
        return femaleGroup;
    }

    public void setFemaleGroup(String femaleGroup) {
        this.femaleGroup = femaleGroup;
    }

    public List<AgeGroup> getAgeGroups() {
        return ageGroups;
    }

    public CountryProgram setAgeGroups(List<AgeGroup> ageGroups) {
        this.ageGroups = ageGroups;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountryProgram that = (CountryProgram) o;
        return code.equalsIgnoreCase(that.code);

    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
