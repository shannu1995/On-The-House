package com.onthehouse.details;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by haseebjehangir on 24/8/17.
 */

public class Member
{
    private static Member instance = null;

    private int id;
    private String title;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private int date_logged_in;
    private int status;
    private String phone_number;
    private String address1;
    private String address2;
    private String city;
    private int zone_id;
    private int zip_code;
    private int country_id;
    private int age;
    private String language_id;
    private int timezone_id;
    private int membership_level_id;
    private int membership_expiry;
    private int membership_id;
    private int membership_expiry_email;
    private int newsletters;
    private int focus_groups;
    private int paid_marketing;
    private int google_calender;
    private String categories;
    private String nickname;
    private String image;
    private String gc_access_token;
    private int foundation_member;
    private int oauth_uid;
    private String oauth_provider;
    private int limit_num_reservations;

    protected Member()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDate_logged_in() {
        return date_logged_in;
    }

    public void setDate_logged_in(int date_logged_in) {
        this.date_logged_in = date_logged_in;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public int getZip_code() {
        return zip_code;
    }

    public void setZip_code(int zip_code) {
        this.zip_code = zip_code;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public int getTimezone_id() {
        return timezone_id;
    }

    public void setTimezone_id(int timezone_id) {
        this.timezone_id = timezone_id;
    }

    public int getMembership_level_id() {
        return membership_level_id;
    }

    public void setMembership_level_id(int membership_level_id) {
        this.membership_level_id = membership_level_id;
    }

    public int getMembership_expiry() {
        return membership_expiry;
    }

    public void setMembership_expiry(int membership_expiry) {
        this.membership_expiry = membership_expiry;
    }

    public int getMembership_id() {
        return membership_id;
    }

    public void setMembership_id(int membership_id) {
        this.membership_id = membership_id;
    }

    public int getMembership_expiry_email() {
        return membership_expiry_email;
    }

    public void setMembership_expiry_email(int membership_expiry_email) {
        this.membership_expiry_email = membership_expiry_email;
    }

    public int getNewsletters() {
        return newsletters;
    }

    public void setNewsletters(int newsletters) {
        this.newsletters = newsletters;
    }

    public int getFocus_groups() {
        return focus_groups;
    }

    public void setFocus_groups(int focus_groups) {
        this.focus_groups = focus_groups;
    }

    public int getPaid_marketing() {
        return paid_marketing;
    }

    public void setPaid_marketing(int paid_marketing) {
        this.paid_marketing = paid_marketing;
    }

    public int getGoogle_calender() {
        return google_calender;
    }

    public void setGoogle_calender(int google_calender) {
        this.google_calender = google_calender;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGc_access_token() {
        return gc_access_token;
    }

    public void setGc_access_token(String gc_access_token) {
        this.gc_access_token = gc_access_token;
    }

    public int getFoundation_member() {
        return foundation_member;
    }

    public void setFoundation_member(int foundation_member) {
        this.foundation_member = foundation_member;
    }

    public int getOauth_uid() {
        return oauth_uid;
    }

    public void setOauth_uid(int oauth_uid) {
        this.oauth_uid = oauth_uid;
    }

    public String getOauth_provider() {
        return oauth_provider;
    }

    public void setOauth_provider(String oauth_provider) {
        this.oauth_provider = oauth_provider;
    }

    public int getLimit_num_reservations() {
        return limit_num_reservations;
    }

    public void setLimit_num_reservations(int limit_num_reservations) {
        this.limit_num_reservations = limit_num_reservations;
    }

/*    public Member getMember(Context context)
    {
        SharedPreferences saver = context.getSharedPreferences("Member", Context.MODE_PRIVATE);
        id = saver.getInt("memId", 0);
        title = saver.getString("memTitle", "");
        first_name = saver.getString("memFName", "");
        first_name = saver.getString("memLName", "");
        first_name = saver.getString("memEmail", "");
        first_name = saver.getString("memPass", "");
        first_name = saver.getString("memDateLogged", "");
        first_name = saver.getString("memStatus", "");
        first_name = saver.getString("memAdd1", "");
        first_name = saver.getString("memAdd2", "");
        first_name = saver.getString("memCity", "");
        first_name = saver.getString("memZoneId", "");
        first_name = saver.getString("memZipCode", "");
        first_name = saver.getString("memCountryId", "");
        id = saver.getInt("memAge", 0);
        id = saver.getInt("memLanguageId", 0);
        id = saver.getInt("memTimeId", 0);
        id = saver.getInt("membershipLevelId", 0);
        id = saver.getInt("membershipExpiry", 0);
        id = saver.getInt("membershipId", 0);
        id = saver.getInt("memId", 0);
        id = saver.getInt("memId", 0);
        id = saver.getInt("memId", 0);
        id = saver.getInt("memId", 0);
        id = saver.getInt("memId", 0);
        id = saver.getInt("memId", 0);
        id = saver.getInt("memId", 0);
        id = saver.getInt("memId", 0);
        id = saver.getInt("memId", 0);
        id = saver.getInt("memId", 0);

        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");
        first_name = saver.getString("memId", "");

    }*/

    public static Member getInstance()
    {
        if(instance == null)
        {
            instance = new Member();
        }

        return instance;
    }
}
