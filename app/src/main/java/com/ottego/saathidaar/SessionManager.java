package com.ottego.saathidaar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ottego.saathidaar.Model.HoroscopeModel;
import com.ottego.saathidaar.Model.SessionModel;
import com.ottego.saathidaar.Model.SessionProfileDetailModel;

public class SessionManager {

    public static final String KEY_MEMBER_ID = "member_id";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PHONE1 = "phone";
    public static final String KEY_EMAIL1 = "email";
    public static final String KEY_ENABLE = "enabled";
    public static final String KEY_USERID = "username";


    //USER DETAIL
    public static final String KEY_FNAME = "firstName";
    public static final String KEY_LNAME = "lastname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_ROLE = "role";
    public static final String KEY_CREATEDBY = "profilecreatedby";


    //Profile details


    public static final String KEY_HEIGHT = "height";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_PRO_LIFESTYLE = "lifestyles";
    public static final String KEY_PRO_ABOUTUS = "about_ourself";
    public static final String KEY_PRO_KNOWN_LANG = "known_languages";
    public static final String KEY_PRO_EDUC = "education";
    public static final String KEY_PRO_JOB = "job";
    public static final String KEY_PRO_HOBBIE = "hobbies";
    public static final String KEY_PRO_EXPEC = "expectations";
    public static final String KEY_PRO_RELIGION_NAME = "religion_name";
    public static final String KEY_PRO_CASTENAME = "caste_name";
    public static final String KEY_PRO_SUB_CASTE_NAME = "sub_caste_name";
    public static final String KEY_PRO_STATE = "state_name";
    public static final String KEY_PRO_CITY = "city_name";
    public static final String KEY_PRO_MOTHERTONGUE = "mother_tounge";
    public static final String KEY_PRO_MARITAL_STATUS = "marital_status";
    public static final String KEY_PRO_NO_CHILD = "no_of_children";
    public static final String KEY_PRO_DOB = "date_of_birth";
    public static final String KEY_PRO_AGE = "age";
    public static final String KEY_PRO_HEALTH = "health_info";
    public static final String KEY_PRO_BLOOD = "blood_group";
    public static final String KEY_PRO_GOTRA = "gothra";
    public static final String KEY_PRO_CORIGION = "ethnic_corigin";
    public static final String KEY_PRO_PIN = "pincode";
    public static final String KEY_PRO_F_STAUS = "father_status";
    public static final String KEY_PRO_F_COMP = "father_company_name";
    public static final String KEY_PRO_F_DESI = "father_designation";
    public static final String KEY_PRO_F_BUSSI_NAME = "father_business_name";
    public static final String KEY_PRO_M_STATUS = "mother_status";
    public static final String KEY_PRO_M_COMP = "mother_company_name";
    public static final String KEY_PRO_M_DESI = "mother_designation";
    public static final String KEY_PRO_M_BUSSI_NAME = "mother_business_name";
    public static final String KEY_PRO_FMLY_LOCA = "family_location";
    public static final String KEY_PRO_NATIVE_PLACE = "native_place";
    public static final String KEY_PRO_FMLY_TYPE = "family_type";
    public static final String KEY_PRO_FMLY_AFFLU = "family_affluence";
    public static final String KEY_PRO_H_QUALI = "highest_qualification";
    public static final String KEY_PRO_COLLEGE = "college_attended";
    public static final String KEY_PRO_WORKINGWITH = "working_with";
    public static final String KEY_PRO_WORKINGAS = "working_as";
    public static final String KEY_PRO_EMPNAME = "employer_name";
    public static final String KEY_PRO_ANNUAL_INCOME = "annual_income";
    public static final String KEY_PRO_CASTEID = "cast_id";
    public static final String KEY_PRO_NAKSHTRA = "nakshatra";
    public static final String KEY_PRO_STATEID = "state_id";
    public static final String KEY_PRO_UNMARRIED_FEMALE = "unmarried_female";
    public static final String KEY_PRO_FAMILY_DETAILS = "FamilyDetails";
    public static final String KEY_PRO_MARRIED_FEMALE = "married_female";
    public static final String KEY_PRO_MARRIED_MALE = "married_male";
    public static final String KEY_PRO_UNMARRIED_MALE = "unmarried_male";
    public static final String KEY_PRO_CITY_BIRTH = "city_of_birth";
    public static final String KEY_PRO_RELIGIONID = "religion_id";
    public static final String KEY_PRO_GENDER = "gender";
    public static final String KEY_PRO_CITY1 = "city";
    public static final String KEY_FAMILY_VALUE = "familyValues";
    public static final String KEY_MEM_NATIVE = "membernative";
    public static final String KEY_PRO_PROFILECREATE = "profilecreatedby";
    public static final String KEY_COUNTRY = "country_name";
    public static final String KEY_TIMESTATUS = "time_status";
    public static final String KEY_PRO_LAST_F = "first_name";
    public static final String KEY_PRO_LAST_N = "last_name";
    public static final String KEY_MANGLIK = "manglik";
    public static final String KEY_PRO_MOB = "contact_number";
    public static final String KEY_PRO_SUNCASTE = "subcaste";

