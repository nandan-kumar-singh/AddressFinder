package in.androidfluid.locationsearch.util;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.Serializable;
import in.androidfluid.locationsearch.model.Address;

/**
 * Created by nandan on 4/5/17.
 */

public abstract class AddressFinder extends AsyncTask<Void, Void, Address> {

    private static final String TAG = AddressFinder.class.getSimpleName();
    private static Address mAddress = new Address();
    private String address = null;
    private JSONObject jsonObj;
    private String address1 = "";
    private double latitude, longitude;

    public AddressFinder(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AddressFinder(String address) {
        this.address = address;
    }


    private void setAddress() {

        try {
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");
                Log.d(TAG, "AddressArray: " + address_components.toString());

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (!TextUtils.isEmpty(long_name) || !long_name.equals("null") || long_name.length() > 0 || !long_name.equals("")) {
                        if (Type.equalsIgnoreCase("street_number")) {
                            mAddress.setAddress1(long_name + " ");
                        } else if (Type.equalsIgnoreCase("route")) {
                            mAddress.setAddress1(mAddress.getAddress1() + long_name);
                            Log.d(TAG, "address1 of setAddress: " + mAddress.getAddress1());
                            address1 = address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            mAddress.setAddress2(long_name);
                            Log.d(TAG, "Address2 of setAddress: " + mAddress.getAddress2());
                            //String address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            mAddress.setCity(long_name);
                            Log.d(TAG, "City of setAddress: " + mAddress.getCity());
                            //String city = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            mAddress.setCounty(long_name);
                            Log.d(TAG, "County of setAddress: " + mAddress.getCounty());
                            //String county = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            mAddress.setState(long_name);
                            Log.d(TAG, "State of setAddress: " + mAddress.getState());
                            //String state = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            mAddress.setCountry(long_name);
                            Log.d(TAG, "Country of setAddress: " + mAddress.getCountry());
                            //String country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            mAddress.setPIN(long_name);
                            Log.d(TAG, "Pin of setAddress: " + mAddress.getPIN());
                            //String PIN = long_name;
                        } else if (Type.equalsIgnoreCase("neighborhood")) {
                            mAddress.setArea(long_name);
                            //String area = long_name;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getGeoPoint() {
        try {
            longitude = ((JSONArray) jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            mAddress.setLongitude(longitude);
            latitude = ((JSONArray) jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            mAddress.setLatitude(latitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Address doInBackground(Void... params) {
        try {
            String SEARCH_TYPE_LAT_LONG = "?latlng=";
            String SEARCH_TYPE_ADDRESS = "?address=";
            String URL = "https://maps.google.com/maps/api/geocode/json" + (address == null ? SEARCH_TYPE_LAT_LONG : SEARCH_TYPE_ADDRESS) + URLEncoder.encode(latitude + "," + longitude, "utf8") +
                    "&sensor=false";
            Log.d(TAG, "URL: " + URL);

            java.net.URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb = sb.append(line).append("\n");
            }
            jsonObj = new JSONObject(sb.toString());
            setAddress();
            getGeoPoint();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAddress;
    }

    @Override
    protected void onPostExecute(Address address) {
        super.onPostExecute(address);
        onGetAddress(address);
    }

    abstract void onGetAddress(Address address);
}



 class Address {

    String Address1 = "";
    String Address2 = "";
    String City = "";
    String State = "";
    String Country = "";
    String County = "";
    String PIN = "";
    String Area = "";
    private String address;
    private double latitude;
    private double longitude;

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Address{" +
                "address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", Address1='" + Address1 + '\'' +
                ", Address2='" + Address2 + '\'' +
                ", City='" + City + '\'' +
                ", State='" + State + '\'' +
                ", Country='" + Country + '\'' +
                ", County='" + County + '\'' +
                ", PIN='" + PIN + '\'' +
                ", Area='" + Area + '\'' +
                '}';
    }
}