    //Horoscope

    public static final String KEY_HOUR = "hours";
    public static final String KEY_MINUTES = "minutes";
    public static final String KEY_COUNTRY_B = "country_of_birth";
    public static final String KEY_MANGLIK1 = "manglik";
    public static final String KEY_TIME = "time";
    public static final String KEY_TIME_STATUS = "time_status";
    public static final String KEY_CITY_B = "city_of_birth";

    private static final String PREF_NAME = "userData";
    private static final String IS_LOGIN = "isLogin";
    private static SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createSession(SessionModel model) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_MEMBER_ID, model.member_id);
        editor.putString(KEY_FIRSTNAME, model.firstName);
        editor.putString(KEY_LASTNAME, model.lastName);
        editor.putString(KEY_PHONE1, model.phone);
        editor.putString(KEY_EMAIL1, model.email);
        editor.putString(KEY_ENABLE, model.enabled);
        editor.putString(KEY_USERID, model.username);

        editor.commit();
    }


    public String getMemberId() {
        return pref.getString(KEY_MEMBER_ID, "");
    }

    public String getName() {
        return pref.getString(KEY_FIRSTNAME, " ");
    }

    public String getLastName() {
        return pref.getString(KEY_LASTNAME, "");
    }

    public String getPhone1() {
        return pref.getString(KEY_PHONE1, "");
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL1, "");
    }

    public void CreateProfileSession(SessionProfileDetailModel model) {
        editor.putString(KEY_PRO_LIFESTYLE, model.lifestyles);
        editor.putString(KEY_PRO_ABOUTUS, model.about_ourself);
        editor.putString(KEY_PRO_KNOWN_LANG, model.known_languages);
        editor.putString(KEY_PRO_EDUC, model.education);
        editor.putString(KEY_PRO_JOB, model.job);
        editor.putString(KEY_HEIGHT,model.height);
        editor.putString(KEY_PRO_HOBBIE, model.hobbies);
        editor.putString(KEY_PRO_EXPEC, model.expectations);
        editor.putString(KEY_PRO_RELIGION_NAME, model.religion_name);
        editor.putString(KEY_PRO_SUB_CASTE_NAME, model.sub_caste_name);
        editor.putString(KEY_PRO_CASTENAME, model.caste_name);
        editor.putString(KEY_PRO_MOTHERTONGUE, model.mother_tounge);
        editor.putString(KEY_PRO_MARITAL_STATUS, model.marital_status);
        editor.putString(KEY_PRO_NO_CHILD, model.no_of_children);
        editor.putString(KEY_PRO_DOB, model.date_of_birth);
        editor.putString(KEY_PRO_AGE, model.age);
        editor.putString(KEY_PRO_HEALTH, model.health_info);
        editor.putString(KEY_PRO_BLOOD, model.blood_group);
        editor.putString(KEY_PRO_GOTRA, model.gothra);
        editor.putString(KEY_PRO_CORIGION, model.ethnic_corigin);
        editor.putString(KEY_PRO_PIN, model.pincode);
        editor.putString(KEY_PRO_F_STAUS, model.father_status);
        editor.putString(KEY_PRO_F_COMP, model.father_company_name);
        editor.putString(KEY_PRO_F_DESI, model.father_designation);
        editor.putString(KEY_PRO_F_BUSSI_NAME, model.father_business_name);
        editor.putString(KEY_PRO_M_STATUS, model.mother_status);
        editor.putString(KEY_PRO_M_COMP, model.mother_company_name);
        editor.putString(KEY_PRO_M_DESI, model.mother_designation);
        editor.putString(KEY_PRO_M_BUSSI_NAME, model.mother_business_name);
        editor.putString(KEY_PRO_FMLY_LOCA, model.family_location);
        editor.putString(KEY_PRO_NATIVE_PLACE, model.native_place);
        editor.putString(KEY_PRO_FMLY_TYPE, model.family_type);
        editor.putString(KEY_PRO_FMLY_AFFLU, model.family_affluence);
        editor.putString(KEY_PRO_H_QUALI, model.highest_qualification);
        editor.putString(KEY_PRO_COLLEGE, model.college_attended);
        editor.putString(KEY_PRO_WORKINGWITH, model.working_with);
        editor.putString(KEY_PRO_WORKINGAS, model.working_as);
        editor.putString(KEY_PRO_EMPNAME, model.employer_name);
        editor.putString(KEY_PRO_ANNUAL_INCOME, model.annual_income);
        editor.putString(KEY_PRO_NAKSHTRA, model.nakshatra);
        editor.putString(KEY_PRO_UNMARRIED_FEMALE, model.unmarried_female);
        editor.putString(KEY_PRO_MARRIED_FEMALE, model.married_female);
        editor.putString(KEY_PRO_MARRIED_MALE, model.married_male);
        editor.putString(KEY_PRO_UNMARRIED_MALE, model.unmarried_male);
        editor.putString(KEY_PRO_CITY_BIRTH, model.city_of_birth);
        editor.putString(KEY_PRO_GENDER, model.gender);
        editor.putString(KEY_PRO_CITY1, model.city);
        editor.putString(KEY_FAMILY_VALUE, model.familyValues);
        editor.putString(KEY_MEM_NATIVE, model.membernative);
        editor.putString(KEY_PRO_PROFILECREATE, model.profilecreatedby);
        editor.putString(KEY_COUNTRY, model.country_name);
        editor.putString(KEY_TIMESTATUS, model.time_status);
        editor.putString(KEY_PRO_LAST_F, model.first_name);
        editor.putString(KEY_PRO_LAST_N, model.last_name);
        editor.putString(KEY_MANGLIK, model.manglik);
        editor.putString(KEY_PRO_MOB, model.contact_number);

        editor.commit();
    }

    //getProfile details............

    public String getKeyProLifestyle() {
        return pref.getString(KEY_PRO_LIFESTYLE, "");
    }

    public String getKeyProAboutus() {
        return pref.getString(KEY_PRO_ABOUTUS, "");
    }

    public String getKeyProKnownLang() {
        return pref.getString(KEY_PRO_KNOWN_LANG, "");
    }

    public String getKeyProEduc() {
        return pref.getString(KEY_PRO_EDUC, "");
    }

    public String getKeyProJob() {
        return pref.getString(KEY_PRO_JOB, "");
    }

    public String getKeyProExpec() {
        return pref.getString(KEY_PRO_EXPEC, "");
    }

    public String getKeyProReligionName() {
        return pref.getString(KEY_PRO_RELIGION_NAME, "");
    }



    public String getKeyProSubCasteName() {
        return pref.getString(KEY_PRO_SUB_CASTE_NAME, "");
    }

    public String getKeyProCastename() {
        return pref.getString(KEY_PRO_CASTENAME, "");
    }

    public String getKeyProMothertongue() {
        return pref.getString(KEY_PRO_MOTHERTONGUE, "");
    }

    public String getKeyProMaritalStatus() {
        return pref.getString(KEY_PRO_MARITAL_STATUS, "");
    }

    public String getKeyProNoChild() {
        return pref.getString(KEY_PRO_NO_CHILD, "");
    }

    public String getKeyProDob() {
        return pref.getString(KEY_PRO_DOB, "");
    }

    public String getKeyProAge() {
        return pref.getString(KEY_PRO_AGE, "");
    }
    public String getKeyHeight() {
        return pref.getString(KEY_HEIGHT, "");
    }

    public String getKeyProHealth() {
        return pref.getString(KEY_PRO_HEALTH, "");
    }

    public String getKeyProBlood() {
        return pref.getString(KEY_PRO_BLOOD, "");
    }

    public String getKeyProGotra() {
        return pref.getString(KEY_PRO_GOTRA, "");
    }

    public String getKeyProCorigion() {
        return pref.getString(KEY_PRO_CORIGION, "");
    }

    public String getKeyProPin() {
        return pref.getString(KEY_PRO_PIN, "");
    }

    public String getKeyProFStaus() {
        return pref.getString(KEY_PRO_F_STAUS, "");
    }

    public String getKeyProFComp() {
        return pref.getString(KEY_PRO_F_COMP, "");
    }



    public String getKeyProFDesi() {
        return pref.getString(KEY_PRO_F_DESI, "");
    }

    public String getKeyProFBussiName() {
        return pref.getString(KEY_PRO_F_BUSSI_NAME, "");
    }

    public String getKeyProMStatus() {
        return pref.getString(KEY_PRO_M_STATUS, "");
    }

    public String getKeyProMDesi() {
        return pref.getString(KEY_PRO_M_DESI, "");
    }

    public String getKeyProMBussiName() {
        return pref.getString(KEY_PRO_M_BUSSI_NAME, "");
    }

    public String getKeyProMComp() {
        return pref.getString(KEY_PRO_M_COMP, "");
    }

    public String getKeyProFmlyLoca() {
        return pref.getString(KEY_PRO_FMLY_LOCA, "");
    }

    ///
    public String getKeyProNativePlace() {
        return pref.getString(KEY_PRO_NATIVE_PLACE, "");
    }

    public String getKeyProFmlyType() {
        return pref.getString(KEY_PRO_FMLY_TYPE, "");
    }

    public String getKeyProFmlyAfflu() {
        return pref.getString(KEY_PRO_FMLY_AFFLU, "");
    }
    public String getKeyProHQuali() {
        return pref.getString(KEY_PRO_H_QUALI, "");
    }

    public String getKeyProCollege() {
        return pref.getString(KEY_PRO_COLLEGE, "");
    }

    public String getKeyProWorkingwith() {
        return pref.getString(KEY_PRO_WORKINGWITH, "");
    }
    public String getKeyProWorkingas() {
        return pref.getString(KEY_PRO_WORKINGAS, "");
    }

    public String getKeyProAnnualIncome() {
        return pref.getString(KEY_PRO_ANNUAL_INCOME, "");
    }

    public String getKeyProNakshtraannu() {
        return pref.getString(KEY_PRO_NAKSHTRA, "");
    }
    public String getKeyProUnmarriedFemale() {
        return pref.getString(KEY_PRO_UNMARRIED_FEMALE, "");
    }

    public String getKeyProMarriedFemale() {
        return pref.getString(KEY_PRO_MARRIED_FEMALE, "");
    }

    public String getKeyProMarriedMale() {
        return pref.getString(KEY_PRO_MARRIED_MALE, "");
    }

    public String getKeyProUnmarriedMale() {
        return pref.getString(KEY_PRO_UNMARRIED_MALE, "");
    }

    public String getKeyProCityBirth() {
        return pref.getString(KEY_PRO_CITY_BIRTH, "");
    }

    public String getKeyProGender() {
        return pref.getString(KEY_PRO_GENDER, "");
    }

    public String getKeyProCity1() {
        return pref.getString(KEY_PRO_CITY1, "");
    }
    public String getKeyFamilyValue() {
        return pref.getString(KEY_FAMILY_VALUE, "");
    }

    public String getKeyMemNative() {
        return pref.getString(KEY_MEM_NATIVE, "");
    }
    public String getKeyCreatedby() {
        return pref.getString(KEY_PRO_PROFILECREATE, "");
    }

    public String getKeyCountry() {
        return pref.getString(KEY_COUNTRY, "");
    }
    public String getKeyTimestatus() {
        return pref.getString(KEY_TIMESTATUS, "");
    }

    public String getKeyProLastF() {
        return pref.getString(KEY_PRO_LAST_F, "");
    }
    public String getKeyLastname() {
        return pref.getString(KEY_PRO_LAST_N, "");
    }
    public String getKeyManglik() {
        return pref.getString(KEY_MANGLIK, "");
    }
    public String getKeyProMob() {
        return pref.getString(KEY_PRO_MOB, "");
    }



    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    public void logoutUser() {
        clearSession();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);

    }




    public void createHoroscope(HoroscopeModel model) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_MEMBER_ID, model.hours);
        editor.putString(KEY_FIRSTNAME, model.minutes);
        editor.putString(KEY_LASTNAME, model.time_status);
        editor.putString(KEY_PHONE1, model.manglik);
        editor.putString(KEY_EMAIL1, model.city_of_birth);
        editor.putString(KEY_ENABLE, model.country_of_birth);
        editor.commit();
    }


}
